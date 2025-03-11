package com.rappytv.globaltags.ui.activities.config;

import com.rappytv.globaltags.config.GlobalTagsConfig;
import com.rappytv.globaltags.ui.widgets.config.HiddenPlayerWidget;
import java.util.UUID;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;

@Link("player-list.lss")
@AutoActivity
public class HiddenTagListActivity extends SimpleActivity {

    private final VerticalListWidget<HiddenPlayerWidget> entries = new VerticalListWidget<>().addId(
        "player-entries");
    private ScrollWidget scrollWidget;
    private ComponentWidget errorComponent;

    public HiddenTagListActivity(GlobalTagsConfig config) {
        for (UUID uuid : config.account().hiddenTags()) {
            this.entries.addChild(new HiddenPlayerWidget(
                uuid,
                config,
                (widget) -> {
                    this.entries.removeChild(widget);
                    this.setVisibility();
                }
            ));
        }
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);

        FlexibleContentWidget container = new FlexibleContentWidget().addId("container");

        this.scrollWidget = new ScrollWidget(this.entries);
        this.errorComponent = ComponentWidget.i18n(
            "globaltags.settings.hiddenTagList.empty",
            NamedTextColor.RED
        ).addId("error-component");
        container.addContent(this.scrollWidget);
        container.addContent(this.errorComponent);
        this.setVisibility();
        this.document.addChild(container);
    }

    public void setVisibility() {
        boolean isEmpty = this.entries.getChildren().isEmpty();
        this.scrollWidget.setVisible(!isEmpty);
        this.errorComponent.setVisible(isEmpty);
    }
}
