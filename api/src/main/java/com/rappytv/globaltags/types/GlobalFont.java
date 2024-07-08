package com.rappytv.globaltags.types;

import net.labymod.api.client.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public enum GlobalFont {
    DEFAULT,
    UNICODE,
    DARK_SOULS;

    private static final Map<GlobalFont, ResourceLocation> locations = new HashMap<>();

    @NotNull
    public ResourceLocation getCachedLocation() {
        if(locations.containsKey(this)) return locations.get(this);
        locations.put(this, getResourceLocation());
        return getCachedLocation();
    }

    @NotNull
    private ResourceLocation getResourceLocation() {
        return switch (this) {
            case DEFAULT -> ResourceLocation.create(
                "minecraft",
                "default"
            );
            case UNICODE -> ResourceLocation.create(
                "minecraft",
                "uniform"
            );
            default -> ResourceLocation.create(
                "globaltags",
                this.name().toLowerCase()
            );
        };
    }
}
