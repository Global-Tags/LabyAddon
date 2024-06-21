package com.rappytv.globaltags.config.subconfig;

import com.rappytv.globaltags.api.ApiHandler;
import com.rappytv.globaltags.config.widget.TagPreviewWidget;
import com.rappytv.globaltags.config.widget.TagPreviewWidget.TagPreviewSetting;
import com.rappytv.globaltags.types.GlobalIcon;
import com.rappytv.globaltags.util.TagCache;
import com.rappytv.globaltags.util.Util;
import com.rappytv.globaltags.util.Util.ResultType;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.util.Debounce;
import net.labymod.api.util.MethodOrder;

public class TagSubConfig extends Config {

    public TagSubConfig() {
        Runnable runnable = () -> Debounce.of(
            "globaltags-config-update",
            1000,
            TagPreviewWidget::change
        );
        tag.addChangeListener(runnable);
        position.addChangeListener(runnable);
        globalIcon.addChangeListener(runnable);
    }

    @SpriteSlot(size = 32, x = 1)
    @IntroducedIn(namespace = "globaltags", value = "1.2.0")
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
        TagCache.resolve(Laby.labyAPI().getUniqueId(), (info) -> {
            if(!info.getPlainTag().equals(tag.get())) ApiHandler.setTag(tag.get(), (response) -> {
                if(response.isSuccessful()) Util.update(ResultType.TAG, Component.text("✔", NamedTextColor.GREEN));
                else Util.update(ResultType.TAG, response.getMessage());
            });
            else Util.update(ResultType.TAG, Util.unchanged);
            if(!info.getPosition().equals(position.get())) ApiHandler.setPosition(position.get(), (response) -> {
                if(response.isSuccessful()) Util.update(ResultType.POSITION, Component.text("✔", NamedTextColor.GREEN));
                else Util.update(ResultType.POSITION, response.getMessage());
            });
            else Util.update(ResultType.POSITION, Util.unchanged);
            if(!info.getGlobalIcon().equals(globalIcon.get())) ApiHandler.setIcon(globalIcon.get(), (response) -> {
                if(response.isSuccessful()) Util.update(ResultType.ICON, Component.text("✔", NamedTextColor.GREEN));
                else Util.update(ResultType.ICON, response.getMessage());
            });
            else Util.update(ResultType.ICON, Util.unchanged);
        });
    }

    @MethodOrder(after = "updateSettings")
    @ButtonSetting
    @SpriteSlot(size = 32, y = 1, x = 3)
    public void resetTag(Setting setting) {
        ApiHandler.resetTag((info) -> {
            if(info.isSuccessful()) TagPreviewWidget.refetch();
            Util.notify(
                Component.translatable(info.isSuccessful()
                    ? "globaltags.notifications.success"
                    : "globaltags.notifications.error"
                ),
                info.getMessage().color(NamedTextColor.WHITE)
            );
        });
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
