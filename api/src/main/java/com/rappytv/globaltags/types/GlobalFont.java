package com.rappytv.globaltags.types;

import net.labymod.api.Laby;
import net.labymod.api.client.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;

public enum GlobalFont {
    DEFAULT(0, 0),
    UNICODE(-1, -10),
    ALAGARD(1, 3),
    DARK_SOULS(1, 22),
    GLACIAL_INDIFFERENCE(3, 18),
    SUPER_RPG(2, 10);

    private static final Map<GlobalFont, ResourceLocation> locations = new HashMap<>();

    private final double vanillaMargin;
    private final double fancyMargin;

    GlobalFont(double vanillaMargin, double fancyMargin) {
        this.vanillaMargin = vanillaMargin;
        this.fancyMargin = fancyMargin;
    }

    public double getStaffMargin() {
        return switch (Laby.labyAPI().themeService().currentTheme().getId()) {
            case "vanilla" -> vanillaMargin;
            case "fancy" -> fancyMargin;
            default -> 0;
        };
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
