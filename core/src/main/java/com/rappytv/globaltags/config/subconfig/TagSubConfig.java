package com.rappytv.globaltags.config.subconfig;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.ApiHandler;
import com.rappytv.globaltags.util.Util;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.util.I18n;
import net.labymod.api.util.MethodOrder;

public class TagSubConfig extends Config {

    private final ApiHandler apiHandler;

    public TagSubConfig() {
        apiHandler = GlobalTagAddon.getAddon().getApiHandler();
    }

    @TextFieldSetting
    private final ConfigProperty<String> tag = new ConfigProperty<>("");

    @MethodOrder(after = "tag")
    @ButtonSetting
    public void setTag(Setting setting) {
        apiHandler.setTag(tag.get());
        tag.set("");
        Util.clearCache(false);
    }

    @MethodOrder(after = "setTag")
    @ButtonSetting
    public void resetTag(Setting setting) {
        apiHandler.resetTag();
        Util.clearCache(false);
    }

    @MethodOrder(after = "resetTag")
    @ButtonSetting
    public void clearCache(Setting setting) {
        if(Util.clearCache(true))
            Util.notify(
                I18n.translate("globaltags.notifications.success"),
                I18n.translate("globaltags.notifications.cacheCleared"),
                null
            );
    }
}
