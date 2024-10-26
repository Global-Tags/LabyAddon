package com.rappytv.globaltags.config.widget;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.config.subconfig.TagSubConfig;
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
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.accessor.SettingAccessor;
import net.labymod.api.configuration.settings.annotation.SettingElement;
import net.labymod.api.configuration.settings.annotation.SettingFactory;
import net.labymod.api.configuration.settings.annotation.SettingWidget;
import net.labymod.api.configuration.settings.widget.WidgetFactory;
import net.labymod.api.util.I18n;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Link("preview.lss")
@AutoWidget
@SettingWidget
public class TagPreviewWidget extends HorizontalListWidget {

    private static boolean refetch = true;
    private static boolean changed = false;
    private final TagSubConfig config;

    private TagPreviewWidget(TagSubConfig config) {
        this.config = config;
    }

    @Override
    public void tick() {
        super.tick();
        if(!refetch && !changed) return;
        if(refetch)
            GlobalTagAddon.getAPI().getCache().remove(GlobalTagAddon.getAPI().getClientUUID());
        reInitialize();
        refetch = false;
        changed = false;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        initialize(refetch);
    }

    @SuppressWarnings("ConstantConditions")
    public void initialize(boolean refetched) {
        GlobalTagAPI api = GlobalTagAddon.getAPI();
        api.getCache().resolveSelf((info) ->
            Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
                Component error = getError(info);
                if (error != null) {
                    ComponentWidget errorComponent = ComponentWidget.component(error)
                        .addId("text", "error");
                    this.addEntryInitialized(errorComponent);
                } else {
                    if(refetched) {
                        config.tag().set(info.getPlainTag());
                        config.position().set(info.getPosition());
                        config.icon().set(info.getGlobalIcon());
                    }
                    boolean updated = !config.tag().get().equals(info.getPlainTag())
                        || !config.position().get().equals(info.getPosition())
                        || !config.icon().get().equals(info.getGlobalIcon());
                    if(changed && updated) {
                        Util.notify(
                            I18n.translate("globaltags.settings.tags.staged.title"),
                            I18n.translate("globaltags.settings.tags.staged.description")
                        );
                    }
                    ComponentWidget tag = ComponentWidget.component(
                        config.tag().get().isBlank()
                            ? Component.translatable(
                                "globaltags.settings.tags.tagPreview.empty",
                                NamedTextColor.RED
                            )
                            : api.translateColorCodes(config.tag().get())
                    ).addId("text");

                    if (config.icon().get() != GlobalIcon.NONE) {
                        String iconUrl = config.icon().get() == GlobalIcon.CUSTOM && info.getGlobalIconHash() != null
                            ? api.getUrls().getCustomIcon(api.getClientUUID(), info.getGlobalIconHash())
                            : api.getUrls().getDefaultIcon(config.icon().get());
                        this.addEntryInitialized(
                            new IconWidget(Icon.url(iconUrl))
                                .addId("icon")
                        );
                    }
                    this.addEntryInitialized(tag);
                    if (info.getHighestRoleIcon() != null)
                        this.addEntryInitialized(
                            new IconWidget(Icon.url(info.getHighestRoleIcon()))
                                .addId("staff-icon")
                        );
                }
                ButtonWidget refreshButton = ButtonWidget.icon(SpriteCommon.REFRESH, TagPreviewWidget::refetch)
                    .addId("refresh-button");
                addEntryInitialized(refreshButton);
                if(info != null && info.isSuspended()) {
                    ButtonWidget appealButton = ButtonWidget.i18n(
                        "globaltags.settings.tags.tagPreview.appeal.name",
                        () -> new AppealPopup(api).displayInOverlay()
                    ).addId("appeal-button");
                    appealButton.setHoverComponent(Component.translatable(
                        "globaltags.settings.tags.tagPreview.appeal.description",
                        NamedTextColor.GOLD
                    ));
                    appealButton.setEnabled(info.getSuspension().isAppealable());
                    addEntryInitialized(appealButton);
                }
            }));
    }

    public static void change() {
        TagPreviewWidget.changed = true;
    }

    public static void refetch() {
        refetch = true;
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
    public static class Factory implements WidgetFactory<TagPreviewSetting, TagPreviewWidget> {

        @Override
        public TagPreviewWidget[] create(Setting setting, TagPreviewSetting annotation, SettingAccessor accessor) {
            return new TagPreviewWidget[]{new TagPreviewWidget((TagSubConfig) accessor.config())};
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
