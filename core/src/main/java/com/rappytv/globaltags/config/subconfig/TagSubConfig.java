package com.rappytv.globaltags.config.subconfig;

import com.rappytv.globaltags.api.ApiHandler;
import com.rappytv.globaltags.config.widget.TagPreviewWidget;
import com.rappytv.globaltags.config.widget.TagPreviewWidget.TagPreviewSetting;
import com.rappytv.globaltags.types.GlobalIcon;
import com.rappytv.globaltags.util.Util;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.util.Debounce;
import net.labymod.api.util.I18n;
import net.labymod.api.util.MethodOrder;

public class TagSubConfig extends Config {

    public TagSubConfig() {
        Runnable runnable = () -> Debounce.of("globaltags-config-update", 1000,
            TagPreviewWidget::change
        );
        tag.addChangeListener(runnable);
        position.addChangeListener(runnable);
        globalIcon.addChangeListener(runnable);
    }

    @TagPreviewSetting
    private final ConfigProperty<Boolean> tagPreview = new ConfigProperty<>(false);

    @TextFieldSetting
    @SpriteSlot(size = 32, y = 1)
    private final ConfigProperty<String> tag = new ConfigProperty<>("");

    @DropdownSetting
    @SpriteSlot(size = 32, x = 3)
    private final ConfigProperty<PositionType> position = new ConfigProperty<>(PositionType.ABOVE_NAME);

    @DropdownSetting
    @SpriteSlot(size = 32, y = 1, x = 2)
    private final ConfigProperty<GlobalIcon> globalIcon = new ConfigProperty<>(GlobalIcon.NONE);

    @MethodOrder(after = "globalIcon")
    @ButtonSetting
    @SpriteSlot(size = 32, y = 1, x = 1)
    public void updateSettings(Setting setting) {
        ApiHandler.setTag(tag.get(), (info) -> {});
        ApiHandler.setPosition(position.get(), (info) -> {});
        ApiHandler.setIcon(globalIcon.get(), (info) -> {});
        Util.clearCache();
    }

    @MethodOrder(after = "updateSettings")
    @ButtonSetting
    @SpriteSlot(size = 32, y = 1, x = 3)
    public void resetTag(Setting setting) {
        ApiHandler.resetTag((info) -> {});
    }

    @MethodOrder(after = "resetTag")
    @ButtonSetting
    @SpriteSlot(size = 32, y = 2)
    public void clearCache(Setting setting) {
        Util.clearCache();
        Util.notify(
            I18n.translate("globaltags.notifications.success"),
            I18n.translate("globaltags.notifications.cacheCleared")
        );
    }

    public ConfigProperty<String> tag() {
        return tag;
    }
    public ConfigProperty<PositionType> position() {
        return position;
    }

    public ConfigProperty<GlobalIcon> icon() {
        return globalIcon;
    }
}
