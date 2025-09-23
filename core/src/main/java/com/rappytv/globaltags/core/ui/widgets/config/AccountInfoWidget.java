package com.rappytv.globaltags.core.ui.widgets.config;

import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.api.event.RefreshInfoEvent;
import com.rappytv.globaltags.core.GlobalTagsAddon;
import com.rappytv.globaltags.core.config.subconfig.AccountConfig;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;
import java.util.function.Consumer;
import net.labymod.api.Laby;
import net.labymod.api.Textures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
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
import net.labymod.api.event.Subscribe;
import net.labymod.api.util.ThreadSafe;

@Link("account-info.lss")
@AutoWidget
@SettingWidget
public class AccountInfoWidget extends HorizontalListWidget {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private final AccountConfig config;
    private boolean reset = true;

    private AccountInfoWidget(AccountConfig config) {
        this.config = config;
        Laby.labyAPI().eventBus().registerListener(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        GlobalTagAPI api = GlobalTagsAddon.getAPI();

        this.addEntry(
            ButtonWidget
                .icon(SpriteCommon.REFRESH, () -> Laby.fireEvent(new RefreshInfoEvent(true)))
                .addId("refresh-button")
        );
        api.getCache().resolveSelf((info) -> {
            if (ThreadSafe.isRenderThread()) {
                this.initializeWithInfo(info, false);
            } else {
                Laby.labyAPI().minecraft().executeOnRenderThread(
                    () -> this.initializeWithInfo(info, true)
                );
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeWithInfo(PlayerInfo<Component> info, boolean async) {
        GlobalTagAPI api = GlobalTagsAddon.getAPI();
        Consumer<Widget> addEntry = async ? this::addEntryInitialized : this::addEntry;

        Component error = this.getError(info);
        if (error != null) {
            ComponentWidget errorComponent = ComponentWidget.component(error)
                    .addId("text", "error");
            addEntry.accept(errorComponent);
        } else {
            if (this.reset) {
                this.config.tag().set(info.getPlainTag());
                this.config.position().set(info.getPosition());
                this.config.icon().set(info.getGlobalIcon());
                this.config.hideRoleIcon().set(info.isRoleIconHidden());
                this.reset = false;
            }
            String iconUrl = TagPreviewWidget.getIconUrl(info, this.config.icon().get());
            addEntry.accept(new TagPreviewWidget(
                this.config.tag().get(),
                iconUrl != null ? Icon.url(iconUrl) : null,
                this.config.hideRoleIcon().get()
                    ? null
                    : info.getRoleIcon() != null
                        ? Icon.url(api.getUrls().getRoleIcon(info.getRoleIcon()))
                        : null
            ));

            boolean updated = !this.config.tag().get().equals(info.getPlainTag())
                    || !this.config.position().get().equals(info.getPosition())
                    || !this.config.icon().get().equals(info.getGlobalIcon())
                    || !this.config.hideRoleIcon().get().equals(info.isRoleIconHidden());

            if (updated) {
                addEntry.accept(ComponentWidget.component(
                        Component.text("*", NamedTextColor.DARK_GRAY)
                                .hoverEvent(HoverEvent.showText(Component.translatable(
                                    "globaltags.settings.account.accountInfo.unsaved",
                                        NamedTextColor.GRAY
                                )))
                ));
            }
        }

        if (info != null && info.isBanned()) {
            ButtonWidget appealButton = ButtonWidget.i18n(
                "globaltags.settings.account.accountInfo.appeal.name",
                    () -> new AppealPopup(api).displayInOverlay()
            ).addId("appeal-button");
            appealButton.setHoverComponent(Component.translatable(
                "globaltags.settings.account.accountInfo.appeal.description",
                    NamedTextColor.GOLD
            ));
            appealButton.setEnabled(info.getBanInfo().isAppealable());
            addEntry.accept(appealButton);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private Component getError(PlayerInfo<Component> info) {
        String session = GlobalTagsAddon.getAPI().getAuthorization();
        if (session == null) {
            return Component.translatable("globaltags.settings.account.accountInfo.labyConnect");
        } else if (info == null) {
            return Component.translatable("globaltags.settings.account.accountInfo.noInfo");
        } else if (info.isBanned()) {
            Component banInfo = Component.empty()
                    .append(Component.translatable(
                        "globaltags.settings.account.accountInfo.reason",
                            NamedTextColor.RED,
                            Component.text(info.getBanInfo().getReason(), NamedTextColor.GRAY)
                    ));

            if (info.getBanInfo().getExpiresAt() != null) {
                banInfo
                        .append(Component.newline())
                        .append(Component.translatable(
                            "globaltags.settings.account.accountInfo.expires",
                                NamedTextColor.RED,
                                Component.text(dateFormat.format(info.getBanInfo().getExpiresAt()),
                                        NamedTextColor.GRAY)
                        ));
            }

            return Component.empty()
                .append(Component.translatable("globaltags.settings.account.accountInfo.banned"))
                    .append(Component.space())
                    .append(Component.text("ⓘ").hoverEvent(HoverEvent.showText(banInfo)));
        }
        return null;
    }

    @Subscribe
    public void onRefresh(RefreshInfoEvent event) {
        if (event.refetch()) {
            GlobalTagsAddon.getAPI().getCache().removeSelf();
            this.reset = true;
        }
        if (ThreadSafe.isRenderThread()) {
            this.reInitialize();
        } else {
            Laby.labyAPI().minecraft().executeOnRenderThread(this::reInitialize);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        Laby.labyAPI().eventBus().unregisterListener(this);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @SettingElement(extended = true)
    public @interface AccountInfoSetting {

    }

    @SettingFactory
    public static class Factory implements WidgetFactory<AccountInfoSetting, AccountInfoWidget> {

        @Override
        public AccountInfoWidget[] create(Setting setting, AccountInfoSetting annotation,
            SettingAccessor accessor) {
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
