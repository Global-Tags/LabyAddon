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
    private final Suspension suspension;

    public PlayerInfo(UUID uuid, String tag, String position, String icon, boolean admin, Ban ban) {
        this.uuid = uuid;
        this.tag = Util.translateColorCodes(tag);
        this.plainTag = tag != null ? tag : "";
        this.position = position;
        this.icon = icon;
        this.admin = admin;
        this.suspension = new Suspension(ban);
    }

    /**
     * Returns the player's uuid
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Returns the player's GlobalTag as a colored component
     */
    @Nullable
    public Component getTag() {
        return !plainTag.isEmpty() ? tag : null;
    }

    /**
     * Returns the player's GlobalTag as a plain string - with color codes
     */
    @NotNull
    public String getPlainTag() {
        return plainTag;
    }

    /**
     * Returns the player's GlobalTag position
     */
    public PositionType getPosition() {
        if(position == null) return PositionType.ABOVE_NAME;
        return switch(position) {
            default -> PositionType.ABOVE_NAME;
            case "BELOW" -> PositionType.BELOW_NAME;
            case "RIGHT" -> PositionType.RIGHT_TO_NAME;
            case "LEFT" -> PositionType.LEFT_TO_NAME;
        };
    }

    /**
     * Returns the {@link GlobalIcon} enum value which the player has selected
     */
    public GlobalIcon getGlobalIcon() {
        try {
            return GlobalIcon.valueOf(icon);
        } catch (Exception ignored) {
            return GlobalIcon.NONE;
        }
    }

    /**
     * Returns the global icon of the player
     */
    public Icon getIcon() {
        return getGlobalIcon().getIcon();
    }

    /**
     * Returns if the player is a GlobalTag admin
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Use {@link PlayerInfo#isSuspended()} instead
     */
    @Deprecated(forRemoval = true)
    public boolean isBanned() {
        return isSuspended();
    }

    /**
     * Use {@link PlayerInfo#getSuspension()} instead
     */
    @Deprecated(forRemoval = true)
    @Nullable
    public String getBanReason() {
        return suspension.reason;
    }

    /**
     * Shortcut to check if the player suspension is active
     */
    public boolean isSuspended() {
        return suspension.active;
    }

    /**
     * Gets the suspension object from a player
     */
    public Suspension getSuspension() {
        return suspension;
    }

    public static class Suspension {

        private final boolean active;
        private final String reason;
        private final boolean appealable;

        /**
         * Creates a suspension from a {@link Ban} object
         */
        protected Suspension(Ban ban) {
            this.active = ban.active;
            this.reason = ban.reason;
            this.appealable = ban.appealable;
        }

        /**
         * Creates an inactive suspension
         */
        protected Suspension() {
            this.active = false;
            this.reason = null;
            this.appealable = false;
        }

        /**
         * Creates an active suspension
         */
        protected Suspension(String reason, boolean appealable) {
            this.active = true;
            this.reason = reason;
            this.appealable = appealable;
        }

        /**
         * Returns if the suspension is active or not
         */
        public boolean isActive() {
            return active;
        }

        /**
         * Returns the suspension reason
         */
        @Nullable
        public String getReason() {
            return reason;
        }

        /**
         * Returns if the suspension can be appealed
         */
        public boolean isAppealable() {
            return appealable;
        }
    }
}
