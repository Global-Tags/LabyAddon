package com.rappytv.globaltags.activities.widgets;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.wrapper.model.PlayerNote;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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
        if(isInitialized()) return;
        super.initialize(parent);

        IconWidget headWidget = new IconWidget(Icon.head(note.getAuthor()))
            .addId("author-head");

        ComponentWidget text = ComponentWidget
            .text(note.getText())
            .addId("text-component");
        text.setHoverComponent(Component.text(note.getText()));

        ComponentWidget description = ComponentWidget
            .text(
                I18n.translate(
                    "globaltags.context.staff_notes.description",
                    formatDate(note.getCreatedAt()),
                    note.getId()
                ),
                NamedTextColor.DARK_GRAY
            )
            .addId("description-component");

        deleteButton = ButtonWidget
            .component(Component.text("✗", NamedTextColor.RED), this::delete)
            .addId("delete-button");
        deleteButton.setHoverComponent(Component.translatable(
            "globaltags.context.staff_notes.hover.delete",
            NamedTextColor.RED
        ));

        this.addChild(headWidget);
        this.addChild(text);
        this.addChild(description);
        this.addChild(deleteButton);
    }

    private String formatDate(Date date) {
        return dateFormat.format(date);
    }

    private void delete() {
        deleteButton.setEnabled(false);
        api.getApiHandler().deleteNote(holder, note.getId(), (response) -> {
            if(!response.successful()) {
                Laby.references().chatExecutor().displayClientMessage(
                    TextComponent.builder()
                        .append(GlobalTagAddon.prefix)
                        .append(Component.text(response.error(), NamedTextColor.RED))
                        .build()
                );
                deleteButton.setEnabled(true);
                return;
            }
            Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
                deleteButton.updateComponent(Component.text("✓", NamedTextColor.GREEN));
                deleteButton.setHoverComponent(Component.translatable(
                    "globaltags.context.staff_notes.hover.deleted",
                    NamedTextColor.GREEN
                ));
            });
        });
    }
}
