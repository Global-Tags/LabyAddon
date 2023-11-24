package com.rappytv.globaltags.activities;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import java.util.UUID;

@Link("report.lss")
@AutoActivity
public class ReportUUIDActivity extends SimpleActivity {

    private final UUID uuid;
    private final String username;

    public ReportUUIDActivity(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        FlexibleContentWidget windowWidget = new FlexibleContentWidget().addId("window");
        HorizontalListWidget outerWrapper = new HorizontalListWidget().addId("outer-header");
        HorizontalListWidget profileWrapper = new HorizontalListWidget().addId("header");
        IconWidget headWidget = new IconWidget(Icon.head(this.uuid)).addId("head");
        ComponentWidget titleWidget = ComponentWidget.i18n("globaltags.report.title", this.username).addId("username");
        ComponentWidget reasonWidget = ComponentWidget.i18n("globaltags.report.reason").addId("reason");
        TextFieldWidget textField = new TextFieldWidget()
            .placeholder(Component.translatable("globaltags.report.placeholder", NamedTextColor.DARK_GRAY))
            .addId("text-field");
        ButtonWidget button = new ButtonWidget()
            .updateComponent(Component.translatable("globaltags.report.send", NamedTextColor.RED))
            .addId("report-button");
        button.setEnabled(false);
        button.setActionListener(() -> Laby.references().chatExecutor().displayClientMessage("§cReported Player"));
        textField.updateListener((text) -> button.setEnabled(!text.isBlank()));

        profileWrapper.addEntry(headWidget);
        profileWrapper.addEntry(titleWidget);

        outerWrapper.addEntry(new DivWidget());
        outerWrapper.addEntry(profileWrapper);
        outerWrapper.addEntry(new DivWidget());

        windowWidget.addContent(outerWrapper);
        windowWidget.addContent(reasonWidget);
        windowWidget.addContent(textField);
        windowWidget.addContent(button);
        this.document.addChild(windowWidget);
    }
}
