package com.rappytv.globaltags.core.ui.snapshot;

import com.rappytv.globaltags.core.GlobalTagsAddon;
import com.rappytv.globaltags.core.config.GlobalTagsConfig;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import com.rappytv.globaltags.wrapper.model.PlayerInfo.Cache;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.laby3d.renderer.snapshot.AbstractLabySnapshot;
import net.labymod.api.laby3d.renderer.snapshot.Extras;
import org.jetbrains.annotations.Nullable;

public class GlobalTagsUserSnapshot extends AbstractLabySnapshot {

    private final GlobalTagsConfig config;
    private final PlayerInfo<Component> playerInfo;
    private final Icon staffIcon;

    public GlobalTagsUserSnapshot(Player player, Extras extras, GlobalTagsAddon addon) {
        super(extras);
        this.config = addon.configuration();
        Cache<Component> cache = GlobalTagsAddon.getAPI().getCache();
        if (cache.has(player.getUniqueId())) {
            this.playerInfo = cache.get(player.getUniqueId());
        } else {
            this.playerInfo = null;
            cache.resolve(player.getUniqueId());
        }
        this.staffIcon = this.playerInfo != null && this.playerInfo.getRoleIcon() != null
            ? Icon.url(
            GlobalTagsAddon.getAPI()
                .getUrls()
                .getRoleIcon(this.playerInfo.getRoleIcon())
        )
            : null;
    }

    @Nullable
    public PlayerInfo<Component> getPlayerInfo() {
        return this.playerInfo;
    }

    @Nullable
    public Icon getStaffIcon() {
        return this.staffIcon;
    }

    public boolean passedSelfCheck() {
        return this.playerInfo != null && (this.config.showOwnTag().get()
            || !this.playerInfo.getUUID().equals(GlobalTagsAddon.getAPI().getClientUUID()));
    }

    public boolean isHidden() {
        return this.config.hiddenTags().contains(this.playerInfo.getUUID());
    }
}
