package com.rappytv.globaltags.config.subconfig;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.config.activity.TagUpdateActivity;
import com.rappytv.globaltags.config.widget.TagPreviewWidget;
import com.rappytv.globaltags.config.widget.TagPreviewWidget.TagPreviewSetting;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.widgets.activity.settings.ActivitySettingWidget.ActivitySetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.util.MethodOrder;

public class TagSubConfig extends Config {

    @SpriteSlot(size = 32, x = 1)
    @IntroducedIn(namespace = "globaltags", value = "1.2.0")
    @TagPreviewSetting
    private final ConfigProperty<Boolean> tagPreview = new ConfigProperty<>(false);

    // TODO: ADD IntroducedIn annotation
    @MethodOrder(after = "tagPreview")
    @ActivitySetting
    @SpriteSlot(size = 32, y = 1)
    public Activity updateTag() {
        return new TagUpdateActivity(GlobalTagAddon.getAPI().getCache().get(
            GlobalTagAddon.getAPI().getClientUUID()
        ));
    }

    @MethodOrder(after = "updateTag")
    @ButtonSetting
    @SpriteSlot(size = 32, y = 1, x = 3)
    public void resetTag() {
        GlobalTagAPI api = GlobalTagAddon.getAPI();
        api.getApiHandler().resetTag((info) -> {
            if(info.isSuccessful()) {
                Util.broadcastTagUpdate();
                TagPreviewWidget.refetch();
            }
            Util.notify(
                Component.translatable(info.isSuccessful()
                    ? "globaltags.general.success"
                    : "globaltags.general.error"
                ),
                Component.text(info.getData(), NamedTextColor.WHITE)
            );
        });
    }
}
