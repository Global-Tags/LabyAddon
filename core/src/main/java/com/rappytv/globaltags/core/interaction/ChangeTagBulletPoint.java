package com.rappytv.globaltags.core.interaction;

import com.rappytv.globaltags.api.Textures;
import com.rappytv.globaltags.core.GlobalTagsAddon;
import com.rappytv.globaltags.core.config.GlobalTagsConfig;
import com.rappytv.globaltags.core.ui.activities.interaction.ChangeTagActivity;
import com.rappytv.globaltags.wrapper.enums.GlobalPermission;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;

public class ChangeTagBulletPoint implements BulletPoint {

    private final GlobalTagsConfig config;

    public ChangeTagBulletPoint(GlobalTagsAddon addon) {
        this.config = addon.configuration();
    }

    @Override
    public Component getTitle() {
        return Component.translatable("globaltags.context.changeTag.name");
    }

    @Override
    public Icon getIcon() {
        return Icon.texture(Textures.ICON_ROUND);
    }

    @Override
    public void execute(Player player) {
        Laby.labyAPI().minecraft().executeNextTick(() ->
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new ChangeTagActivity(
                player.getUniqueId(),
                player.getName()
            ))
        );
    }

    @Override
    public boolean isVisible(Player player) {
        if(!this.config.enabled().get() || !this.config.showBulletPoints().get()) {
            return false;
        }
        PlayerInfo<?> executor = GlobalTagsAddon.getAPI().getCache().get(Laby.labyAPI().getUniqueId());
        PlayerInfo<?> target = GlobalTagsAddon.getAPI().getCache().get(player.getUniqueId());
        return executor != null && executor.hasPermission(GlobalPermission.MANAGE_TAGS)
            && target != null;
    }
}
