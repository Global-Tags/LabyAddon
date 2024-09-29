package com.rappytv.globaltags.interaction;

import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.activities.BanActivity;
import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.wrapper.enums.GlobalPermission;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;

public class ToggleBanBulletPoint implements BulletPoint {

    private final GlobalTagAPI api;
    private PlayerInfo<Component> target;

    public ToggleBanBulletPoint() {
        this.api = GlobalTagAddon.getAPI();
    }

    @Override
    public Component getTitle() {
        return Component.translatable("globaltags.context." + (target.isSuspended() ? "unban" : "ban") + ".name");
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public void execute(Player player) {
        if(target.isSuspended()) {
            api.getApiHandler().unbanPlayer(target.getUUID(), (response) -> {
                if(response.successful()) Util.broadcastTagUpdate(target.getUUID());
                Laby.references().chatExecutor().displayClientMessage(
                    Component.empty()
                        .append(GlobalTagAddon.prefix)
                        .append(Util.getResponseComponent(response))
                );
            });
        } else {
            Laby.labyAPI().minecraft().executeNextTick(() ->
                Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new BanActivity(
                    api,
                    player.getUniqueId(),
                    player.getName()
                ))
            );
        }
    }

    @Override
    public boolean isVisible(Player player) {
        PlayerInfo<Component> executer = api.getCache().get(Laby.labyAPI().getUniqueId());
        target = api.getCache().get(player.getUniqueId());
        return executer != null && executer.hasPermission(GlobalPermission.MANAGE_BANS) && target != null;
    }
}
