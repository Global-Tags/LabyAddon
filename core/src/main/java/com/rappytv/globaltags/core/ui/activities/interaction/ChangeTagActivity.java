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
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;

@Link("input.lss")
@AutoActivity
public class ChangeTagActivity extends SimpleActivity {

    private final GlobalTagAPI api;
    private final UUID uuid;
    private final String username;

    public ChangeTagActivity(UUID uuid, String username) {
        this.api = GlobalTagsAddon.getAPI();
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        this.api.getCache().resolve(this.uuid, (info) -> {
            if (info == null) {
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen((ScreenInstance) null);
                return;
            }
            FlexibleContentWidget windowWidget = new FlexibleContentWidget().addId("window");
            HorizontalListWidget profileWrapper = new HorizontalListWidget().addId("header");
            IconWidget headWidget = new IconWidget(Icon.head(this.uuid)).addId("head");
            ComponentWidget titleWidget = ComponentWidget.i18n("globaltags.context.changeTag.title", this.username).addId("username");
            VerticalListWidget<Widget> content = new VerticalListWidget<>().addId("content");
            ComponentWidget labelWidget = ComponentWidget.i18n("globaltags.context.changeTag.label").addId("label");
            TextFieldWidget inputWidget = new TextFieldWidget()
                .placeholder(Component.translatable("globaltags.context.changeTag.placeholder", NamedTextColor.DARK_GRAY))
                .addId("input");
            boolean hasTag = !info.getPlainTag().isBlank();
            inputWidget.setText(hasTag ? info.getPlainTag() : "");
            ButtonWidget sendButton = new ButtonWidget()
                .updateComponent(Component.translatable("globaltags.context.changeTag.send", NamedTextColor.AQUA))
                .addId("send-button");
            sendButton.setEnabled(!inputWidget.getText().isBlank() && (!hasTag || !inputWidget.getText().equals(info.getPlainTag())));
            sendButton.setActionListener(() -> {
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen((ScreenInstance) null);
                this.api.getApiHandler().setTag(this.uuid, inputWidget.getText(), (response) -> {
                    if(response.isSuccessful()) Util.broadcastTagUpdate(this.uuid);
                    Laby.references().chatExecutor().displayClientMessage(
                        Component.empty()
                            .append(GlobalTagsAddon.prefix())
                            .append(Util.getResponseComponent(response))
                    );
                });
            });
            inputWidget.updateListener((text) -> sendButton.setEnabled(!text.isBlank() && (!hasTag || !inputWidget.getText().equals(info.getPlainTag()))));

            profileWrapper.addEntry(headWidget);
            profileWrapper.addEntry(titleWidget);

            content.addChild(labelWidget);
            content.addChild(inputWidget);
            content.addChild(sendButton);

            windowWidget.addContent(profileWrapper);
            windowWidget.addContent(content);
            this.document.addChild(windowWidget);
        });
    }
}
