package com.rappytv.globaltags.interaction;

import com.rappytv.globaltags.GlobalTagsAddon;
import com.rappytv.globaltags.config.GlobalTagsConfig;
import com.rappytv.globaltags.ui.activities.interaction.ReportActivity;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;

public class ReportBulletPoint implements BulletPoint {

    private final GlobalTagsConfig config;

    public ReportBulletPoint(GlobalTagsAddon addon) {
        this.config = addon.configuration();
    }

    @Override
    public Component getTitle() {
        return Component.translatable("globaltags.context.report.name");
    }

    @Override
    public Icon getIcon() {
        return GlobalTagsAddon.roundIcon;
    }

    @Override
    public void execute(Player player) {
        Laby.labyAPI().minecraft().executeNextTick(() ->
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new ReportActivity(
                this.config,
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
        PlayerInfo<Component> playerInfo = GlobalTagsAddon.getAPI().getCache().get(player.getUniqueId());
        return playerInfo != null && playerInfo.getTag() != null;
    }
}
