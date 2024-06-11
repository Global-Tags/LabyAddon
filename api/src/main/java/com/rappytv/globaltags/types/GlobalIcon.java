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

    private final Map<String, Icon> iconCache = new HashMap<>();

    public Icon getIcon() {
        String name = name().toLowerCase();
        if(iconCache.containsKey(name))
            return iconCache.get(name);
        ResourceLocation location = ResourceLocation.create(
            "globaltags",
            "textures/icons/" + name + ".png"
        );
        iconCache.put(
            name,
            location.exists() ? Icon.texture(location) : null
        );
        return getIcon();
    }
}
