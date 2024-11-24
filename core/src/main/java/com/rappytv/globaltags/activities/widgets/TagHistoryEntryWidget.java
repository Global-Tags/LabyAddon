package com.rappytv.globaltags.activities.widgets;

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
        if(isInitialized()) return;
        super.initialize(parent);

        ComponentWidget text = ComponentWidget
            .component(
                Component
                    .empty()
                    .append(Component.text(number + ". ", NamedTextColor.AQUA))
                    .append(api.translateColorCodes(entry.tag()))
            )
            .addId("text-component");

        this.addChild(text);
        if(!entry.flaggedWords().isEmpty()) {
            Component hoverComponent = Component.translatable(
                "globaltags.context.tag_history.flagged_words",
                NamedTextColor.GOLD
            );
            for(String word : entry.flaggedWords()) {
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
