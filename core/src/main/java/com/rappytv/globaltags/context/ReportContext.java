package com.rappytv.globaltags.context;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.util.TagCache;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.util.I18n;

public class ReportContext implements BulletPoint {

    private final GlobalTagAddon addon;

    public ReportContext(GlobalTagAddon addon) {
        this.addon = addon;
    }

    @Override
    public Component getTitle() {
        return Component.text(I18n.translate("globaltags.context.report"));
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public void execute(Player player) {
        addon.getApiHandler().reportPlayer(player.getUniqueId());
    }

    @Override
    public boolean isVisible(Player playerInfo) {
        return TagCache.get(playerInfo.getUniqueId()) != null;
    }
}
