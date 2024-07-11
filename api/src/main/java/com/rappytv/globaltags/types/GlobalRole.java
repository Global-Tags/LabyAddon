package com.rappytv.globaltags.types;

import net.labymod.api.client.resources.ResourceLocation;

public enum GlobalRole {
    ADMIN("purple"),
    DEVELOPER("aqua"),
    MODERATOR("orange"),
    SUPPORTER("green");

    private final ResourceLocation location;

    GlobalRole(String color) {
        this.location = ResourceLocation.create(
            "globaltags",
            "textures/icon/staff/" + color + ".png"
        );
    }

    public ResourceLocation getLocation() {
        return location;
    }
}
