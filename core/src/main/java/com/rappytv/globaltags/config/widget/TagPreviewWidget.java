package com.rappytv.globaltags.config.widget;

import com.rappytv.globaltags.types.PlayerInfo;
import com.rappytv.globaltags.util.TagCache;
import com.rappytv.globaltags.util.Util;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.settings.Setting;
import net.labymod.api.configuration.settings.accessor.SettingAccessor;
import net.labymod.api.configuration.settings.annotation.SettingElement;
import net.labymod.api.configuration.settings.annotation.SettingFactory;
import net.labymod.api.configuration.settings.annotation.SettingWidget;
import net.labymod.api.configuration.settings.widget.WidgetFactory;
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

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);

        TagCache.resolve(Laby.labyAPI().getUniqueId(),
            (info) -> Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
                Component error = getError(info);
                if (error != null) {
                    ComponentWidget errorComponent = ComponentWidget.component(error)
                        .addId("error");
                    this.addEntry(errorComponent);
                    return;
                }
                ComponentWidget tag = ComponentWidget.component(
                    info.getTag() != null ? info.getTag() : Component.empty()).addId("tag");
                if (!info.getIconName().equals("NONE"))
                    this.addEntry(new IconWidget(info.getIcon()).addId("icon"));
                this.addEntry(tag);
                if (info.isAdmin())
                    this.addEntry(new IconWidget(adminIcon).addId("staff-icon"));
            }));
    }

    private Component getError(PlayerInfo info) {
        if(Util.getSessionToken() == null) return Component.translatable("globaltags.settings.tags.tagPreview.labyConnect");
        else if(info == null) return Component.translatable("globaltags.settings.tags.tagPreview.noInfo");
        else if(info.isBanned()) return Component.translatable(
            "globaltags.settings.tags.tagPreview.banned",
            Component.text(info.getBanReason())
        );
        return null;
    }

    @SettingFactory
    public static class Factory implements WidgetFactory<TagPreviewSetting, TagPreviewWidget> {

        @Override
        public TagPreviewWidget[] create(Setting setting, TagPreviewSetting annotation, SettingAccessor accessor) {
            return new TagPreviewWidget[]{new TagPreviewWidget()};
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
