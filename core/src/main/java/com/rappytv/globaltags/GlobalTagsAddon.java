package com.rappytv.globaltags;

import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.command.GlobalTagCommand;
import com.rappytv.globaltags.config.GlobalTagsConfig;
import com.rappytv.globaltags.interaction.ChangeTagBulletPoint;
import com.rappytv.globaltags.interaction.ClearTagBulletPoint;
import com.rappytv.globaltags.interaction.EditBanInfoBulletPoint;
import com.rappytv.globaltags.interaction.ReferPlayerBulletPoint;
import com.rappytv.globaltags.interaction.ReportBulletPoint;
import com.rappytv.globaltags.interaction.StaffNotesBulletPoint;
import com.rappytv.globaltags.interaction.TagHistoryBulletPoint;
import com.rappytv.globaltags.interaction.ToggleBanBulletPoint;
import com.rappytv.globaltags.interaction.ToggleHideTagBulletPoint;
import com.rappytv.globaltags.listeners.BroadcastListener;
import com.rappytv.globaltags.listeners.LabyConnectDisconnectListener;
import com.rappytv.globaltags.listeners.ServerNavigationListener;
import com.rappytv.globaltags.listeners.WorldEnterListener;
import com.rappytv.globaltags.ui.activities.config.ReferralLeaderboardActivity;
import com.rappytv.globaltags.ui.nametag.GlobalTagNameTag;
import com.rappytv.globaltags.wrapper.GlobalTagsAPI.Agent;
import java.util.concurrent.TimeUnit;
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
import net.labymod.api.util.concurrent.task.Task;
import net.labymod.api.util.version.SemanticVersion;

@AddonMain
public class GlobalTagsAddon extends LabyAddon<GlobalTagsConfig> {

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
        Laby.references().revisionRegistry().register(new SimpleRevision("globaltags", new SemanticVersion("1.3.5"), "2024-12-15"));
        Laby.references().revisionRegistry()
            .register(new SimpleRevision("globaltags", new SemanticVersion("1.4.0"), "2024-03-01"));
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

        this.registerCommand(new GlobalTagCommand(this));
        this.registerListener(new BroadcastListener(api));
        this.registerListener(new LabyConnectDisconnectListener(api));
        this.registerListener(new ServerNavigationListener());
        this.registerListener(new WorldEnterListener());
        this.labyAPI().interactionMenuRegistry().register(new ChangeTagBulletPoint(this));
        this.labyAPI().interactionMenuRegistry().register(new ClearTagBulletPoint(this));
        this.labyAPI().interactionMenuRegistry().register(new EditBanInfoBulletPoint(this));
        this.labyAPI().interactionMenuRegistry().register(new ReferPlayerBulletPoint(this));
        this.labyAPI().interactionMenuRegistry().register(new ReportBulletPoint(this));
        this.labyAPI().interactionMenuRegistry().register(new StaffNotesBulletPoint(this));
        this.labyAPI().interactionMenuRegistry().register(new TagHistoryBulletPoint(this));
        this.labyAPI().interactionMenuRegistry().register(new ToggleBanBulletPoint(this));
        this.labyAPI().interactionMenuRegistry().register(new ToggleHideTagBulletPoint(this));

        TagRegistry tagRegistry = this.labyAPI().tagRegistry();
        for (PositionType positionType : PositionType.values()) {
            tagRegistry.registerAfter(
                "labymod_role",
                "globaltags_tag",
                positionType,
                new GlobalTagNameTag(this, positionType)
            );
        }

        Task.builder(() -> api.getApiHandler().getReferralLeaderboards(response -> {
            if (!response.isSuccessful()) {
                return;
            }
            ReferralLeaderboardActivity.setLeaderboards(response.getData());
        })).repeat(5, TimeUnit.MINUTES).build().execute();
    }

    @Override
    protected Class<? extends GlobalTagsConfig> configurationClass() {
        return GlobalTagsConfig.class;
    }

    public static GlobalTagAPI getAPI() {
        return api;
    }
}
