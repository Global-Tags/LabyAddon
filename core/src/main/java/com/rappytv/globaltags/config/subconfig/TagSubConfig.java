package com.rappytv.globaltags.config.subconfig;

import com.rappytv.globaltags.api.ApiHandler;
import com.rappytv.globaltags.types.GlobalIcon;
import com.rappytv.globaltags.util.Util;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.util.I18n;
import net.labymod.api.util.MethodOrder;

@Deprecated
public class TagSubConfig extends Config {

    public TagSubConfig() {
        position.addChangeListener((property, oldValue, newValue) -> {
            if(Laby.labyAPI().isFullyInitialized())
                ApiHandler.setPosition(newValue, (info) -> {});
        });
        globalIcon.addChangeListener((property, oldValue, newValue) -> {
            if(Laby.labyAPI().isFullyInitialized())
                ApiHandler.setIcon(newValue, (info) -> {});
        });
    }

    @TextFieldSetting
    @SpriteSlot(size = 32, y = 1)
    private final ConfigProperty<String> tag = new ConfigProperty<>("");

    @MethodOrder(after = "tag")
    @ButtonSetting
    @SpriteSlot(size = 32, y = 1, x = 1)
    public void setTag(Setting setting) {
        ApiHandler.setTag(tag.get(), (info) -> {});
        tag.set("");
        Util.clearCache(false);
    }

    @DropdownSetting
    @SpriteSlot(size = 32, x = 3)
    private final ConfigProperty<PositionType> position = new ConfigProperty<>(PositionType.ABOVE_NAME);


    @DropdownSetting
    @SpriteSlot(size = 32, y = 1, x = 2)
    private final ConfigProperty<GlobalIcon> globalIcon = new ConfigProperty<>(GlobalIcon.NONE);

    @MethodOrder(after = "globalIcon")
    @ButtonSetting
    @SpriteSlot(size = 32, y = 1, x = 3)
    public void resetTag(Setting setting) {
        ApiHandler.resetTag((info) -> {});
    }

    @MethodOrder(after = "resetTag")
    @ButtonSetting
    @SpriteSlot(size = 32, y = 2)
    public void clearCache(Setting setting) {
        if(Util.clearCache(true))
            Util.notify(
                I18n.translate("globaltags.notifications.success"),
                I18n.translate("globaltags.notifications.cacheCleared")
            );
    }
}
