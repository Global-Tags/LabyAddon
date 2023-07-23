package com.rappytv.globaltags;

import com.rappytv.globaltags.api.ApiHandler;
import com.rappytv.globaltags.command.GlobalTagCommand;
import com.rappytv.globaltags.config.GlobalTagConfig;
import com.rappytv.globaltags.context.ReportContext;
import com.rappytv.globaltags.listener.ServerNavigationListener;
import com.rappytv.globaltags.nametag.CustomTag;
import com.rappytv.globaltags.util.TagCache;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.entity.player.tag.TagRegistry;
import net.labymod.api.models.addon.annotation.AddonMain;
import java.util.Timer;
import java.util.TimerTask;

@AddonMain
public class GlobalTagAddon extends LabyAddon<GlobalTagConfig> {

    public static String prefix = "§1§lGlobalTags §8» §f";
    public static String version;
    private static GlobalTagAddon addon;
    private ApiHandler apiHandler;

    @Override
    protected void preConfigurationLoad() {
        this.apiHandler = new ApiHandler();
        addon = this;
    }

    @Override
    protected void enable() {
        registerSettingCategory();
        version = addon.addonInfo().getVersion();

        TagRegistry tagRegistry = labyAPI().tagRegistry();
        for (PositionType positionType : PositionType.values())
            tagRegistry.register(
                "friendtags_tag",
                positionType,
                new CustomTag(this, positionType)
            );
        registerListener(new ServerNavigationListener());
        labyAPI().interactionMenuRegistry().register(new ReportContext(this));
        registerCommand(new GlobalTagCommand(this));

        // Clear cache every 5 minutes
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                TagCache.clear();
            }
        }, 0, 1000 * 60 * 5);
    }

    @Override
    protected Class<? extends GlobalTagConfig> configurationClass() {
        return GlobalTagConfig.class;
    }

    public static GlobalTagAddon getAddon() {
        return addon;
    }
    public ApiHandler getApiHandler() {
        return apiHandler;
    }
}
