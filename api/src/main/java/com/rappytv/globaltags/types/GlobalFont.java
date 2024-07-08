package com.rappytv.globaltags.types;

import net.labymod.api.client.component.format.Style;
import net.labymod.api.client.resources.ResourceLocation;
import java.util.HashMap;
import java.util.Map;

public enum GlobalFont {
    DEFAULT,
    VANILLA,
    FANCY,
    UNICODE;

    private static final Map<GlobalFont, Style> fontStyles = new HashMap<>();

    public Style getFontStyle() {
        if(fontStyles.containsKey(this)) return fontStyles.get(this);
        fontStyles.put(this, Style.empty().font(getResourceLocation()));
        return getFontStyle();
    }

    private ResourceLocation getResourceLocation() {
        return switch (this) {
            case VANILLA -> ResourceLocation.create(
                "minecraft",
                "default"
            );
            case UNICODE -> ResourceLocation.create(
                "minecraft",
                "uniform"
            );
            default -> ResourceLocation.create(
                "globaltags",
                "fonts/" + this.name().toLowerCase()
            );
        };
    }
}
