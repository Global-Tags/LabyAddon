package com.rappytv.globaltags.activities;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.api.Util;
import java.util.UUID;
import java.util.function.Consumer;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
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
import net.labymod.api.client.gui.screen.widget.widgets.input.CheckBoxWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.CheckBoxWidget.State;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;

@Link("input.lss")
@AutoActivity
public class ReportUUIDActivity extends SimpleActivity {

    private final GlobalTagAPI api;
    private final UUID uuid;
    private final String username;

    public ReportUUIDActivity(GlobalTagAPI api, UUID uuid, String username) {
        this.api = api;
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        FlexibleContentWidget windowWidget = new FlexibleContentWidget().addId("window");
        HorizontalListWidget profileWrapper = new HorizontalListWidget().addId("header");
        IconWidget headWidget = new IconWidget(Icon.head(this.uuid)).addId("head");
        ComponentWidget titleWidget = ComponentWidget.i18n("globaltags.context.report.title", this.username).addId("username");
        VerticalListWidget<Widget> content = new VerticalListWidget<>().addId("content");
        ComponentWidget labelWidget = ComponentWidget.i18n("globaltags.context.reason").addId("label");
        TextFieldWidget inputWidget = new TextFieldWidget()
            .placeholder(Component.translatable("globaltags.context.placeholder", NamedTextColor.DARK_GRAY))
            .addId("input");
        HorizontalListWidget checkboxWrapper = new HorizontalListWidget().addId("checkbox-wrapper");
        CheckBoxWidget checkBoxWidget = new CheckBoxWidget().addId("check-box");
        checkBoxWidget.setState(State.UNCHECKED);
        Component boxLabel = Component.translatable(
            "globaltags.context.report.checkbox.label",
            NamedTextColor.WHITE,
            Component.translatable("globaltags.context.report.checkbox.rules")
                .color(NamedTextColor.AQUA)
                .hoverEvent(HoverEvent.showText(
                    Component.translatable("globaltags.context.report.checkbox.hover")))
                .clickEvent(ClickEvent.openUrl("https://docs.globaltags.xyz/rules"))
        );
        ComponentWidget boxLabelWidget = ComponentWidget
            .component(boxLabel)
            .addId("checkbox-label");
        ButtonWidget sendButton = new ButtonWidget()
            .updateComponent(Component.translatable("globaltags.context.report.send", NamedTextColor.RED))
            .addId("send-button");
        sendButton.setEnabled(false);
        sendButton.setActionListener(() -> {
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen((ScreenInstance) null);
            this.api.getApiHandler().reportPlayer(
                this.uuid, inputWidget.getText(), (response) -> Laby.references().chatExecutor().displayClientMessage(
                Component.empty()
                    .append(GlobalTagAddon.prefix)
                    .append(Util.getResponseComponent(response))
            ));
        });
        Consumer<String> updateButton = (text) -> sendButton.setEnabled(
            !text.isBlank() && checkBoxWidget.state() == State.CHECKED
        );
        inputWidget.updateListener(updateButton);
        checkBoxWidget.setActionListener(() ->
            updateButton.accept(inputWidget.getText())
        );

        profileWrapper.addEntry(headWidget);
        profileWrapper.addEntry(titleWidget);

        checkboxWrapper.addEntry(checkBoxWidget);
        checkboxWrapper.addEntry(boxLabelWidget);

        content.addChild(labelWidget);
        content.addChild(inputWidget);
        content.addChild(checkboxWrapper);
        content.addChild(sendButton);

        windowWidget.addContent(profileWrapper);
        windowWidget.addContent(content);
        this.document.addChild(windowWidget);
    }
}
