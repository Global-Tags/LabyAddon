package com.rappytv.globaltags.types;

import com.rappytv.globaltags.util.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInfo {

    private final UUID uuid;
    private final Component tag;
    private final String plainTag;
    private final String position;
    private final String icon;
    private final boolean admin;

    public PlayerInfo(UUID uuid, String tag, String position, String icon, boolean admin) {
        this.uuid = uuid;
        this.tag = Util.translateColorCodes(tag);
        this.plainTag = tag;
        this.position = position;
        this.icon = icon;
        this.admin = admin;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Component getTag() {
        return tag;
    }

    public String getPlainTag() {
        return plainTag;
    }

    public PositionType getPosition() {
        if(position == null) return PositionType.ABOVE_NAME;
        return switch(position) {
            default -> PositionType.ABOVE_NAME;
            case "BELOW" -> PositionType.BELOW_NAME;
            case "RIGHT" -> PositionType.RIGHT_TO_NAME;
            case "LEFT" -> PositionType.LEFT_TO_NAME;
        };
    }

    // To reduce object creation. Won't be cleared until restart
    private final Map<String, Icon> iconCache = new HashMap<>();

    public String getIconName() {
        return icon;
    }

    public Icon getIcon() {
        if(iconCache.containsKey(this.icon.toLowerCase()))
            return iconCache.get(this.icon.toLowerCase());
        ResourceLocation location = ResourceLocation.create(
            "globaltags",
            "textures/icons/" + this.icon.toLowerCase() + ".png"
        );
        iconCache.put(
            this.icon.toLowerCase(),
            location.exists() ? Icon.texture(location) : null
        );
        return getIcon();
    }

    public boolean isAdmin() {
        return admin;
    }
}
