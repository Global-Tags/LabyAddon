package com.rappytv.globaltags.api;

import net.labymod.api.client.entity.player.tag.PositionType;

public class RequestBody {

    public String tag;
    public String position;
    public String reason;

    public RequestBody(String argument, StringType type) {
        switch (type) {
            case TAG -> this.tag = argument;
            case REPORT_REASON -> this.reason = argument;
        }
    }
    public RequestBody(PositionType type) {
        position = switch (type) {
            case ABOVE_NAME -> "ABOVE";
            case BELOW_NAME -> "BELOW";
            case RIGHT_TO_NAME -> "RIGHT";
            case LEFT_TO_NAME -> "LEFT";
        };
    }

    public enum StringType {
        TAG,
        REPORT_REASON
    }
}
