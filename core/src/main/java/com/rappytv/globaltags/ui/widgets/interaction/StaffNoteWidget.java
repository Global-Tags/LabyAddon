package com.rappytv.globaltags.ui.widgets.interaction;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.wrapper.model.PlayerNote;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.util.I18n;

@AutoWidget
public class StaffNoteWidget extends SimpleWidget {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final UUID holder;
    private final GlobalTagAPI api;
    private final PlayerNote note;
    private ButtonWidget deleteButton;

    public StaffNoteWidget(UUID holder, GlobalTagAPI api, PlayerNote note) {
        this.holder = holder;
        this.api = api;
        this.note = note;
    }

    @Override
    public void initialize(Parent parent) {
        if(this.isInitialized()) return;
        super.initialize(parent);

        IconWidget headWidget = new IconWidget(Icon.head(this.note.getAuthor()))
            .addId("author-head");

        ComponentWidget text = ComponentWidget
            .text(this.note.getText())
            .addId("text-component");
        text.setHoverComponent(Component.text(this.note.getText()));

        ComponentWidget description = ComponentWidget
            .text(
                I18n.translate(
                    "globaltags.context.staff_notes.description",
                    this.formatDate(this.note.getCreatedAt()),
                    this.note.getId()
                ),
                NamedTextColor.DARK_GRAY
            )
            .addId("description-component");

        this.deleteButton = ButtonWidget
            .component(Component.text("✗", NamedTextColor.RED), this::delete)
            .addId("delete-button");
        this.deleteButton.setHoverComponent(Component.translatable(
            "globaltags.context.staff_notes.hover.delete",
            NamedTextColor.RED
        ));

        this.addChild(headWidget);
        this.addChild(text);
        this.addChild(description);
        this.addChild(this.deleteButton);
    }

    private String formatDate(Date date) {
        return dateFormat.format(date);
    }

    private void delete() {
        this.deleteButton.setEnabled(false);
        this.api.getApiHandler().deleteNote(this.holder, this.note.getId(), (response) -> {
            Laby.references().chatExecutor().displayClientMessage(
                TextComponent.builder()
                    .append(GlobalTagAddon.prefix)
                    .append(Util.getResponseComponent(response))
                    .build()
            );
            if(!response.isSuccessful()) {
                this.deleteButton.setEnabled(true);
                return;
            }
            Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
                this.deleteButton.updateComponent(Component.text("✓", NamedTextColor.GREEN));
                this.deleteButton.setHoverComponent(Component.translatable(
                    "globaltags.context.staff_notes.hover.deleted",
                    NamedTextColor.GREEN
                ));
            });
        });
    }
}
