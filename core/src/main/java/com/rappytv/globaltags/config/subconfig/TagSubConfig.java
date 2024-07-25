package com.rappytv.globaltags.config.subconfig;

import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.api.Util.ResultType;
import com.rappytv.globaltags.config.widget.TagPreviewWidget;
import com.rappytv.globaltags.config.widget.TagPreviewWidget.TagPreviewSetting;
import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.enums.GlobalPosition;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
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

    private final GlobalTagAPI api;

    public TagSubConfig(GlobalTagAPI api) {
        this.api = api;
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
    private final ConfigProperty<GlobalPosition> position = new ConfigProperty<>(GlobalPosition.ABOVE);

    @DropdownSetting
    @SpriteSlot(size = 32, y = 1, x = 2)
    private final ConfigProperty<GlobalIcon> globalIcon = new ConfigProperty<>(GlobalIcon.NONE);

    @MethodOrder(after = "globalIcon")
    @ButtonSetting
    @SpriteSlot(size = 32, y = 1, x = 1)
    @SuppressWarnings("ConstantConditions")
    public void updateSettings(Setting setting) {
        api.getCache().resolveSelf((info) -> {
            if(api.getAuthorization() == null) {
                Util.notify(
                    Component.translatable("globaltags.notifications.error"),
                    Component.translatable("globaltags.settings.tags.tagPreview.labyConnect")
                );
                return;
            }
            if(info == null || !info.getPlainTag().equals(tag.get())) api.getApiHandler().setTag(tag.get(), (response) -> {
                if(response.successful()) Util.update(api, ResultType.TAG, Component.text("✔", NamedTextColor.GREEN));
                else Util.update(api, ResultType.TAG, Component.text(response.data(), NamedTextColor.RED));
            });
            else Util.update(api, ResultType.TAG, Util.unchanged);
            if(info != null && !info.getPosition().equals(position.get())) api.getApiHandler().setPosition(position.get(), (response) -> {
                if(response.successful()) Util.update(api, ResultType.POSITION, Component.text("✔", NamedTextColor.GREEN));
                else Util.update(api, ResultType.POSITION, Component.text(response.data(), NamedTextColor.RED));
            });
            else Util.update(api, ResultType.POSITION, Util.unchanged);
            if(info != null && !info.getGlobalIcon().equals(globalIcon.get())) api.getApiHandler().setIcon(globalIcon.get(), (response) -> {
                if(response.successful()) Util.update(api, ResultType.ICON, Component.text("✔", NamedTextColor.GREEN));
                else Util.update(api, ResultType.ICON, Component.text(response.data(), NamedTextColor.RED));
            });
            else Util.update(api, ResultType.ICON, Util.unchanged);
        });
    }

    @MethodOrder(after = "updateSettings")
    @ButtonSetting
    @SpriteSlot(size = 32, y = 1, x = 3)
    public void resetTag(Setting setting) {
        api.getApiHandler().resetTag((info) -> {
            if(info.successful()) TagPreviewWidget.refetch();
            Util.notify(
                Component.translatable(info.successful()
                    ? "globaltags.notifications.success"
                    : "globaltags.notifications.error"
                ),
                Component.text(info.data(), NamedTextColor.WHITE)
            );
        });
    }

    public ConfigProperty<String> tag() {
        return tag;
    }
    public ConfigProperty<GlobalPosition> position() {
        return position;
    }
    public ConfigProperty<GlobalIcon> icon() {
        return globalIcon;
    }
    public GlobalTagAPI getAPI() {
        return api;
    }
}
