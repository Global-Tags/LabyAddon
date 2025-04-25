package com.rappytv.globaltags.config.subconfig;

import com.rappytv.globaltags.GlobalTagsAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.api.Util.ResultType;
import com.rappytv.globaltags.api.event.RefreshInfoEvent;
import com.rappytv.globaltags.ui.activities.config.IconChooserActivity;
import com.rappytv.globaltags.ui.activities.config.TagEditorActivity;
import com.rappytv.globaltags.ui.widgets.config.AccountInfoWidget.AccountInfoSetting;
import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.enums.GlobalPosition;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.widgets.activity.settings.ActivitySettingWidget.ActivitySetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.Exclude;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.Debounce;
import net.labymod.api.util.MethodOrder;

public class AccountConfig extends Config {

    private final static Component TICK = Component.text("✔", NamedTextColor.GREEN);

    public AccountConfig() {
        Runnable runnable = () -> Debounce.of(
            "globaltags-config-update",
            1000,
            () -> Laby.fireEvent(new RefreshInfoEvent())
        );
        this.tag.addChangeListener(runnable);
        this.position.addChangeListener(runnable);
        this.icon.addChangeListener(runnable);
        this.hideRoleIcon.addChangeListener(runnable);
    }

    @Exclude
    private final ConfigProperty<String> tag = new ConfigProperty<>("");

    @Exclude
    private final ConfigProperty<GlobalIcon> icon = new ConfigProperty<>(GlobalIcon.NONE);

    @IntroducedIn(namespace = "globaltags", value = "1.2.0")
    @SpriteSlot(size = 32, x = 2, y = 1)
    @AccountInfoSetting
    private final ConfigProperty<Boolean> accountInfo = new ConfigProperty<>(false);

    @SettingSection(value = "settings", center = true)
    @IntroducedIn(namespace = "globaltags", value = "1.4.1")
    @MethodOrder(after = "accountInfo")
    @SpriteSlot(x = 3, y = 2)
    @ActivitySetting
    public Activity tagEditor() {
        String session = GlobalTagsAddon.getAPI().getAuthorization();
        if (session == null) {
            return new TagEditorActivity();
        }
        return new TagEditorActivity(
            GlobalTagsAddon.getAPI().getCache().get(GlobalTagsAddon.getAPI().getClientUUID()),
            this
        );
    }

    @SpriteSlot(x = 2, y = 1)
    @DropdownSetting
    private final ConfigProperty<GlobalPosition> position = new ConfigProperty<>(
        GlobalPosition.ABOVE);

    @MethodOrder(after = "position")
    @SpriteSlot(x = 1, y = 2)
    @ActivitySetting
    public Activity iconChooser() {
        String session = GlobalTagsAddon.getAPI().getAuthorization();
        if (session == null) {
            return new IconChooserActivity();
        }
        return new IconChooserActivity(
            GlobalTagsAddon.getAPI().getCache().get(GlobalTagsAddon.getAPI().getClientUUID()),
            this
        );
    }

    @IntroducedIn(namespace = "globaltags", value = "1.4.1")
    @SpriteSlot(y = 3)
    @SwitchSetting
    private final ConfigProperty<Boolean> hideRoleIcon = new ConfigProperty<>(false);

    @SettingSection(value = "actions", center = true)
    @MethodOrder(after = "hideRoleIcon")
    @SpriteSlot(x = 3, y = 1)
    @ButtonSetting
    public void updateSettings(Setting setting) {
        GlobalTagAPI api = GlobalTagsAddon.getAPI();
        api.getCache().resolveSelf((info) -> {
            if(api.getAuthorization() == null) {
                Util.notify(
                    Component.translatable("globaltags.general.error"),
                    Component.translatable("globaltags.settings.account.accountInfo.labyConnect")
                );
                return;
            }

            // Update tag
            if (info == null || !info.getPlainTag().equals(this.tag.get())) {
                api.getApiHandler().setTag(
                    this.tag.get(), (response) -> {
                        if (response.isSuccessful()) {
                            Util.update(
                                api,
                                ResultType.TAG,
                                TICK
                            );
                        } else {
                            Util.update(api, ResultType.TAG,
                                Component.text(response.getError(), NamedTextColor.RED));
                        }
                    });
            } else {
                Util.update(api, ResultType.TAG, Util.unchanged);
            }

            // Update position
            if (info == null || !info.getPosition().equals(this.position.get())) {
                api.getApiHandler().setPosition(
                    this.position.get(), (response) -> {
                        if (response.isSuccessful()) {
                            Util.update(
                                api,
                                ResultType.POSITION,
                                TICK
                            );
                        } else {
                            Util.update(
                                api,
                                ResultType.POSITION,
                                Component.text(response.getError(), NamedTextColor.RED)
                            );
                        }
                    });
            } else {
                Util.update(api, ResultType.POSITION, Util.unchanged);
            }

            // Update icon
            if (info == null || !info.getGlobalIcon().equals(this.icon.get())) {
                api.getApiHandler().setIcon(this.icon.get(), (response) -> {
                    System.out.println(response.isSuccessful() + " " + response.getData() + " "
                        + response.getError());
                    if (response.isSuccessful()) {
                        Util.update(
                            api,
                            ResultType.ICON,
                            TICK
                        );
                    } else {
                        Util.update(
                            api,
                            ResultType.ICON,
                            Component.text(response.getError(), NamedTextColor.RED)
                        );
                    }
                });
            } else {
                Util.update(api, ResultType.ICON, Util.unchanged);
            }

            // Update role icon visibility
            if (info == null || info.isRoleIconHidden() != this.hideRoleIcon.get()) {
                api.getApiHandler().setRoleIconVisibility(
                    !this.hideRoleIcon.get(),
                    (response) -> {
                        if (response.isSuccessful()) {
                            Util.update(
                                api,
                                ResultType.ROLE_ICON_VISIBILITY,
                                TICK
                            );
                        } else {
                            Util.update(
                                api,
                                ResultType.ROLE_ICON_VISIBILITY,
                                Component.text(response.getError(), NamedTextColor.RED)
                            );
                        }
                    });
            } else {
                Util.update(api, ResultType.ROLE_ICON_VISIBILITY, Util.unchanged);
            }
        });
    }

    @MethodOrder(after = "updateSettings")
    @SpriteSlot(size = 32, x = 3, y = 1)
    @ButtonSetting
    public void resetTag(Setting setting) {
        GlobalTagAPI api = GlobalTagsAddon.getAPI();
        api.getApiHandler().resetTag((info) -> {
            if(info.isSuccessful()) {
                Util.broadcastTagUpdate();
                Laby.fireEvent(new RefreshInfoEvent(true));
            }
            Util.sendResponseNotification(info);
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
    public ConfigProperty<Boolean> hideRoleIcon() {
        return this.hideRoleIcon;
    }
}
