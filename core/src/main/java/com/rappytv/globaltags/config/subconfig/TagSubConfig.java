package com.rappytv.globaltags.config.subconfig;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.ApiHandler;
import com.rappytv.globaltags.util.GlobalIcon;
import com.rappytv.globaltags.util.Util;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.Exclude;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.util.I18n;
import net.labymod.api.util.MethodOrder;

public class TagSubConfig extends Config {

    @Exclude
    private final ApiHandler apiHandler;

    public TagSubConfig() {
        apiHandler = GlobalTagAddon.getAddon().getApiHandler();
        position.addChangeListener((property, oldValue, newValue) -> apiHandler.setPosition(newValue));
        globalIcon.addChangeListener((property, oldValue, newValue) -> apiHandler.setIcon(newValue));
    }

    @TextFieldSetting
    @SpriteSlot(size = 32, y = 1)
    private final ConfigProperty<String> tag = new ConfigProperty<>("");

    @MethodOrder(after = "tag")
    @ButtonSetting
    @SpriteSlot(size = 32, y = 1, x = 1)
    public void setTag(Setting setting) {
        apiHandler.setTag(tag.get());
        tag.set("");
        Util.clearCache(false);
    }

    @DropdownSetting
    @SpriteSlot(size = 32, x = 3)
    private final ConfigProperty<PositionType> position = new ConfigProperty<>(PositionType.ABOVE_NAME);

    @DropdownSetting
    private final ConfigProperty<GlobalIcon> globalIcon = new ConfigProperty<>(GlobalIcon.NONE);

    @MethodOrder(after = "globalIcon")
    @ButtonSetting
    @SpriteSlot(size = 32, y = 1, x = 2)
    public void resetTag(Setting setting) {
        apiHandler.resetTag();
    }

    @MethodOrder(after = "resetTag")
    @ButtonSetting
    @SpriteSlot(size = 32, y = 1, x = 3)
    public void clearCache(Setting setting) {
        if(Util.clearCache(true))
            Util.notify(
                I18n.translate("globaltags.notifications.success"),
                I18n.translate("globaltags.notifications.cacheCleared"),
                false
            );
    }
}
