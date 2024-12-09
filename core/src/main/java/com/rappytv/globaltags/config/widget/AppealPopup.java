package com.rappytv.globaltags.config.widget;

import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.api.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.popup.AdvancedPopup;
import org.jetbrains.annotations.Nullable;

public class AppealPopup extends AdvancedPopup {

    private final GlobalTagAPI api;

    public AppealPopup(GlobalTagAPI api) {
        this.api = api;
    }

    @Override
    public @Nullable Widget initialize() {
        VerticalListWidget<Widget> content = new VerticalListWidget<>().addId("content");
        ComponentWidget labelWidget = ComponentWidget.i18n(
            "globaltags.settings.tags.tagPreview.appeal.popup.label"
        ).addId("label", "popup-description");
        TextFieldWidget inputWidget = new TextFieldWidget()
            .placeholder(Component.translatable(
                "globaltags.settings.tags.tagPreview.appeal.popup.placeholder",
                NamedTextColor.DARK_GRAY
            ))
            .addId("input", "popup-description");
        ButtonWidget sendButton = new ButtonWidget()
            .updateComponent(Component.translatable(
                "globaltags.settings.tags.tagPreview.appeal.popup.button",
                NamedTextColor.RED
            ))
            .addId("send-button", "popup-description");
        sendButton.setEnabled(false);
        sendButton.setActionListener(() -> {
            this.api.getApiHandler().appealBan(inputWidget.getText(), (response) ->
                Util.notify(
                    Component.translatable(response.isSuccessful()
                        ? "globaltags.general.success"
                        : "globaltags.general.error"
                    ),
                    Component.text(response.getData(), NamedTextColor.WHITE)
                )
            );
            this.close();
        });
        inputWidget.updateListener((text) -> sendButton.setEnabled(!text.isBlank()));

        content.addChild(labelWidget);
        content.addChild(inputWidget);
        content.addChild(sendButton);

        return content;
    }
}
