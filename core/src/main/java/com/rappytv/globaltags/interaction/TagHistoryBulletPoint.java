package com.rappytv.globaltags.interaction;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.activities.TagHistoryActivity;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.wrapper.enums.GlobalPermission;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;

public class TagHistoryBulletPoint implements BulletPoint {

    private final GlobalTagAPI api;

    public TagHistoryBulletPoint() {
        this.api = GlobalTagAddon.getAPI();
    }

    @Override
    public Component getTitle() {
        return Component.translatable("globaltags.context.tag_history.name");
    }

    @Override
    public Icon getIcon() {
        return GlobalTagAddon.roundIcon;
    }

    @Override
    public void execute(Player player) {
        Laby.labyAPI().minecraft().executeNextTick(() ->
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new TagHistoryActivity(
                this.api,
                player.getUniqueId(),
                player.getName()
            ))
        );
    }

    @Override
    public boolean isVisible(Player player) {
        PlayerInfo<Component> executor = this.api.getCache().get(Laby.labyAPI().getUniqueId());
        return executor != null && executor.hasPermission(GlobalPermission.MANAGE_TAGS) &&
            this.api.getCache().get(player.getUniqueId()) != null;
    }
}
