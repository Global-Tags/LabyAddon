package com.rappytv.globaltags;

import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.command.GlobalTagCommand;
import com.rappytv.globaltags.config.GlobalTagConfig;
import com.rappytv.globaltags.interaction.EditBanInfoBulletPoint;
import com.rappytv.globaltags.interaction.ReferPlayerBulletPoint;
import com.rappytv.globaltags.interaction.ToggleBanBulletPoint;
import com.rappytv.globaltags.interaction.ChangeTagBulletPoint;
import com.rappytv.globaltags.interaction.ClearTagBulletPoint;
import com.rappytv.globaltags.interaction.ReportBulletPoint;
import com.rappytv.globaltags.listener.ServerNavigationListener;
import com.rappytv.globaltags.nametag.CustomTag;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.entity.player.tag.TagRegistry;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.revision.SimpleRevision;
import net.labymod.api.util.version.SemanticVersion;

@AddonMain
public class GlobalTagAddon extends LabyAddon<GlobalTagConfig> {

    public static final Component prefix = Component.empty()
        .append(Component.text("GlobalTags").color(NamedTextColor.BLUE).decorate(TextDecoration.BOLD))
        .append(Component.text(" » ", NamedTextColor.DARK_GRAY));

    private static GlobalTagAPI api;

    @Override
    protected void preConfigurationLoad() {
        Laby.references().revisionRegistry().register(new SimpleRevision("globaltags", new SemanticVersion("1.1.0"), "2023-11-24"));
        Laby.references().revisionRegistry().register(new SimpleRevision("globaltags", new SemanticVersion("1.1.7"), "2024-02-27"));
        Laby.references().revisionRegistry().register(new SimpleRevision("globaltags", new SemanticVersion("1.1.9"), "2024-06-01"));
        Laby.references().revisionRegistry().register(new SimpleRevision("globaltags", new SemanticVersion("1.2.0"), "2024-07-14"));
    }

    @Override
    protected void enable() {
        registerSettingCategory();
        api = new GlobalTagAPI(
            () -> addonInfo().getVersion(),
            () -> configuration().localizedResponses().get()
                ? Laby.labyAPI().minecraft().options().getCurrentLanguage()
                : "en_us"
        );

        TagRegistry tagRegistry = labyAPI().tagRegistry();
        for (PositionType positionType : PositionType.values())
            tagRegistry.registerBefore(
                "friendtags_tag",
                "globaltag",
                positionType,
                new CustomTag(this, positionType)
            );
        registerListener(new ServerNavigationListener());
        labyAPI().interactionMenuRegistry().register(new ChangeTagBulletPoint());
        labyAPI().interactionMenuRegistry().register(new ClearTagBulletPoint());
        labyAPI().interactionMenuRegistry().register(new EditBanInfoBulletPoint());
        labyAPI().interactionMenuRegistry().register(new ReferPlayerBulletPoint());
        labyAPI().interactionMenuRegistry().register(new ReportBulletPoint());
        labyAPI().interactionMenuRegistry().register(new ToggleBanBulletPoint());
        registerCommand(new GlobalTagCommand(this));
    }

    @Override
    protected Class<? extends GlobalTagConfig> configurationClass() {
        return GlobalTagConfig.class;
    }

    public static GlobalTagAPI getAPI() {
        return api;
    }
}
