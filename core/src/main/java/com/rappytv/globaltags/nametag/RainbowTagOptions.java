package com.rappytv.globaltags.nametag;

public class RainbowTagOptions {

    private final boolean enabled;
    private final boolean bold;
    private final boolean italic;
    private final boolean underscored;
    private final boolean strikethrough;
    private final RainbowDirection direction;
    private final RainbowMode mode;

    public RainbowTagOptions() {
        this.enabled = true;
        this.bold = false;
        this.italic = false;
        this.underscored = false;
        this.strikethrough = false;
        this.direction = RainbowDirection.LEFT_TO_RIGHT;
        this.mode = RainbowMode.SEPERATE_LETTERS;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public boolean isUnderscored() {
        return underscored;
    }

    public boolean isStrikethrough() {
        return strikethrough;
    }

    public RainbowDirection getDirection() {
        return direction;
    }

    public RainbowMode getMode() {
        return mode;
    }

    public enum RainbowDirection {
        RIGHT_TO_LEFT,
        LEFT_TO_RIGHT
    }

    public enum RainbowMode {
        FULL_TAG,
        SEPERATE_LETTERS
    }
}
