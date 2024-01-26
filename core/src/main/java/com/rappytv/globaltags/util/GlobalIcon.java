package com.rappytv.globaltags.util;

import net.labymod.api.client.resources.ResourceLocation;

public enum GlobalIcon {
    NONE,
    BEREAL,
    DISCORD,
    EBIO,
    EPICGAMES,
    GITHUB,
    GITLAB,
    INSTAGRAM,
    KICK,
    PLAYSTATION,
    SNAPCHAT,
    STEAM,
    THREADS,
    TIKTOK,
    TWITCH,
    X,
    XBOX,
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
