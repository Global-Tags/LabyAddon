package com.rappytv.globaltags;

import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.command.GlobalTagCommand;
import com.rappytv.globaltags.config.GlobalTagConfig;
import com.rappytv.globaltags.interaction.ChangeTagBulletPoint;
import com.rappytv.globaltags.interaction.ClearTagBulletPoint;
import com.rappytv.globaltags.interaction.EditBanInfoBulletPoint;
import com.rappytv.globaltags.interaction.ReferPlayerBulletPoint;
import com.rappytv.globaltags.interaction.ReportBulletPoint;
import com.rappytv.globaltags.interaction.StaffNotesBulletPoint;
import com.rappytv.globaltags.interaction.TagHistoryBulletPoint;
import com.rappytv.globaltags.interaction.ToggleBanBulletPoint;
import com.rappytv.globaltags.listener.BroadcastListener;
import com.rappytv.globaltags.listener.ServerNavigationListener;
import com.rappytv.globaltags.nametag.CustomTag;
import com.rappytv.globaltags.wrapper.GlobalTagsAPI.Agent;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.entity.player.tag.TagRegistry;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.revision.SimpleRevision;
import net.labymod.api.util.version.SemanticVersion;

@AddonMain
public class GlobalTagAddon extends LabyAddon<GlobalTagConfig> {

    public static final Component prefix = Component.empty()
        .append(Component.text("GlobalTags").color(NamedTextColor.BLUE).decorate(TextDecoration.BOLD))
        .append(Component.text(" » ", NamedTextColor.DARK_GRAY));
    public static Icon roundIcon;

    private static GlobalTagAPI api;

    @Override
    protected void preConfigurationLoad() {
        Laby.references().revisionRegistry().register(new SimpleRevision("globaltags", new SemanticVersion("1.1.0"), "2023-11-24"));
        Laby.references().revisionRegistry().register(new SimpleRevision("globaltags", new SemanticVersion("1.1.7"), "2024-02-27"));
        Laby.references().revisionRegistry().register(new SimpleRevision("globaltags", new SemanticVersion("1.1.9"), "2024-06-01"));
        Laby.references().revisionRegistry().register(new SimpleRevision("globaltags", new SemanticVersion("1.2.0"), "2024-07-14"));
        Laby.references().revisionRegistry()
            .register(new SimpleRevision("globaltags", new SemanticVersion("1.3.5"), "2024-12-09"));
    }

    @Override
    protected void enable() {
        this.registerSettingCategory();
        api = new GlobalTagAPI(
            new Agent("LabyAddon", this.addonInfo().getVersion(), Laby.labyAPI().minecraft().getVersion()),
            () -> this.configuration().localizedResponses().get()
                ? Laby.labyAPI().minecraft().options().getCurrentLanguage()
                : "en_us"
        );
        roundIcon = Icon.texture(ResourceLocation.create("globaltags", "textures/icon_round.png"));

        TagRegistry tagRegistry = this.labyAPI().tagRegistry();
        for (PositionType positionType : PositionType.values())
            tagRegistry.registerBefore(
                "friendtags_tag",
                "globaltag",
                positionType,
                new CustomTag(this, positionType)
            );
        this.registerListener(new BroadcastListener(api));
        this.registerListener(new ServerNavigationListener());
        this.labyAPI().interactionMenuRegistry().register(new ChangeTagBulletPoint());
        this.labyAPI().interactionMenuRegistry().register(new ClearTagBulletPoint());
        this.labyAPI().interactionMenuRegistry().register(new EditBanInfoBulletPoint());
        this.labyAPI().interactionMenuRegistry().register(new ReferPlayerBulletPoint());
        this.labyAPI().interactionMenuRegistry().register(new ReportBulletPoint());
        this.labyAPI().interactionMenuRegistry().register(new StaffNotesBulletPoint());
        this.labyAPI().interactionMenuRegistry().register(new TagHistoryBulletPoint());
        this.labyAPI().interactionMenuRegistry().register(new ToggleBanBulletPoint());
        this.registerCommand(new GlobalTagCommand(this));
    }

    @Override
    protected Class<? extends GlobalTagConfig> configurationClass() {
        return GlobalTagConfig.class;
    }

    public static GlobalTagAPI getAPI() {
        return api;
    }
}
