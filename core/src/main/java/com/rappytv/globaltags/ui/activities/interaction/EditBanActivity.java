package com.rappytv.globaltags.ui.activities.interaction;

import com.rappytv.globaltags.GlobalTagsAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.wrapper.model.BanInfo;
import java.util.UUID;
import java.util.function.Consumer;
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
public class EditBanActivity extends SimpleActivity {

    private final GlobalTagAPI api;
    private final UUID uuid;
    private final String username;

    public EditBanActivity(UUID uuid, String username) {
        this.api = GlobalTagsAddon.getAPI();
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        this.api.getCache().resolve(this.uuid, (info) -> {
            if (info == null || info.getBanInfo() == null) {
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen((ScreenInstance) null);
                return;
            }
            BanInfo banInfo = info.getBanInfo();
            FlexibleContentWidget windowWidget = new FlexibleContentWidget().addId("window");
            HorizontalListWidget profileWrapper = new HorizontalListWidget().addId("header");
            IconWidget headWidget = new IconWidget(Icon.head(this.uuid)).addId("head");
            ComponentWidget titleWidget = ComponentWidget.i18n(
                "globaltags.context.editBan.title",
                this.username
            ).addId("username");
            VerticalListWidget<Widget> content = new VerticalListWidget<>().addId("content");
            ComponentWidget inputLabelWidget = ComponentWidget.i18n("globaltags.context.reason")
                .addId("label");
            TextFieldWidget inputWidget = new TextFieldWidget()
                .placeholder(Component.translatable(
                    "globaltags.context.placeholder",
                    NamedTextColor.DARK_GRAY
                ))
                .addId("input");
            String reason = banInfo.getReason();
            inputWidget.setText(reason, true);
            HorizontalListWidget checkboxWrapper = new HorizontalListWidget().addId(
                "checkbox-wrapper");
            CheckBoxWidget checkBoxWidget = new CheckBoxWidget().addId("check-box");
            checkBoxWidget.setState(banInfo.isAppealable() ? State.CHECKED : State.UNCHECKED);
            ComponentWidget boxLabelWidget = ComponentWidget
                .i18n("globaltags.context.editBan.appealable")
                .addId("checkbox-label");
            ButtonWidget sendButton = new ButtonWidget()
                .updateComponent(Component.translatable(
                    "globaltags.context.editBan.send",
                    NamedTextColor.AQUA
                ))
                .addId("send-button");
            sendButton.setEnabled(false);
            sendButton.setActionListener(() -> {
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen((ScreenInstance) null);
                this.api.getApiHandler().editBan(
                    this.uuid,
                    reason,
                    checkBoxWidget.state() == State.CHECKED,
                    (response) ->
                        Laby.references().chatExecutor().displayClientMessage(
                            Component.empty()
                                .append(GlobalTagsAddon.prefix)
                                .append(Util.getResponseComponent(response))
                        )
                );
            });
            Consumer<String> updateButton = (text) -> {
                boolean reasonNotEmpty = !text.isBlank();
                boolean updatedReason = !text.equals(banInfo.getReason());
                boolean updatedCheckbox =
                    (checkBoxWidget.state() == State.CHECKED) != banInfo.isAppealable();
                sendButton.setEnabled(reasonNotEmpty && (updatedReason || updatedCheckbox));
            };
            inputWidget.updateListener(updateButton);
            checkBoxWidget.setActionListener(() ->
                updateButton.accept(inputWidget.getText())
            );

            profileWrapper.addEntry(headWidget);
            profileWrapper.addEntry(titleWidget);

            checkboxWrapper.addEntry(checkBoxWidget);
            checkboxWrapper.addEntry(boxLabelWidget);

            content.addChild(inputLabelWidget);
            content.addChild(inputWidget);
            content.addChild(checkboxWrapper);
            content.addChild(sendButton);

            windowWidget.addContent(profileWrapper);
            windowWidget.addContent(content);
            this.document.addChild(windowWidget);
        });
    }
}
