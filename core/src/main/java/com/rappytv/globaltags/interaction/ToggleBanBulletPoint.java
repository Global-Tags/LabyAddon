package com.rappytv.globaltags.interaction;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.activities.BanActivity;
import com.rappytv.globaltags.api.ApiHandler;
import com.rappytv.globaltags.types.PlayerInfo;
import com.rappytv.globaltags.util.TagCache;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;

public class ToggleBanBulletPoint implements BulletPoint {

    private PlayerInfo target;

    @Override
    public Component getTitle() {
        return Component.translatable("globaltags.context." + (target.isBanned() ? "unban" : "ban") + ".name");
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public void execute(Player player) {
        if(target.isBanned()) {
            ApiHandler.unbanPlayer(target.getUUID(), (response) -> Laby.references().chatExecutor().displayClientMessage(
                Component
                    .text(GlobalTagAddon.prefix)
                    .append(response.getMessage())
            ));
        } else {
            Laby.labyAPI().minecraft().executeNextTick(() ->
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new BanActivity(
                    player.getUniqueId(),
                    player.getName()
                ))
            );
        }
    }

    @Override
    public boolean isVisible(Player player) {
        PlayerInfo executer = TagCache.get(Laby.labyAPI().getUniqueId());
        target = TagCache.get(player.getUniqueId());
        return executer != null && executer.isAdmin() && target != null;
    }
}
