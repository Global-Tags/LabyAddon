package com.rappytv.globaltags.types;

import net.labymod.api.client.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;

public enum GlobalFont {
    DEFAULT,
    UNICODE,
    ALAGARD(3),
    GLACIAL_INDIFFERENCE,
    SUPER_RPG,
    DARK_SOULS;

    private static final Map<GlobalFont, ResourceLocation> locations = new HashMap<>();

    private final double staffMargin;

    GlobalFont() {
        this(0);
    }

    GlobalFont(double staffMargin) {
        this.staffMargin = staffMargin;
    }

    public double getStaffMargin() {
        return staffMargin;
    }

    @Nullable
    public ResourceLocation getCachedLocation() {
        if(locations.containsKey(this)) return locations.get(this);
        locations.put(this, getResourceLocation());
        return getCachedLocation();
    }

    @Nullable
    private ResourceLocation getResourceLocation() {
        return switch (this) {
            case DEFAULT -> null;
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
