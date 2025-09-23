package com.rappytv.globaltags.core;

import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.core.command.GlobalTagCommand;
import com.rappytv.globaltags.core.config.GlobalTagsConfig;
import com.rappytv.globaltags.core.interaction.ChangeTagBulletPoint;
import com.rappytv.globaltags.core.interaction.ClearTagBulletPoint;
import com.rappytv.globaltags.core.interaction.EditBanInfoBulletPoint;
import com.rappytv.globaltags.core.interaction.ReferPlayerBulletPoint;
import com.rappytv.globaltags.core.interaction.ReportBulletPoint;
import com.rappytv.globaltags.core.interaction.StaffNotesBulletPoint;
import com.rappytv.globaltags.core.interaction.TagHistoryBulletPoint;
import com.rappytv.globaltags.core.interaction.ToggleBanBulletPoint;
import com.rappytv.globaltags.core.interaction.ToggleHideTagBulletPoint;
import com.rappytv.globaltags.core.listeners.BroadcastListener;
import com.rappytv.globaltags.core.listeners.LabyConnectDisconnectListener;
import com.rappytv.globaltags.core.listeners.ServerNavigationListener;
import com.rappytv.globaltags.core.listeners.WorldEnterListener;
import com.rappytv.globaltags.core.ui.activities.config.ReferralLeaderboardActivity;
import com.rappytv.globaltags.core.ui.nametag.GlobalTagNameTag;
import com.rappytv.globaltags.wrapper.GlobalTagsAPI.Agent;
import java.util.concurrent.TimeUnit;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.entity.player.tag.TagRegistry;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.revision.SimpleRevision;
import net.labymod.api.util.concurrent.task.Task;
import net.labymod.api.util.version.SemanticVersion;

@AddonMain
public class GlobalTagsAddon extends LabyAddon<GlobalTagsConfig> {

    private static final Component prefix = Component.empty()
        .append(Component.text("GlobalTags").color(NamedTextColor.BLUE).decorate(TextDecoration.BOLD))
        .append(Component.text(" » ", NamedTextColor.DARK_GRAY));

    private static GlobalTagAPI api;
    private static final String[][] versions = {
        {"1.1.0", "2023-11-24"},
        {"1.1.7", "2024-02-27"},
        {"1.1.9", "2024-06-01"},
        {"1.2.0", "2024-07-14"},
        {"1.3.5", "2024-12-15"},
        {"1.4.0", "2025-03-01"},
        {"1.4.1", "2025-04-10"},
        {"1.4.3", "2025-05-20"},
    };

    @Override
    protected void preConfigurationLoad() {
        for (String[] version : versions) {
            Laby.references().revisionRegistry().register(new SimpleRevision(
                "globaltags",
                new SemanticVersion(version[0]),
                version[1]
            ));
        }
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

    public static Component prefix() {
        return prefix.copy();
    }

    public static GlobalTagAPI getAPI() {
        return api;
    }
}
