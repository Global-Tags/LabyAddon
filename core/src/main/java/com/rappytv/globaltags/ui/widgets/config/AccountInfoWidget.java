package com.rappytv.globaltags.ui.widgets.config;

import com.rappytv.globaltags.GlobalTagsAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.config.subconfig.AccountConfig;
import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import net.labymod.api.Laby;
import net.labymod.api.Textures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.Links;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.accessor.SettingAccessor;
import net.labymod.api.configuration.settings.annotation.SettingElement;
import net.labymod.api.configuration.settings.annotation.SettingFactory;
import net.labymod.api.configuration.settings.annotation.SettingWidget;
import net.labymod.api.configuration.settings.widget.WidgetFactory;
import net.labymod.api.util.ThreadSafe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;
import java.util.function.Consumer;

@Links({@Link("account-info.lss"), @Link("preview.lss")})
@AutoWidget
@SettingWidget
public class AccountInfoWidget extends HorizontalListWidget {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private static boolean refetch = true;
    private static boolean changed = false;
    private final AccountConfig config;

    private AccountInfoWidget(AccountConfig config) {
        this.config = config;
    }

    public static void change() {
        AccountInfoWidget.changed = true;
    }

    public static void refetch() {
        AccountInfoWidget.refetch = true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!refetch && !changed) return;
        if (refetch)
            GlobalTagsAddon.getAPI().getCache().remove(GlobalTagsAddon.getAPI().getClientUUID());
        this.reInitialize();
        refetch = false;
        changed = false;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        this.initialize(refetch);
    }

    @SuppressWarnings("ConstantConditions")
    public void initialize(boolean refetched) {
        GlobalTagAPI api = GlobalTagsAddon.getAPI();

        this.addEntry(ButtonWidget
                .icon(SpriteCommon.REFRESH, AccountInfoWidget::refetch)
                .addId("refresh-button")
        );
        api.getCache().resolveSelf((info) -> {
            if (ThreadSafe.isRenderThread()) {
                this.initializeWithInfo(info, refetched, false);
            } else {
                Laby.labyAPI().minecraft().executeOnRenderThread(
                        () -> this.initializeWithInfo(info, refetched, true)
                );
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeWithInfo(PlayerInfo<Component> info, boolean refetched, boolean async) {
        GlobalTagAPI api = GlobalTagsAddon.getAPI();
        Consumer<Widget> addEntry = async ? this::addEntryInitialized : this::addEntry;

        Component error = this.getError(info);
        if (error != null) {
            ComponentWidget errorComponent = ComponentWidget.component(error)
                    .addId("text", "error");
            addEntry.accept(errorComponent);
        } else {
            if (refetched) {
                this.config.tag().set(info.getPlainTag());
                this.config.position().set(info.getPosition());
                this.config.icon().set(info.getGlobalIcon());
                this.config.hideRoleIcon().set(info.isRoleIconHidden());
            }
            addEntry.accept(new TagPreviewWidget(
                    this.config.tag().get(),
                    this.config.icon().get() != GlobalIcon.NONE ? Icon.url(this.getIconUrl(api, info)) : null,
                    this.config.hideRoleIcon().get() ? null : info.getRoleIcon() != null ? Icon.url(api.getUrls().getRoleIcon(info.getRoleIcon())) : null
            ));

            boolean updated = !this.config.tag().get().equals(info.getPlainTag())
                    || !this.config.position().get().equals(info.getPosition())
                    || !this.config.icon().get().equals(info.getGlobalIcon())
                    || !this.config.hideRoleIcon().get().equals(info.isRoleIconHidden());

            if (updated) {
                addEntry.accept(ComponentWidget.component(
                        Component.text("*", NamedTextColor.DARK_GRAY)
                                .hoverEvent(HoverEvent.showText(Component.translatable(
                                        "globaltags.settings.account.tagPreview.unsaved",
                                        NamedTextColor.GRAY
                                )))
                ));
            }
        }

        if (info != null && info.isBanned()) {
            ButtonWidget appealButton = ButtonWidget.i18n(
                    "globaltags.settings.account.tagPreview.appeal.name",
                    () -> new AppealPopup(api).displayInOverlay()
            ).addId("appeal-button");
            appealButton.setHoverComponent(Component.translatable(
                    "globaltags.settings.account.tagPreview.appeal.description",
                    NamedTextColor.GOLD
            ));
            appealButton.setEnabled(info.getBanInfo().isAppealable());
            addEntry.accept(appealButton);
        }
    }

    private String getIconUrl(GlobalTagAPI api, PlayerInfo<?> info) {
        return this.config.icon().get() == GlobalIcon.CUSTOM && info.getGlobalIconHash() != null
                ? api.getUrls().getCustomIcon(api.getClientUUID(), info.getGlobalIconHash())
                : api.getUrls().getDefaultIcon(this.config.icon().get());
    }

    @SuppressWarnings("ConstantConditions")
    private Component getError(PlayerInfo<Component> info) {
        String session = GlobalTagsAddon.getAPI().getAuthorization();
        if (session == null) {
            return Component.translatable("globaltags.settings.account.tagPreview.labyConnect");
        } else if (info == null) {
            return Component.translatable("globaltags.settings.account.tagPreview.noInfo");
        } else if (info.isBanned()) {
            Component banInfo = Component.empty()
                    .append(Component.translatable(
                            "globaltags.settings.account.tagPreview.reason",
                            NamedTextColor.RED,
                            Component.text(info.getBanInfo().getReason(), NamedTextColor.GRAY)
                    ));

            if (info.getBanInfo().getExpiresAt() != null) {
                banInfo
                        .append(Component.newline())
                        .append(Component.translatable(
                                "globaltags.settings.account.tagPreview.expires",
                                NamedTextColor.RED,
                                Component.text(dateFormat.format(info.getBanInfo().getExpiresAt()),
                                        NamedTextColor.GRAY)
                        ));
            }

            return Component.empty()
                    .append(Component.translatable("globaltags.settings.account.tagPreview.banned"))
                    .append(Component.space())
                    .append(Component.text("ⓘ").hoverEvent(HoverEvent.showText(banInfo)));
        }
        return null;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @SettingElement(extended = true)
    public @interface TagPreviewSetting {

    }

    @SettingFactory
    public static class Factory implements WidgetFactory<TagPreviewSetting, AccountInfoWidget> {

        @Override
        public AccountInfoWidget[] create(Setting setting, TagPreviewSetting annotation, SettingAccessor accessor) {
            if (!(accessor.config() instanceof AccountConfig config)) {
                return new AccountInfoWidget[0];
            }
            return new AccountInfoWidget[]{new AccountInfoWidget(config)};
        }

        @Override
        public Class<?>[] types() {
            return new Class[0];
        }
    }
}
