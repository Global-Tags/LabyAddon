package com.rappytv.globaltags.ui.widgets.config;

import com.rappytv.globaltags.config.GlobalTagsConfig;
import java.util.UUID;
import java.util.function.Consumer;
import net.labymod.api.Laby;
import net.labymod.api.Textures;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.util.ThreadSafe;

@AutoWidget
public class HiddenPlayerWidget extends HorizontalListWidget {

    private final UUID uuid;
    private final GlobalTagsConfig config;
    private final Consumer<HiddenPlayerWidget> onDelete;

    public HiddenPlayerWidget(UUID uuid, GlobalTagsConfig config,
        Consumer<HiddenPlayerWidget> onDelete) {
        this.uuid = uuid;
        this.config = config;
        this.onDelete = onDelete;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);

        IconWidget headWidget = new IconWidget(Icon.head(this.uuid))
            .addId("player-head");

        ComponentWidget usernameWidget = ComponentWidget
            .component(this.getUsernameComponent(true, null))
            .addId("username-component");

        ButtonWidget removeButton = ButtonWidget.icon(Textures.SpriteCommon.X, () -> {
            this.config.tags().hiddenTags().remove(this.uuid);
            this.onDelete.accept(this);
        }).addId("remove-button");
        removeButton.setHoverComponent(Component.translatable(
            "globaltags.settings.hiddenTagList.remove",
            NamedTextColor.GREEN
        ));

        this.addEntry(headWidget);
        this.addEntry(usernameWidget);
        this.addEntry(removeButton);

        Laby.references()
            .labyNetController()
            .loadNameByUniqueId(this.uuid, (name) -> {
                if (ThreadSafe.isRenderThread()) {
                    usernameWidget.setComponent(
                        this.getUsernameComponent(false, name.getNullable()));
                    usernameWidget.addId("loaded");
                } else {
                    Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
                        usernameWidget.setComponent(
                            this.getUsernameComponent(false, name.getNullable()));
                        usernameWidget.addId("loaded");
                    });
                }
            });
    }

    private Component getUsernameComponent(boolean loading, String username) {
        Component component = Component
            .empty()
            .clickEvent(ClickEvent.openUrl("https://laby.net/@" + this.uuid));

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
