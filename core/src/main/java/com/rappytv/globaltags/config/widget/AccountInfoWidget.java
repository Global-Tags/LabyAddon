package com.rappytv.globaltags.config.widget;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import net.labymod.api.Laby;
import net.labymod.api.Textures.SpriteCommon;
import net.labymod.api.client.component.Component;
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
import net.labymod.api.util.I18n;
import net.labymod.api.util.ThreadSafe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Consumer;

@Links({@Link("account-info.lss"), @Link("preview.lss")})
@AutoWidget
@SettingWidget
public class AccountInfoWidget extends HorizontalListWidget {

    private static boolean refetch = true;

    @Override
    public void tick() {
        super.tick();
        if(!refetch) return;
        GlobalTagAddon.getAPI().getCache().remove(GlobalTagAddon.getAPI().getClientUUID());
        this.reInitialize();
        refetch = false;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        GlobalTagAPI api = GlobalTagAddon.getAPI();

        this.addEntry(ButtonWidget
            .icon(SpriteCommon.REFRESH, AccountInfoWidget::refetch)
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

    private void initializeWithInfo(PlayerInfo<Component> info, boolean async) {
        GlobalTagAPI api = GlobalTagAddon.getAPI();
        Consumer<Widget> addEntry = async ? this::addEntryInitialized : this::addEntry;

        Component error = this.getError(info);
        if (error != null) {
            ComponentWidget errorComponent = ComponentWidget.component(error)
                .addId("error");
            addEntry.accept(errorComponent);
        } else {
            addEntry.accept(new TagPreviewWidget(
                    info.getTag(),
                    info.getGlobalIcon() != GlobalIcon.NONE ? Icon.url(this.getIconUrl(api, info)) : null,
                    info.getHighestRoleIcon() != null ? Icon.url(info.getHighestRoleIcon()) : null
            ));
        }

        if (info != null && info.isSuspended()) {
            ButtonWidget appealButton = ButtonWidget.i18n(
                "globaltags.settings.tags.tagPreview.appeal.name",
                () -> new AppealPopup(api).displayInOverlay()
            ).addId("appeal-button");
            appealButton.setHoverComponent(Component.translatable(
                "globaltags.settings.tags.tagPreview.appeal.description",
                NamedTextColor.GOLD
            ));
            appealButton.setEnabled(info.getSuspension().isAppealable());
            addEntry.accept(appealButton);
        }
    }

    public static void refetch() {
        AccountInfoWidget.refetch = true;
    }

    private String getIconUrl(GlobalTagAPI api, PlayerInfo<?> info) {
        return info.getGlobalIcon() == GlobalIcon.CUSTOM && info.getGlobalIconHash() != null
            ? api.getUrls().getCustomIcon(api.getClientUUID(), info.getGlobalIconHash())
            : api.getUrls().getDefaultIcon(info.getGlobalIcon());
    }

    private Component getError(PlayerInfo<Component> info) {
        String session = GlobalTagAddon.getAPI().getAuthorization();
        if(session == null) return Component.translatable("globaltags.settings.tags.tagPreview.labyConnect");
        else if(info == null) return Component.translatable("globaltags.settings.tags.tagPreview.noInfo");
        else if(info.isSuspended()) return Component.translatable(
            "globaltags.settings.tags.tagPreview.banned",
            Component.text(
                info.getSuspension().getReason() != null
                    ? info.getSuspension().getReason()
                    : I18n.translate("globaltags.settings.tags.tagPreview.emptyReason")
            )
        );
        return null;
    }

    @SettingFactory
    public static class Factory implements WidgetFactory<TagPreviewSetting, AccountInfoWidget> {

        @Override
        public AccountInfoWidget[] create(Setting setting, TagPreviewSetting annotation, SettingAccessor accessor) {
            return new AccountInfoWidget[]{new AccountInfoWidget()};
        }

        @Override
        public Class<?>[] types() {
            return new Class[0];
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @SettingElement(extended = true)
    public @interface TagPreviewSetting {

    }
}
