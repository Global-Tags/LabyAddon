package com.rappytv.globaltags.interaction;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.activities.ReportUUIDActivity;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;

public class ReportBulletPoint implements BulletPoint {

    private final GlobalTagAPI api;

    public ReportBulletPoint() {
        this.api = GlobalTagAddon.getAPI();
    }

    @Override
    public Component getTitle() {
        return Component.translatable("globaltags.context.report.name");
    }

    @Override
    public Icon getIcon() {
        return GlobalTagAddon.roundIcon;
    }

    @Override
    public void execute(Player player) {
        Laby.labyAPI().minecraft().executeNextTick(() ->
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new ReportUUIDActivity(
                this.api,
                player.getUniqueId(),
                player.getName()
            ))
        );
    }

    @Override
    public boolean isVisible(Player player) {
        PlayerInfo<Component> playerInfo = this.api.getCache().get(player.getUniqueId());
        return playerInfo != null && playerInfo.getTag() != null;
    }
}
