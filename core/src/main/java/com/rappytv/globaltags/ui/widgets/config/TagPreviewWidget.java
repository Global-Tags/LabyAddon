package com.rappytv.globaltags.ui.widgets.config;

import com.rappytv.globaltags.GlobalTagsAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import java.util.Objects;
import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.util.ThreadSafe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Link("account-info.lss")
@AutoWidget
public class TagPreviewWidget extends HorizontalListWidget {

    private final GlobalTagAPI api;
    private Component tag;
    private Icon globalIcon;
    private Icon roleIcon;

    private ComponentWidget tagWidget;
    private IconWidget globalIconWidget;
    private IconWidget roleIconWidget;

    public TagPreviewWidget(@NotNull String tag, @Nullable Icon globalIcon,
        @Nullable Icon roleIcon) {
        Objects.requireNonNull(tag);
        this.api = GlobalTagsAddon.getAPI();
        this.tag = !tag.isBlank() ? this.api.translateColorCodes(tag) : null;
        this.globalIcon = globalIcon;
        this.roleIcon = roleIcon;
    }

    public TagPreviewWidget(@Nullable Component tag, @Nullable Icon globalIcon,
        @Nullable Icon roleIcon) {
        this.api = GlobalTagsAddon.getAPI();
        this.tag = tag;
        this.globalIcon = globalIcon;
        this.roleIcon = roleIcon;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        this.tagWidget = ComponentWidget.empty().addId("tag");
        this.globalIconWidget = new IconWidget(null).addId("global-icon");
        this.roleIconWidget = new IconWidget(null).addId("role-icon");
        this.updateTag(this.tag);
        this.updateGlobalIcon(this.globalIcon);
        this.updateRoleIcon(this.roleIcon);
        this.addEntry(this.globalIconWidget);
        this.addEntry(this.tagWidget);
        this.addEntry(this.roleIconWidget);
    }

    public void updateTag(@Nullable Component component) {
        this.tag = component != null
            ? component
            : Component.translatable(
                "globaltags.settings.tags.tagPreview.empty",
                NamedTextColor.RED
            );
        this.runThreadSafely(() -> this.tagWidget.setComponent(this.tag));
    }

    public void updateTag(@NotNull String tag) {
        Objects.requireNonNull(tag);
        this.updateTag(!tag.isBlank() ? this.api.translateColorCodes(tag) : null);
    }

    public void updateGlobalIcon(@Nullable Icon icon) {
        this.globalIconWidget.setVisible((this.globalIcon = icon) != null);
        this.runThreadSafely(() -> this.globalIconWidget.icon().set(this.globalIcon));
    }

    public void updateGlobalIcon(@Nullable GlobalIcon icon, @Nullable UUID uuid,
        @Nullable String hash) {
        if (icon == GlobalIcon.CUSTOM && uuid != null && hash != null) {
            this.globalIcon = Icon.url(this.api.getUrls().getCustomIcon(uuid, hash));
        } else if (icon == null) {
            this.globalIcon = null;
        } else {
            this.globalIcon = Icon.url(this.api.getUrls().getDefaultIcon(icon));
        }
        this.updateGlobalIcon(this.globalIcon);
    }

    public void updateRoleIcon(@Nullable Icon icon) {
        this.roleIconWidget.setVisible((this.roleIcon = icon) != null);
        this.runThreadSafely(() -> this.roleIconWidget.icon().set(this.roleIcon));
    }

    public void updateRoleIcon(@Nullable String role) {
        this.roleIcon = role != null ? Icon.url(this.api.getUrls().getRoleIcon(role)) : null;
        this.updateRoleIcon(this.roleIcon);
    }

    private void runThreadSafely(Runnable runnable) {
        if (ThreadSafe.isRenderThread()) {
            runnable.run();
        } else {
            Laby.labyAPI().minecraft().executeOnRenderThread(runnable);
        }
    }

}
