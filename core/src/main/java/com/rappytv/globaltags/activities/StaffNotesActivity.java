package com.rappytv.globaltags.activities;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.activities.widgets.StaffNoteWidget;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.wrapper.model.PlayerNote;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import java.util.UUID;

@Link("list.lss")
@AutoActivity
public class StaffNotesActivity extends SimpleActivity {

    private final GlobalTagAPI api;
    private final UUID uuid;
    private final String username;

    public StaffNotesActivity(GlobalTagAPI api, UUID uuid, String username) {
        this.api = api;
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        api.getApiHandler().getNotes(uuid, (response) -> Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
            if(document.getChild("window") != null) return;
            if(!response.successful()) {
                Laby.references().chatExecutor().displayClientMessage(
                    TextComponent.builder()
                        .append(GlobalTagAddon.prefix)
                        .append(Component.text(response.error(), NamedTextColor.RED))
                        .build()
                );
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen((ScreenInstance) null);
                return;
            }
            FlexibleContentWidget windowWidget = new FlexibleContentWidget().addId("window");
            HorizontalListWidget profileWrapper = new HorizontalListWidget().addId("header");
            ButtonWidget createButton = ButtonWidget
                .text("+", () -> Laby.labyAPI().minecraft().minecraftWindow().displayScreen(
                    new CreateNoteActivity(api, uuid, username)
                ))
                .addId("create-button");
            createButton.setHoverComponent(Component.translatable("globaltags.context.staff_notes.hover.create"));
            IconWidget headWidget = new IconWidget(Icon.head(this.uuid)).addId("head");
            ComponentWidget titleWidget = ComponentWidget.i18n("globaltags.context.staff_notes.title", this.username).addId("username");
            VerticalListWidget<StaffNoteWidget> notes = new VerticalListWidget<>().addId("item-list");
            for (PlayerNote note : response.data()) {
                notes.addChild(new StaffNoteWidget(uuid, api, note));
            }

            profileWrapper.addEntryInitialized(headWidget);
            profileWrapper.addEntryInitialized(titleWidget);
            profileWrapper.addEntryInitialized(createButton);

            windowWidget.addContentInitialized(profileWrapper);
            windowWidget.addContentInitialized(new ScrollWidget(notes));
            this.document.addChildInitialized(windowWidget);
        }));
    }
}
