package com.rappytv.globaltags.interaction;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.config.GlobalTagConfig;
import com.rappytv.globaltags.ui.activities.interaction.BanActivity;
import com.rappytv.globaltags.wrapper.enums.GlobalPermission;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;

public class ToggleBanBulletPoint implements BulletPoint {

    private final GlobalTagConfig config;
    private PlayerInfo<Component> target;

    public ToggleBanBulletPoint(GlobalTagAddon addon) {
        this.config = addon.configuration();
    }

    @Override
    public Component getTitle() {
        return Component.translatable(
            "globaltags.context." + (this.target.isBanned() ? "unban" : "ban") + ".name");
    }

    @Override
    public Icon getIcon() {
        return GlobalTagAddon.roundIcon;
    }

    @Override
    public void execute(Player player) {
        if (this.target.isBanned()) {
            GlobalTagAddon.getAPI().getApiHandler().unbanPlayer(this.target.getUUID(), (response) -> {
                if(response.isSuccessful()) Util.broadcastTagUpdate(this.target.getUUID());
                Laby.references().chatExecutor().displayClientMessage(
                    Component.empty()
                        .append(GlobalTagAddon.prefix)
                        .append(Util.getResponseComponent(response))
                );
            });
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
        if(!this.config.enabled().get() || !this.config.showBulletPoints().get()) {
            return false;
        }
        PlayerInfo<Component> executor = GlobalTagAddon.getAPI().getCache().get(Laby.labyAPI().getUniqueId());
        this.target = GlobalTagAddon.getAPI().getCache().get(player.getUniqueId());
        return executor != null && executor.hasPermission(GlobalPermission.MANAGE_BANS) &&
            this.target != null;
    }
}
