package com.rappytv.globaltags.core.ui.widgets.config;

import com.rappytv.globaltags.wrapper.model.ReferralLeaderboardEntry;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.util.ThreadSafe;

@AutoWidget
public class ReferralLeaderboardEntryWidget extends HorizontalListWidget {

    private final ReferralLeaderboardEntry entry;

    public ReferralLeaderboardEntryWidget(ReferralLeaderboardEntry entry) {
        this.entry = entry;
    }

    @Override
    public void initialize(Parent parent) {
        if (this.isInitialized()) {
            return;
        }
        super.initialize(parent);

        IconWidget headWidget = new IconWidget(Icon.head(this.entry.getUUID()))
            .addId("player-head");

        ComponentWidget text = ComponentWidget
            .component(this.getUsernameComponent(true, null))
            .addId("username-component");

        ComponentWidget value = ComponentWidget
            .component(
                Component
                    .empty()
                    .append(Component.translatable(
                        "globaltags.settings.referralLeaderboards.activity.total"))
                    .append(Component.text(": ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(this.entry.getTotalReferrals(), NamedTextColor.AQUA))
                    .append(Component.newline())
                    .append(Component.translatable(
                        "globaltags.settings.referralLeaderboards.activity.currentMonth"))
                    .append(Component.text(": ", NamedTextColor.DARK_GRAY))
                    .append(
                        Component.text(this.entry.getCurrentMonthReferrals(), NamedTextColor.AQUA))
            )
            .addId("referral-count-component");

        this.addEntry(headWidget);
        this.addEntry(text);
        this.addEntry(value);

        Laby.references()
            .labyNetController()
            .loadNameByUniqueId(this.entry.getUUID(), (name) -> {
                if (ThreadSafe.isRenderThread()) {
                    text.setComponent(this.getUsernameComponent(false, name.getNullable()));
                    text.addId("loaded");
                } else {
                    Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
                        text.setComponent(this.getUsernameComponent(false, name.getNullable()));
                        text.addId("loaded");
                    });
                }
            });
    }

    private Component getUsernameComponent(boolean loading, String username) {
        Component component = Component
            .empty()
            .append(Component.text(this.entry.getRank() + ". ", NamedTextColor.AQUA))
            .clickEvent(ClickEvent.openUrl("https://laby.net/@" + this.entry.getUUID()));

        if (loading) {
            component.append(Component.translatable(
                "globaltags.settings.referralLeaderboards.activity.loading",
                NamedTextColor.GRAY
            ));
        } else {
            if (username == null) {
                component.append(Component.translatable(
                    "globaltags.settings.referralLeaderboards.activity.open"
                ));
            } else {
                component.append(Component.text(username));
            }
        }

        return component;
    }
}
