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

public class ClearTagBulletPoint implements BulletPoint {

    private final GlobalTagAPI api;

    public ClearTagBulletPoint() {
        this.api = GlobalTagAddon.getAPI();
    }

    @Override
    public Component getTitle() {
        return Component.translatable("globaltags.context.clearTag.name");
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public void execute(Player player) {
        api.getApiHandler().resetTag(player.getUniqueId(), (response) -> Laby.references().chatExecutor().displayClientMessage(
            Component.empty()
                .append(GlobalTagAddon.prefix)
                .append(Util.getResponseComponent(response))
        ));
    }

    @Override
    public boolean isVisible(Player player) {
        PlayerInfo<Component> executer = api.getCache().get(Laby.labyAPI().getUniqueId());
        PlayerInfo<Component> target = api.getCache().get(player.getUniqueId());
        return executer != null && executer.isAdmin() && target != null && target.getTag() != null;
    }
}
