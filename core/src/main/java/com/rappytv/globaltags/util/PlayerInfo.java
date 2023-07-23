package com.rappytv.globaltags.util;

import net.labymod.api.client.entity.player.tag.PositionType;

public class PlayerInfo {

    private final String tag;
    private final String position;

    public PlayerInfo(String tag, String position) {
        this.tag = tag;
        this.position = position;
    }

    public String getTag() {
        return tag;
    }

    public PositionType getPosition() {
        return switch(position) {
            default -> PositionType.ABOVE_NAME;
            case "BELOW" -> PositionType.BELOW_NAME;
            case "RIGHT" -> PositionType.RIGHT_TO_NAME;
            case "LEFT" -> PositionType.LEFT_TO_NAME;
        };
    }
}
