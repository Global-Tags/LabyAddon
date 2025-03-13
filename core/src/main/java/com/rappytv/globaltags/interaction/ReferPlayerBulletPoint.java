package com.rappytv.globaltags.interaction;

import com.rappytv.globaltags.GlobalTagsAddon;
import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.config.GlobalTagsConfig;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;

public class ReferPlayerBulletPoint implements BulletPoint {

    private final GlobalTagsConfig config;

    public ReferPlayerBulletPoint(GlobalTagsAddon addon) {
        this.config = addon.configuration();
    }

    @Override
    public Component getTitle() {
        return Component.translatable("globaltags.context.referral.name");
    }

    @Override
    public Icon getIcon() {
        return GlobalTagsAddon.roundIcon;
    }

    @Override
    public void execute(Player player) {
        GlobalTagsAddon.getAPI().getApiHandler().referPlayer(player.getUniqueId(), (response) -> Laby.references().chatExecutor().displayClientMessage(
            Component.empty()
                .append(GlobalTagsAddon.prefix)
                .append(Util.getResponseComponent(response))
        ));
    }

    @Override
    public boolean isVisible(Player player) {
        if(!this.config.enabled().get() || !this.config.showBulletPoints().get()) {
            return false;
        }
        PlayerInfo<Component> executor = GlobalTagsAddon.getAPI().getCache().get(Laby.labyAPI().getUniqueId());
        PlayerInfo<Component> target = GlobalTagsAddon.getAPI().getCache().get(player.getUniqueId());
        return (executor == null || !executor.hasReferred()) && target != null;
    }
}
