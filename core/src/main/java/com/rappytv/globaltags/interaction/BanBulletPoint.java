package com.rappytv.globaltags.interaction;

import com.rappytv.globaltags.activities.BanUUIDActivity;
import com.rappytv.globaltags.types.PlayerInfo;
import com.rappytv.globaltags.util.TagCache;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;

public class BanBulletPoint implements BulletPoint {

    @Override
    public Component getTitle() {
        return Component.translatable("globaltags.context.ban.name");
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public void execute(Player player) {
        Laby.labyAPI().minecraft().executeNextTick(() ->
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new BanUUIDActivity(player.getUniqueId(), player.getName()))
        );
    }

    @Override
    public boolean isVisible(Player player) {
        PlayerInfo executer = TagCache.get(Laby.labyAPI().getUniqueId());
        PlayerInfo banned = TagCache.get(player.getUniqueId());
        return executer != null && executer.isAdmin() && banned != null && banned.getTag() != null;
    }
}
