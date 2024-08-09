package com.rappytv.globaltags.config.subconfig;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ShowSettingInParent;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class RainbowTagSubconfig extends Config {

    @ShowSettingInParent
    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
    @SwitchSetting
    private final ConfigProperty<Boolean> bold = new ConfigProperty<>(false);
    @SwitchSetting
    private final ConfigProperty<Boolean> italic = new ConfigProperty<>(false);
    @SwitchSetting
    private final ConfigProperty<Boolean> underscored = new ConfigProperty<>(false);
    @SwitchSetting
    private final ConfigProperty<Boolean> strikethrough = new ConfigProperty<>(false);
    @DropdownSetting
    private final ConfigProperty<RainbowDirection> direction = new ConfigProperty<>(RainbowDirection.LEFT_TO_RIGHT);
    @DropdownSetting
    private final ConfigProperty<RainbowMode> mode = new ConfigProperty<>(RainbowMode.FULL_TAG);

    public boolean enabled() {
        return enabled.get();
    }

    public boolean bold() {
        return bold.get();
    }

    public boolean italic() {
        return italic.get();
    }

    public boolean underscored() {
        return underscored.get();
    }

    public boolean strikethrough() {
        return strikethrough.get();
    }

    public RainbowDirection direction() {
        return direction.get();
    }

    public RainbowMode mode() {
        return mode.get();
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
