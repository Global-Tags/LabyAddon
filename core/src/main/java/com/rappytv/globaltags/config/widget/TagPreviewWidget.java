package com.rappytv.globaltags.config.widget;

import com.rappytv.globaltags.config.subconfig.TagSubConfig;
import com.rappytv.globaltags.types.GlobalIcon;
import com.rappytv.globaltags.types.PlayerInfo;
import com.rappytv.globaltags.util.TagCache;
import com.rappytv.globaltags.util.Util;
import net.labymod.api.Laby;
import net.labymod.api.Textures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.size.SizeType;
import net.labymod.api.client.gui.screen.widget.size.WidgetSide;
import net.labymod.api.client.gui.screen.widget.size.WidgetSize;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.client.resources.ResourceLocation;
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

    private final Icon adminIcon = Icon.texture(ResourceLocation.create(
        "globaltags",
        "textures/icons/staff.png"
    ));
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
            TagCache.remove(Laby.labyAPI().getUniqueId());
        reInitialize();
        refetch = false;
        changed = false;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        initialize(refetch);
    }

    public void initialize(boolean refetched) {
        TagCache.resolve(Laby.labyAPI().getUniqueId(), (info) ->
            Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
                Component error = getError(info);
                if (error != null) {
                    ComponentWidget errorComponent = ComponentWidget.component(error)
                        .addId("text", "error");
                    errorComponent.setSize(SizeType.MAX, WidgetSide.WIDTH, WidgetSize.fixed(500));
                    this.addEntry(errorComponent);
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
                            : Util.translateColorCodes(config.tag().get())
                    ).addId("text");
                    if (config.icon().get() != GlobalIcon.NONE)
                        this.addEntry(new IconWidget(config.icon().get().getIcon()).addId("icon"));
                    this.addEntry(tag);
                    if (info.isAdmin())
                        this.addEntry(new IconWidget(adminIcon).addId("staff-icon"));
                }
                ButtonWidget refreshButton = ButtonWidget.icon(SpriteCommon.REFRESH, this::refetch)
                    .addId("refresh-button");
                addEntry(refreshButton);
                if(info.isSuspended()) {
                    // TODO: Implement appeal functionality
                    ButtonWidget appealButton = ButtonWidget.i18n(
                        "globaltags.settings.tags.tagPreview.appeal.name",
                        () -> {}
                    ).addId("appeal-button");
                    appealButton.setHoverComponent(Component.translatable(
                        "globaltags.settings.tags.tagPreview.appeal.description",
                        NamedTextColor.GOLD
                    ));
                    appealButton.setEnabled(info.getSuspension().isAppealable());
                    addEntry(appealButton);
                }
            }));
    }

    public static void change() {
        TagPreviewWidget.changed = true;
    }

    public void refetch() {
        refetch = true;
    }

    private Component getError(PlayerInfo info) {
        String session = Util.getSessionToken();
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
