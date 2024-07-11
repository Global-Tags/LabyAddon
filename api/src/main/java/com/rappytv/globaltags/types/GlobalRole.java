package com.rappytv.globaltags.types;

import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public enum GlobalRole {
    ADMIN("purple"),
    DEVELOPER("aqua"),
    MODERATOR("orange"),
    SUPPORTER("green");

    private final Icon icon;

    GlobalRole(String color) {
        this.icon = Icon.texture(ResourceLocation.create(
            "globaltags",
            "textures/icon/roles/" + color + ".png"
        ));
    }

    public Icon getIcon() {
        return icon;
    }
}
