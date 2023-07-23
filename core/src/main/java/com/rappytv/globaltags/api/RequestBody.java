package com.rappytv.globaltags.api;

import net.labymod.api.client.entity.player.tag.PositionType;

public class RequestBody {

    public String tag;
    public String position;

    public RequestBody(String tag) {
        this.tag = tag;
    }
    public RequestBody(PositionType type) {
        position = switch (type) {
            case ABOVE_NAME -> "ABOVE";
            case BELOW_NAME -> "BELOW";
            case RIGHT_TO_NAME -> "RIGHT";
            case LEFT_TO_NAME -> "LEFT";
        };
    }
}
