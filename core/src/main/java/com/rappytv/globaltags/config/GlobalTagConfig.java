package com.rappytv.globaltags.config;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.config.subconfig.TagSubConfig;
import net.labymod.api.Laby;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.I18n;
import net.labymod.api.util.MethodOrder;

@ConfigName("settings")
@SpriteTexture("settings")
public class GlobalTagConfig extends AddonConfig {

    @SwitchSetting
    @SpriteSlot(size = 32)
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
    @IntroducedIn(namespace = "globaltags", value = "1.1.9")
    @SpriteSlot(size = 32, y = 2, x = 2)
    @SwitchSetting
    private final ConfigProperty<Boolean> localizedResponses = new ConfigProperty<>(true);
    @IntroducedIn(namespace = "globaltags", value = "1.2.0")
    @MethodOrder(after = "localizedResponses")
    @SpriteSlot(size = 32, y = 2, x = 3)
    @ButtonSetting
    public void joinDiscord(Setting setting) {
        Laby.references().chatExecutor().openUrl("https://globaltags.xyz/discord");
    }

    @SettingSection("display")
    @SwitchSetting
    @SpriteSlot(size = 32, x = 1)
    private final ConfigProperty<Boolean> showOwnTag = new ConfigProperty<>(true);
    @SpriteSlot(size = 32, x = 2)
    @SliderSetting(min = 5, max = 10)
    private final ConfigProperty<Integer> tagSize = new ConfigProperty<>(10);
    @IntroducedIn(namespace = "globaltags", value = "1.1.7")
    @SpriteSlot(size = 32, y = 2, x = 1)
    @SwitchSetting
    private final ConfigProperty<Boolean> showBackground = new ConfigProperty<>(false);

    @SettingSection("tags")
    @SpriteSlot(size = 32, y = 1)
    private final TagSubConfig tags = new TagSubConfig();

    @MethodOrder(after = "tags")
    @ButtonSetting
    @SpriteSlot(size = 32, y = 2)
    public void clearCache(Setting setting) {
        GlobalTagAddon.getAPI().getCache().clear();
        GlobalTagAddon.getAPI().getCache().resolveSelf();
        Util.notify(
            I18n.translate("globaltags.general.success"),
            I18n.translate("globaltags.commands.clear_cache.success")
        );
    }

    @Override
    public ConfigProperty<Boolean> enabled() {
        return enabled;
    }
    public ConfigProperty<Boolean> localizedResponses() {
        return localizedResponses;
    }
    public ConfigProperty<Boolean> showOwnTag() {
        return showOwnTag;
    }
    public ConfigProperty<Integer> tagSize() {
        return tagSize;
    }
    public ConfigProperty<Boolean> showBackground() {
        return showBackground;
    }
}
