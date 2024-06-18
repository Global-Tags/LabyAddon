package com.rappytv.globaltags.types;

import com.rappytv.globaltags.api.ResponseBody.Ban;
import com.rappytv.globaltags.util.Util;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.gui.icon.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public class PlayerInfo {

    private final UUID uuid;
    private final Component tag;
    private final String plainTag;
    private final String position;
    private final String icon;
    private final boolean admin;
    private final Ban ban;

    public PlayerInfo(UUID uuid, String tag, String position, String icon, boolean admin, Ban ban) {
        this.uuid = uuid;
        this.tag = Util.translateColorCodes(tag);
        this.plainTag = tag != null ? tag : "";
        this.position = position;
        this.icon = icon;
        this.admin = admin;
        this.ban = ban;
    }

    public UUID getUUID() {
        return uuid;
    }

    @NotNull
    public Component getTag() {
        return tag;
    }

    @NotNull
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

    public GlobalIcon getGlobalIcon() {
        try {
            return GlobalIcon.valueOf(icon);
        } catch (Exception ignored) {
            return GlobalIcon.NONE;
        }
    }

    public Icon getIcon() {
        return getGlobalIcon().getIcon();
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean isBanned() {
        return ban != null && ban.active;
    }

    @Nullable
    public String getBanReason() {
        if(ban == null) return null;
        return ban.reason;
    }
}
