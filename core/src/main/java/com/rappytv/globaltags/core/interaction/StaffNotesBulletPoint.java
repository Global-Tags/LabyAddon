package com.rappytv.globaltags.core.interaction;

import com.rappytv.globaltags.api.Textures;
import com.rappytv.globaltags.core.GlobalTagsAddon;
import com.rappytv.globaltags.core.config.GlobalTagsConfig;
import com.rappytv.globaltags.core.ui.activities.interaction.StaffNotesActivity;
import com.rappytv.globaltags.wrapper.enums.GlobalPermission;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;

public class StaffNotesBulletPoint implements BulletPoint {

    private final GlobalTagsConfig config;

    public StaffNotesBulletPoint(GlobalTagsAddon addon) {
        this.config = addon.configuration();
    }

    @Override
    public Component getTitle() {
        return Component.translatable("globaltags.context.staffNotes.name");
    }

    @Override
    public Icon getIcon() {
        return Icon.texture(Textures.ICON_ROUND);
    }

    @Override
    public void execute(Player player) {
        Laby.labyAPI().minecraft().executeNextTick(() ->
            Laby.labyAPI().minecraft().minecraftWindow().displayScreen(new StaffNotesActivity(
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
        PlayerInfo<Component> executor = GlobalTagsAddon.getAPI().getCache().get(Laby.labyAPI().getUniqueId());
        return executor != null && executor.hasPermission(GlobalPermission.MANAGE_NOTES) &&
            GlobalTagsAddon.getAPI().getCache().get(player.getUniqueId()) != null;
    }
}
