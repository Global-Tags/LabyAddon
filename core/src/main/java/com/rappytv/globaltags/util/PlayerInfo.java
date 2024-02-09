package com.rappytv.globaltags.util;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.tag.PositionType;

public class PlayerInfo {

    private final Component tag;
    private final String position;
    private final String icon;

    public PlayerInfo(Component tag, String position, String icon) {
        this.tag = tag;
        this.position = position;
        this.icon = icon;
    }

    public Component getTag() {
        return tag;
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

    public GlobalIcon getIcon() {
        try {
            return GlobalIcon.valueOf(icon.toUpperCase());
        } catch (IllegalArgumentException e) {
            return GlobalIcon.NONE;
        }
    }
}
