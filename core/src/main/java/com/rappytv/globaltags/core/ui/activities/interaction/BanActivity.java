package com.rappytv.globaltags.core.ui.activities.interaction;

import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.core.GlobalTagsAddon;
import java.util.UUID;
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
import net.labymod.api.client.gui.screen.widget.widgets.input.CheckBoxWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.CheckBoxWidget.State;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;

@Link("input.lss")
@AutoActivity
public class BanActivity extends SimpleActivity {

    private final GlobalTagAPI api;
    private final UUID uuid;
    private final String username;

    public BanActivity(UUID uuid, String username) {
        this.api = GlobalTagsAddon.getAPI();
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        FlexibleContentWidget windowWidget = new FlexibleContentWidget().addId("window");
        HorizontalListWidget profileWrapper = new HorizontalListWidget().addId("header",
            "ban-header");
        IconWidget headIcon = new IconWidget(Icon.head(this.uuid)).addId("head");
        ComponentWidget titleComponent = ComponentWidget.i18n("globaltags.context.ban.title",
            this.username).addId("username");
        VerticalListWidget<Widget> content = new VerticalListWidget<>().addId("content");
        ComponentWidget reasonLabelComponent = ComponentWidget.i18n("globaltags.context.reason")
            .addId("label");
        TextFieldWidget reasonInput = new TextFieldWidget()
            .placeholder(Component.translatable("globaltags.context.placeholder", NamedTextColor.DARK_GRAY))
            .addId("input");
        ComponentWidget durationLabelComponent = ComponentWidget.i18n(
            "globaltags.context.ban.duration.title").addId("label");
        TextFieldWidget durationInput = new TextFieldWidget()
            .placeholder(Component.translatable("globaltags.context.ban.duration.placeholder",
                NamedTextColor.DARK_GRAY))
            .addId("input");
        HorizontalListWidget checkboxWrapper = new HorizontalListWidget().addId(
            "checkbox-wrapper");
        CheckBoxWidget appealableCheckbox = new CheckBoxWidget().addId("check-box");
        appealableCheckbox.setState(State.CHECKED);
        ComponentWidget checkboxLabel = ComponentWidget
            .i18n("globaltags.context.editBan.appealable")
            .addId("checkbox-label");
        ButtonWidget sendButton = new ButtonWidget()
            .updateComponent(Component.translatable("globaltags.context.ban.send", NamedTextColor.RED))
            .addId("send-button");
        sendButton.setEnabled(false);
        sendButton.setActionListener(() -> {
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen((ScreenInstance) null);
            long duration = this.getDuration(durationInput.getText());
            this.api.getApiHandler().banPlayer(
                this.uuid,
                reasonInput.getText(),
                appealableCheckbox.state() == State.CHECKED,
                duration != -1 ? duration : null,
                (response) -> {
                if(response.isSuccessful()) Util.broadcastTagUpdate(this.uuid);
                Laby.references().chatExecutor().displayClientMessage(
                    Component.empty()
                        .append(GlobalTagsAddon.prefix())
                        .append(Util.getResponseComponent(response))
                );
            });
        });
        Runnable updateButtonVisibility = () -> sendButton.setEnabled(
            !reasonInput.getText().isBlank() && (durationInput.getText().isBlank()
                || this.getDuration(durationInput.getText()) != -1)
        );
        reasonInput.updateListener((text) -> updateButtonVisibility.run());
        durationInput.updateListener((text) -> updateButtonVisibility.run());

        profileWrapper.addEntry(headIcon);
        profileWrapper.addEntry(titleComponent);

        checkboxWrapper.addEntry(appealableCheckbox);
        checkboxWrapper.addEntry(checkboxLabel);

        content.addChild(reasonLabelComponent);
        content.addChild(reasonInput);
        content.addChild(durationLabelComponent);
        content.addChild(durationInput);
        content.addChild(checkboxWrapper);
        content.addChild(sendButton);

        windowWidget.addContent(profileWrapper);
        windowWidget.addContent(content);
        this.document.addChild(windowWidget);
    }

    private long getDuration(String timeArg) {
        String format;
        long duration;
        try {
            format = timeArg.substring(timeArg.length() - 1);
            duration = Integer.parseInt(timeArg.substring(0, timeArg.length() - 1));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return -1;
        }

        return switch (format) {
            case "s" -> duration * 1000;
            case "m" -> duration * 1000 * 60;
            case "h" -> duration * 1000 * 60 * 60;
            case "d" -> duration * 1000 * 60 * 60 * 24;
            case "w" -> duration * 1000 * 60 * 60 * 24 * 7;
            case "y" -> duration * 1000 * 60 * 60 * 24 * 7 * 52;
            default -> -1;
        };
    }
}
