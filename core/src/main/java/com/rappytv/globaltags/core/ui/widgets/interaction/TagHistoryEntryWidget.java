package com.rappytv.globaltags.core.ui.widgets.interaction;

import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.wrapper.model.TagHistoryEntry;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;

@AutoWidget
public class TagHistoryEntryWidget extends SimpleWidget {

    private final int number;
    private final GlobalTagAPI api;
    private final TagHistoryEntry entry;

    public TagHistoryEntryWidget(int number, GlobalTagAPI api, TagHistoryEntry entry) {
        this.number = number;
        this.api = api;
        this.entry = entry;
    }

    @Override
    public void initialize(Parent parent) {
        if(this.isInitialized()) return;
        super.initialize(parent);

        ComponentWidget text = ComponentWidget
            .component(
                Component
                    .empty()
                    .append(Component.text(this.number + ". ", NamedTextColor.AQUA))
                    .append(this.api.translateColorCodes(this.entry.getTag()))
            )
            .addId("text-component");

        this.addChild(text);
        if(!this.entry.getFlaggedWords().isEmpty()) {
            Component hoverComponent = Component.translatable(
                "globaltags.context.tagHistory.flaggedWords",
                NamedTextColor.GOLD
            );
            for(String word : this.entry.getFlaggedWords()) {
                hoverComponent
                    .append(Component.newline())
                    .append(Component.text(
                        "- " + word,
                        NamedTextColor.GOLD
                    ));
            }
            ComponentWidget flaggedWords = ComponentWidget
                .text("⚠", NamedTextColor.GOLD)
                .addId("flagged-words");
            flaggedWords.setHoverComponent(hoverComponent);
            this.addChild(flaggedWords);
        }
    }
}
