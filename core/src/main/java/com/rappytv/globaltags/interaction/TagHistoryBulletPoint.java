package com.rappytv.globaltags.interaction;

import com.rappytv.globaltags.GlobalTagsAddon;
import com.rappytv.globaltags.config.GlobalTagsConfig;
import com.rappytv.globaltags.ui.activities.interaction.TagHistoryActivity;
import com.rappytv.globaltags.wrapper.enums.GlobalPermission;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;

public class TagHistoryBulletPoint implements BulletPoint {

    private final GlobalTagsConfig config;

    public TagHistoryBulletPoint(GlobalTagsAddon addon) {
        this.config = addon.configuration();
    }

    @Override
    public Component getTitle() {
        return Component.translatable("globaltags.context.tag_history.name");
    }

    @Override
    public Icon getIcon() {
        return GlobalTagsAddon.roundIcon;
    }

    @Override
    public void execute(Player player) {
        Laby.labyAPI().minecraft().executeNextTick(() ->
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new TagHistoryActivity(
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
        PlayerInfo<Component> executor = GlobalTagsAddon.getAPI().getCache().get(Laby.labyAPI().getUniqueId());
        return executor != null && executor.hasPermission(GlobalPermission.MANAGE_TAGS) &&
            GlobalTagsAddon.getAPI().getCache().get(player.getUniqueId()) != null;
    }
}
