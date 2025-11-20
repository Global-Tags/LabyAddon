package com.rappytv.globaltags.core.ui.snapshot;

import com.rappytv.globaltags.core.GlobalTagsAddon;
import com.rappytv.globaltags.core.config.GlobalTagsConfig;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import com.rappytv.globaltags.wrapper.model.PlayerInfo.Cache;
import java.util.HashMap;
import java.util.Map;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.laby3d.renderer.snapshot.AbstractLabySnapshot;
import net.labymod.api.laby3d.renderer.snapshot.Extras;
import org.jetbrains.annotations.Nullable;

public class GlobalTagsUserSnapshot extends AbstractLabySnapshot {

    private static final Map<String, Icon> roleIconCache = new HashMap<>();

    private final GlobalTagsConfig config;
    private final boolean enabled;
    private final PlayerInfo<Component> playerInfo;

    public GlobalTagsUserSnapshot(Player player, Extras extras, GlobalTagsAddon addon) {
        super(extras);
        Cache<Component> cache = GlobalTagsAddon.getAPI().getCache();
        this.config = addon.configuration();
        this.enabled = this.config.enabled().get();
        this.playerInfo = cache.get(player.getUniqueId());
        if (this.playerInfo == null) {
            cache.resolve(player.getUniqueId());
        }
    }

    public boolean isAddonEnabled() {
        return this.enabled;
    }

    @Nullable
    public PlayerInfo<Component> getPlayerInfo() {
        return this.playerInfo;
    }

    @Nullable
    public Icon getStaffIcon() {
        if (this.playerInfo == null || this.playerInfo.getRoleIcon() == null) {
            return null;
        }
        return roleIconCache.computeIfAbsent(this.playerInfo.getRoleIcon(), (icon) ->
            Icon.url(GlobalTagsAddon.getAPI().getUrls().getRoleIcon(icon))
        );
    }

    public boolean isHidden() {
        if (this.playerInfo == null) {
            return true;
        }
        boolean isSelf = this.playerInfo.getUUID().equals(GlobalTagsAddon.getAPI().getClientUUID());
        boolean showOwnTag = this.config.showOwnTag().get();
        return this.config.hiddenTags().contains(this.playerInfo.getUUID())
            || (!showOwnTag && isSelf);
    }
}
