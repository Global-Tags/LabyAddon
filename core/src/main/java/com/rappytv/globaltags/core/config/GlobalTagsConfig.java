package com.rappytv.globaltags.core.config;

import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.core.GlobalTagsAddon;
import com.rappytv.globaltags.core.config.subconfig.AccountConfig;
import com.rappytv.globaltags.core.ui.activities.config.HiddenTagListActivity;
import com.rappytv.globaltags.core.ui.activities.config.ReferralLeaderboardActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.widgets.activity.settings.ActivitySettingWidget.ActivitySetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.Exclude;
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
public class GlobalTagsConfig extends AddonConfig {

    @Exclude
    private final List<UUID> hiddenTags = new ArrayList<>();

    @SpriteSlot
    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

    @IntroducedIn(namespace = "globaltags", value = "1.1.9")
    @SpriteSlot(size = 32, x = 2)
    @SwitchSetting
    private final ConfigProperty<Boolean> localizedResponses = new ConfigProperty<>(true);

    @IntroducedIn(namespace = "globaltags", value = "1.4.0")
    @SpriteSlot(x = 1)
    @SwitchSetting
    private final ConfigProperty<Boolean> showBulletPoints = new ConfigProperty<>(true);

    @SettingSection(value = "display", center = true)
    @SpriteSlot(size = 32, x = 2, y = 1)
    @SwitchSetting
    private final ConfigProperty<Boolean> showOwnTag = new ConfigProperty<>(true);
    @SpriteSlot(y = 1)
    @SliderSetting(min = 5, max = 10)
    private final ConfigProperty<Integer> tagSize = new ConfigProperty<>(10);
    @IntroducedIn(namespace = "globaltags", value = "1.1.7")
    @SpriteSlot(x = 1, y = 1)
    @SwitchSetting
    private final ConfigProperty<Boolean> showBackground = new ConfigProperty<>(false);
    @SettingSection(value = "tags", center = true)
    @SpriteSlot(y = 2)
    private final AccountConfig account = new AccountConfig();

    @MethodOrder(after = "showBulletPoints")
    @IntroducedIn(namespace = "globaltags", value = "1.2.0")
    @SpriteSlot(size = 32, x = 3)
    @ButtonSetting
    public void joinDiscord(Setting setting) {
        Laby.references().chatExecutor().openUrl("https://globaltags.xyz/discord");
    }

    @MethodOrder(after = "joinDiscord")
    @IntroducedIn(namespace = "globaltags", value = "1.3.5")
    @SpriteSlot(x = 2)
    @ActivitySetting
    public Activity referralLeaderboards() {
        return new ReferralLeaderboardActivity();
    }

    @MethodOrder(after = "showBackground")
    @IntroducedIn(namespace = "globaltags", value = "1.4.0")
    @SpriteSlot(x = 3)
    @ActivitySetting
    public Activity hiddenTagList() {
        return new HiddenTagListActivity(this);
    }

    @MethodOrder(after = "account")
    @SpriteSlot(x = 2, y = 2)
    @ButtonSetting
    public void clearCache(Setting setting) {
        GlobalTagsAddon.getAPI().getCache().clear();
        GlobalTagsAddon.getAPI().getCache().resolveSelf();
        Util.notify(
            I18n.translate("globaltags.general.success"),
            I18n.translate("globaltags.commands.clearCache.success")
        );
    }

    public List<UUID> hiddenTags() {
        return this.hiddenTags;
    }

    @Override
    public ConfigProperty<Boolean> enabled() {
        return this.enabled;
    }
    public ConfigProperty<Boolean> localizedResponses() {
        return this.localizedResponses;
    }
    public ConfigProperty<Boolean> showBulletPoints() {
        return this.showBulletPoints;
    }
    public ConfigProperty<Boolean> showOwnTag() {
        return this.showOwnTag;
    }
    public ConfigProperty<Integer> tagSize() {
        return this.tagSize;
    }
    public ConfigProperty<Boolean> showBackground() {
        return this.showBackground;
    }
}
