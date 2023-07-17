package com.rappytv.globaltags.config;

import com.rappytv.globaltags.config.subconfig.TagSubConfig;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@ConfigName("settings")
public class GlobalTagConfig extends AddonConfig {

    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
    @SwitchSetting
    private final ConfigProperty<Boolean> showOwnTag = new ConfigProperty<>(false);
    @SliderSetting(min = .1f, max = 1f, steps = .1f)
    private final ConfigProperty<Float> tagSize = new ConfigProperty<>(1f);
    @DropdownSetting
    private final ConfigProperty<PositionType> position = new ConfigProperty<>(PositionType.ABOVE_NAME);
    private final TagSubConfig tags = new TagSubConfig();

    @Override
    public ConfigProperty<Boolean> enabled() {
        return enabled;
    }
    public ConfigProperty<Boolean> showOwnTag() {
        return showOwnTag;
    }
    public ConfigProperty<Float> tagSize() {
        return tagSize;
    }
    public ConfigProperty<PositionType> position() {
        return position;
    }
}
