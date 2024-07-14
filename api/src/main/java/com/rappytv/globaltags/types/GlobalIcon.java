package com.rappytv.globaltags.types;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
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

    private final Icon icon;

    GlobalIcon() {
        this.icon = Icon.texture(ResourceLocation.create(
            "globaltags",
            "textures/icons/" + this.name().toLowerCase() + ".png"
        ));
    }

    public Icon getIcon() {
        return icon;
    }
}
