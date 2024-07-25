package com.rappytv.globaltags.interaction;

import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;

public class ReferPlayerBulletPoint implements BulletPoint {

    private final GlobalTagAPI api;

    public ReferPlayerBulletPoint() {
        this.api = GlobalTagAddon.getAPI();
    }

    @Override
    public Component getTitle() {
        return Component.translatable("globaltags.context.referral.name");
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public void execute(Player player) {
        api.getApiHandler().referPlayer(player.getUniqueId(), (response) -> Laby.references().chatExecutor().displayClientMessage(
            Component.empty()
                .append(GlobalTagAddon.prefix)
                .append(Util.getResponseComponent(response))
        ));
    }

    @Override
    public boolean isVisible(Player player) {
        PlayerInfo<Component> executer = api.getCache().get(Laby.labyAPI().getUniqueId());
        return executer != null && !executer.hasReferred();
    }
}
