package com.rappytv.globaltags.interaction;

import com.rappytv.globaltags.activities.ReportUUIDActivity;
import com.rappytv.globaltags.util.TagCache;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;

public class ReportBulletPoint implements BulletPoint {

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
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new ReportUUIDActivity(player.getUniqueId(), player.getName()))
        );
    }

    @Override
    public boolean isVisible(Player playerInfo) {
        return TagCache.get(playerInfo.getUniqueId()) != null;
    }
}
