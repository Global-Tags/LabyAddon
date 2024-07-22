package com.rappytv.globaltags.types;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

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

    /**
     * Get the {@link Icon} of the {@link GlobalIcon}
     * @return The {@link Icon} or null if the {@link GlobalIcon} equals {@link GlobalIcon#NONE}
     */
    @Nullable
    public Icon getIcon() {
        return this != NONE ? icon : null;
    }
}
