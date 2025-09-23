package com.rappytv.globaltags.interaction;

import com.rappytv.globaltags.GlobalTagsAddon;
import com.rappytv.globaltags.api.Textures;
import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.config.GlobalTagsConfig;
import com.rappytv.globaltags.wrapper.enums.GlobalPermission;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.interaction.BulletPoint;
import net.labymod.api.client.gui.icon.Icon;

public class ClearTagBulletPoint implements BulletPoint {

    private final GlobalTagsConfig config;

    public ClearTagBulletPoint(GlobalTagsAddon addon) {
        this.config = addon.configuration();
    }

    @Override
    public Component getTitle() {
        return Component.translatable("globaltags.context.clearTag.name");
    }

    @Override
    public Icon getIcon() {
        return Icon.texture(Textures.ICON_ROUND);
    }

    @Override
    public void execute(Player player) {
        GlobalTagsAddon.getAPI().getApiHandler().resetTag(player.getUniqueId(), (response) -> Laby.references().chatExecutor().displayClientMessage(
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
        PlayerInfo<?> executor = GlobalTagsAddon.getAPI().getCache().get(Laby.labyAPI().getUniqueId());
        PlayerInfo<?> target = GlobalTagsAddon.getAPI().getCache().get(player.getUniqueId());
        return executor != null && executor.hasPermission(GlobalPermission.MANAGE_TAGS)
            && target != null && target.getTag() != null;
    }
}
