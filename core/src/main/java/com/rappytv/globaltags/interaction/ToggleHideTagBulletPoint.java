package com.rappytv.globaltags.interaction;

import com.rappytv.globaltags.GlobalTagsAddon;
import com.rappytv.globaltags.api.Textures;
import com.rappytv.globaltags.config.GlobalTagsConfig;
import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;

public class ToggleHideTagBulletPoint implements BulletPoint {

    private final GlobalTagsConfig config;
    private UUID uuid;

    public ToggleHideTagBulletPoint(GlobalTagsAddon addon) {
        this.config = addon.configuration();
    }

    @Override
    public Component getTitle() {
        return Component.translatable(
            "globaltags.context." + (this.config.hiddenTags().contains(this.uuid)
                ? "showTag"
                : "hideTag") + ".name");
    }

    @Override
    public Icon getIcon() {
        return Icon.texture(Textures.ICON_ROUND);
    }

    @Override
    public void execute(Player player) {
        boolean hidden = this.config.hiddenTags().contains(this.uuid);
        if (hidden) {
            this.config.hiddenTags().remove(this.uuid);
        } else {
            this.config.hiddenTags().add(this.uuid);
        }
        Laby.references().chatExecutor().displayClientMessage(
            Component.empty()
                .append(GlobalTagsAddon.prefix())
                .append(Component.translatable(
                    "globaltags.context." + (hidden ? "showTag" : "hideTag") + ".success",
                    NamedTextColor.GRAY,
                    Component.text(player.getName(), player.gameUser().displayColor())
                ))
        );
    }

    @Override
    public boolean isVisible(Player player) {
        if (!this.config.enabled().get() || !this.config.showBulletPoints().get()) {
            return false;
        }
        this.uuid = player.getUniqueId();
        return GlobalTagsAddon.getAPI().getCache().get(player.getUniqueId()) != null;
    }
}
