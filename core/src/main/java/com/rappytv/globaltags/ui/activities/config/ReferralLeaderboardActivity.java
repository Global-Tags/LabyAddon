package com.rappytv.globaltags.ui.activities.config;

import com.rappytv.globaltags.GlobalTagsAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.ui.widgets.config.ReferralLeaderboardEntryWidget;
import com.rappytv.globaltags.wrapper.enums.ReferralLeaderboardType;
import com.rappytv.globaltags.wrapper.model.ReferralLeaderboardEntry;
import java.util.List;
import java.util.Map;
import net.labymod.api.Laby;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.util.ThreadSafe;

@Link("player-list.lss")
@AutoActivity
public class ReferralLeaderboardActivity extends SimpleActivity {

    private static Map<ReferralLeaderboardType, List<ReferralLeaderboardEntry>> leaderboards = null;
    private final GlobalTagAPI api;
    private final VerticalListWidget<ReferralLeaderboardEntryWidget> entries = new VerticalListWidget<>().addId(
        "player-entries");
    private final HorizontalListWidget buttonMenu;
    private ReferralLeaderboardType selectedLeaderboard = ReferralLeaderboardType.TOTAL;

    public ReferralLeaderboardActivity() {
        this.api = GlobalTagsAddon.getAPI();
        this.buttonMenu = new HorizontalListWidget().addId("button-menu");
        ButtonWidget totalViewButton = ButtonWidget.i18n(
            "globaltags.settings.referralLeaderboards.activity.total");
        ButtonWidget currentMonthViewButton = ButtonWidget.i18n(
            "globaltags.settings.referralLeaderboards.activity.current_month");
        totalViewButton.setEnabled(this.selectedLeaderboard != ReferralLeaderboardType.TOTAL);
        totalViewButton.setPressable(() -> {
            this.selectedLeaderboard = ReferralLeaderboardType.TOTAL;
            totalViewButton.setEnabled(false);
            currentMonthViewButton.setEnabled(true);
            this.initializeWithInfo(true);
        });
        currentMonthViewButton.setEnabled(
            this.selectedLeaderboard != ReferralLeaderboardType.CURRENT_MONTH);
        currentMonthViewButton.setPressable(() -> {
            this.selectedLeaderboard = ReferralLeaderboardType.CURRENT_MONTH;
            totalViewButton.setEnabled(true);
            currentMonthViewButton.setEnabled(false);
            this.initializeWithInfo(true);
        });

        this.buttonMenu.addEntry(totalViewButton);
        this.buttonMenu.addEntry(currentMonthViewButton);
    }

    public static void setLeaderboards(
        Map<ReferralLeaderboardType, List<ReferralLeaderboardEntry>> leaderboards) {
        ReferralLeaderboardActivity.leaderboards = leaderboards;
    }

    private void initializeWithInfo() {
        if (ThreadSafe.isRenderThread()) {
            this.initializeWithInfo(false);
        } else {
            Laby.labyAPI().minecraft().executeOnRenderThread(
                () -> this.initializeWithInfo(true)
            );
        }
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);

        FlexibleContentWidget container = new FlexibleContentWidget().addId("container");

        container.addContent(this.buttonMenu);
        ScrollWidget scroll = new ScrollWidget(this.entries);
        container.addContent(scroll);
        this.document.addChild(container);

        if (ReferralLeaderboardActivity.leaderboards == null) {
            this.api.getApiHandler().getReferralLeaderboards(response -> {
                if (!response.isSuccessful()) {
                    Laby.labyAPI().minecraft().executeOnRenderThread(
                        () -> container.addContentInitialized(
                            ComponentWidget.text(response.getError(), NamedTextColor.RED)
                                .addId("error-component")
                        )
                    );
                    scroll.setVisible(false);
                    return;
                }
                ReferralLeaderboardActivity.leaderboards = response.getData();
                this.initializeWithInfo();
            });
        } else {
            this.initializeWithInfo();
        }
    }

    private void initializeWithInfo(boolean initialized) {
        this.entries.getChildren().clear();
        if (leaderboards == null) {
            return;
        }
        for (ReferralLeaderboardEntry entry : leaderboards.get(this.selectedLeaderboard)) {
            if (initialized) {
                this.entries.addChildInitialized(new ReferralLeaderboardEntryWidget(entry));
            } else {
                this.entries.addChild(new ReferralLeaderboardEntryWidget(entry));
            }
        }
    }
}
