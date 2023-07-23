package com.rappytv.globaltags.config;

import com.rappytv.globaltags.config.subconfig.TagSubConfig;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@ConfigName("settings")
@SpriteTexture("settings")
public class GlobalTagConfig extends AddonConfig {

    @SwitchSetting
    @SpriteSlot(size = 32)
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
    @SwitchSetting
    @SpriteSlot(size = 32, x = 1)
    private final ConfigProperty<Boolean> showOwnTag = new ConfigProperty<>(false);
    @SliderSetting(min = 5, max = 10)
    @SpriteSlot(size = 32, x = 2)
    private final ConfigProperty<Integer> tagSize = new ConfigProperty<>(10);
    @SpriteSlot(size = 32, x = 1)
    private final TagSubConfig tags = new TagSubConfig();

    @Override
    public ConfigProperty<Boolean> enabled() {
        return enabled;
    }
    public ConfigProperty<Boolean> showOwnTag() {
        return showOwnTag;
    }
    public ConfigProperty<Integer> tagSize() {
        return tagSize;
    }
}
