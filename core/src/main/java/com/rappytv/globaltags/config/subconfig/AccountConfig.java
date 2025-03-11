package com.rappytv.globaltags.config.subconfig;

import com.rappytv.globaltags.GlobalTagsAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.api.Util.ResultType;
import com.rappytv.globaltags.ui.widgets.config.TagPreviewWidget;
import com.rappytv.globaltags.ui.widgets.config.TagPreviewWidget.TagPreviewSetting;
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

public class AccountConfig extends Config {

    @SpriteSlot(size = 32, y = 1, x = 2)
    @DropdownSetting
    private final ConfigProperty<GlobalIcon> icon = new ConfigProperty<>(GlobalIcon.NONE);

    @IntroducedIn(namespace = "globaltags", value = "1.2.0")
    @SpriteSlot(size = 32, x = 1)
    @TagPreviewSetting
    private final ConfigProperty<Boolean> tagPreview = new ConfigProperty<>(false);

    @SpriteSlot(size = 32, y = 1)
    @TextFieldSetting
    private final ConfigProperty<String> tag = new ConfigProperty<>("");

    @SpriteSlot(size = 32, x = 3)
    @DropdownSetting
    private final ConfigProperty<GlobalPosition> position = new ConfigProperty<>(GlobalPosition.ABOVE);

    public AccountConfig() {
        Runnable runnable = () -> Debounce.of(
            "globaltags-config-update",
            1000,
            TagPreviewWidget::change
        );
        this.tag.addChangeListener(runnable);
        this.position.addChangeListener(runnable);
        this.icon.addChangeListener(runnable);
    }

    @MethodOrder(after = "icon")
    @SpriteSlot(size = 32, y = 1, x = 1)
    @ButtonSetting
    public void updateSettings(Setting setting) {
        GlobalTagAPI api = GlobalTagsAddon.getAPI();
        api.getCache().resolveSelf((info) -> {
            if(api.getAuthorization() == null) {
                Util.notify(
                    Component.translatable("globaltags.general.error"),
                    Component.translatable("globaltags.settings.account.tagPreview.labyConnect")
                );
                return;
            }
            if(info == null || !info.getPlainTag().equals(this.tag.get())) api.getApiHandler().setTag(
                this.tag.get(), (response) -> {
                    if (response.isSuccessful())
                        Util.update(api, ResultType.TAG,
                            Component.text("✔", NamedTextColor.GREEN));
                    else {
                        Util.update(api, ResultType.TAG,
                            Component.text(response.getError(), NamedTextColor.RED));
                    }
            });
            else Util.update(api, ResultType.TAG, Util.unchanged);
            if(info != null && !info.getPosition().equals(this.position.get())) api.getApiHandler().setPosition(
                this.position.get(), (response) -> {
                    if (response.isSuccessful())
                        Util.update(api, ResultType.POSITION,
                            Component.text("✔", NamedTextColor.GREEN));
                    else {
                        Util.update(api, ResultType.POSITION,
                            Component.text(response.getError(), NamedTextColor.RED));
                    }
            });
            else Util.update(api, ResultType.POSITION, Util.unchanged);
            if (info != null && !info.getGlobalIcon().equals(this.icon.get()))
                api.getApiHandler().setIcon(
                    this.icon.get(), (response) -> {
                    if (response.isSuccessful())
                        Util.update(api, ResultType.ICON,
                            Component.text("✔", NamedTextColor.GREEN));
                    else {
                        Util.update(api, ResultType.ICON,
                            Component.text(response.getError(), NamedTextColor.RED));
                    }
            });
            else Util.update(api, ResultType.ICON, Util.unchanged);
        });
    }

    @MethodOrder(after = "updateSettings")
    @SpriteSlot(size = 32, y = 1, x = 3)
    @ButtonSetting
    public void resetTag(Setting setting) {
        GlobalTagAPI api = GlobalTagsAddon.getAPI();
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
                    Util.getResponseComponent(info).color(NamedTextColor.WHITE)
            );
        });
    }

    public ConfigProperty<String> tag() {
        return this.tag;
    }
    public ConfigProperty<GlobalPosition> position() {
        return this.position;
    }
    public ConfigProperty<GlobalIcon> icon() {
        return this.icon;
    }
}
