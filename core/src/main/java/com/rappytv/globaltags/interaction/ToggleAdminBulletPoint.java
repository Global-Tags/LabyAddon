package com.rappytv.globaltags.interaction;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.ApiHandler;
import com.rappytv.globaltags.types.PlayerInfo;
import com.rappytv.globaltags.util.TagCache;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;

public class ToggleAdminBulletPoint implements BulletPoint {

    @Override
    public Component getTitle() {
        return Component.translatable("globaltags.context.toggleAdmin.name");
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public void execute(Player player) {
        ApiHandler.toggleAdmin(player.getUniqueId(), (response) -> Laby.references().chatExecutor().displayClientMessage(
            Component
                .text(GlobalTagAddon.prefix)
                .append(response.getMessage())
        ));
    }

    @Override
    public boolean isVisible(Player player) {
        PlayerInfo executer = TagCache.get(Laby.labyAPI().getUniqueId());
        PlayerInfo target = TagCache.get(player.getUniqueId());
        return executer != null && executer.isAdmin() && target != null && target.getTag() != null;
    }
}
