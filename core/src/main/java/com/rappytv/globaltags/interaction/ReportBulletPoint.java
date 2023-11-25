package com.rappytv.globaltags.interaction;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.activities.ReportUUIDActivity;
import com.rappytv.globaltags.util.PlayerInfo;
import com.rappytv.globaltags.util.TagCache;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;

public class ReportBulletPoint implements BulletPoint {

    private final GlobalTagAddon addon;

    public ReportBulletPoint(GlobalTagAddon addon) {
        this.addon = addon;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("globaltags.context.report");
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public void execute(Player player) {
        Laby.labyAPI().minecraft().executeNextTick(() ->
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new ReportUUIDActivity(addon, player.getUniqueId(), player.getName()))
        );
    }

    @Override
    public boolean isVisible(Player player) {
        PlayerInfo playerInfo = TagCache.get(player.getUniqueId());
        return playerInfo != null && playerInfo.getTag() != null;
    }
}
