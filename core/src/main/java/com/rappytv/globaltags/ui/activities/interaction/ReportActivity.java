package com.rappytv.globaltags.ui.activities.interaction;

import com.rappytv.globaltags.GlobalTagsAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.config.GlobalTagsConfig;
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
public class ReportActivity extends SimpleActivity {

    private final GlobalTagsConfig config;
    private final GlobalTagAPI api;
    private final UUID uuid;
    private final String username;

    public ReportActivity(GlobalTagsConfig config, UUID uuid, String username) {
        this.config = config;
        this.api = GlobalTagsAddon.getAPI();
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
        HorizontalListWidget ruleCheckBoxWrapper = new HorizontalListWidget().addId(
            "checkbox-wrapper");
        CheckBoxWidget ruleCheckBoxWidget = new CheckBoxWidget().addId("check-box");
        ruleCheckBoxWidget.setState(State.UNCHECKED);
        Component ruleCheckBoxLabel = Component.translatable(
            "globaltags.context.report.checkbox.label",
            NamedTextColor.WHITE,
            Component.translatable("globaltags.context.report.checkbox.rules")
                .color(NamedTextColor.AQUA)
                .hoverEvent(HoverEvent.showText(
                    Component.translatable("globaltags.context.report.checkbox.hover")))
                .clickEvent(ClickEvent.openUrl("https://docs.globaltags.xyz/rules"))
        );
        ComponentWidget ruleCheckBoxLabelWidget = ComponentWidget
            .component(ruleCheckBoxLabel)
            .addId("checkbox-label");

        HorizontalListWidget hideCheckBoxWrapper = new HorizontalListWidget().addId(
            "checkbox-wrapper");
        CheckBoxWidget hideCheckBoxWidget = new CheckBoxWidget().addId("check-box");
        hideCheckBoxWidget.setState(State.CHECKED);
        Component hideCheckBoxLabel = Component.translatable(
            "globaltags.context.report.hide.label",
            NamedTextColor.WHITE
        );
        ComponentWidget hideCheckBoxLabelWidget = ComponentWidget
            .component(hideCheckBoxLabel)
            .addId("checkbox-label");

        ButtonWidget sendButton = new ButtonWidget()
            .updateComponent(Component.translatable("globaltags.context.report.send", NamedTextColor.RED))
            .addId("send-button");
        sendButton.setEnabled(false);
        sendButton.setActionListener(() -> {
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen((ScreenInstance) null);
            if (hideCheckBoxWidget.state() == State.CHECKED) {
                this.config.hiddenTags().add(this.uuid);
            }
            this.api.getApiHandler().reportPlayer(
                this.uuid, inputWidget.getText(), (response) -> Laby.references().chatExecutor().displayClientMessage(
                Component.empty()
                    .append(GlobalTagsAddon.prefix())
                    .append(Util.getResponseComponent(response))
            ));
        });
        Consumer<String> updateButton = (text) -> sendButton.setEnabled(
            !text.isBlank() && ruleCheckBoxWidget.state() == State.CHECKED
        );
        inputWidget.updateListener(updateButton);
        ruleCheckBoxWidget.setActionListener(() ->
            updateButton.accept(inputWidget.getText())
        );

        profileWrapper.addEntry(headWidget);
        profileWrapper.addEntry(titleWidget);

        ruleCheckBoxWrapper.addEntry(ruleCheckBoxWidget);
        ruleCheckBoxWrapper.addEntry(ruleCheckBoxLabelWidget);

        hideCheckBoxWrapper.addEntry(hideCheckBoxWidget);
        hideCheckBoxWrapper.addEntry(hideCheckBoxLabelWidget);

        content.addChild(labelWidget);
        content.addChild(inputWidget);
        content.addChild(ruleCheckBoxWrapper);
        content.addChild(hideCheckBoxWrapper);
        content.addChild(sendButton);

        windowWidget.addContent(profileWrapper);
        windowWidget.addContent(content);
        this.document.addChild(windowWidget);
    }
}
