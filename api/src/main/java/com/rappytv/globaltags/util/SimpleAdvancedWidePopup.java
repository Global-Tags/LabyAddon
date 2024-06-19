package com.rappytv.globaltags.util;

import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.size.SizeType;
import net.labymod.api.client.gui.screen.widget.size.WidgetSide;
import net.labymod.api.client.gui.screen.widget.size.WidgetSize;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.popup.SimpleAdvancedPopup;

public class SimpleAdvancedWidePopup extends SimpleAdvancedPopup {

    @Override
    protected void initializeComponents(VerticalListWidget<Widget> container) {
        if (this.title != null) {
            container.addChild(ComponentWidget.component(this.title).addId("popup-title"));
        }

        if (this.description != null) {
            ComponentWidget description = ComponentWidget.component(this.description)
                .addId("popup-description");
            description.setSize(SizeType.MAX, WidgetSide.WIDTH, WidgetSize.fixed(500));
            container.addChild(description);
        }
    }
}
