package com.rappytv.globaltags.activities;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.ApiHandler;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;

import java.util.UUID;

@Link("input.lss")
@AutoActivity
public class BanActivity extends SimpleActivity {

    private final UUID uuid;
    private final String username;

    public BanActivity(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        FlexibleContentWidget windowWidget = new FlexibleContentWidget().addId("window");
        HorizontalListWidget profileWrapper = new HorizontalListWidget().addId("header");
        IconWidget headWidget = new IconWidget(Icon.head(this.uuid)).addId("head");
        ComponentWidget titleWidget = ComponentWidget.i18n("globaltags.context.ban.title", this.username).addId("username");
        VerticalListWidget<Widget> content = new VerticalListWidget<>().addId("content");
        ComponentWidget labelWidget = ComponentWidget.i18n("globaltags.context.reason").addId("label");
        TextFieldWidget inputWidget = new TextFieldWidget()
            .placeholder(Component.translatable("globaltags.context.placeholder", NamedTextColor.DARK_GRAY))
            .addId("input");
        ButtonWidget sendButton = new ButtonWidget()
            .updateComponent(Component.translatable("globaltags.context.ban.send", NamedTextColor.RED))
            .addId("send-button");
        sendButton.setEnabled(false);
        sendButton.setActionListener(() -> {
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen((ScreenInstance) null);
            ApiHandler.banPlayer(uuid, inputWidget.getText(), (response) -> Laby.references().chatExecutor().displayClientMessage(
                Component.empty()
                    .append(GlobalTagAddon.prefix)
                    .append(response.getMessage())
            ));
        });
        inputWidget.updateListener((text) -> sendButton.setEnabled(!text.isBlank()));

        profileWrapper.addEntry(headWidget);
        profileWrapper.addEntry(titleWidget);

        content.addChild(labelWidget);
        content.addChild(inputWidget);
        content.addChild(sendButton);

        windowWidget.addContent(profileWrapper);
        windowWidget.addContent(content);
        this.document.addChild(windowWidget);
    }
}
