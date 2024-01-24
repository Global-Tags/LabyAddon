package com.rappytv.globaltags.util;

import net.labymod.api.client.resources.ResourceLocation;

public enum GlobalIcon {
    NONE,
    DISCORD,
    TWITCH,
    YOUTUBE;

    private final ResourceLocation resourceLocation;

    GlobalIcon() {
        this.resourceLocation = ResourceLocation.create(
            "globaltags",
            "textures/icons/" + this.name().toLowerCase() + ".png"
        );
    }

    public ResourceLocation resourceLocation() {
        return resourceLocation;
    }
}
