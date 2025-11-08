package com.rappytv.globaltags.core.ui.snapshot;

import com.rappytv.globaltags.core.GlobalTagsAddon;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.laby3d.renderer.snapshot.Extras;
import net.labymod.api.laby3d.renderer.snapshot.LabySnapshotFactory;
import net.labymod.api.service.annotation.AutoService;

@AutoService(LabySnapshotFactory.class)
public class GlobalTagsUserSnapshotFactory extends
    LabySnapshotFactory<Player, GlobalTagsUserSnapshot> {

    private final GlobalTagsAddon addon;

    public GlobalTagsUserSnapshotFactory(GlobalTagsAddon addon) {
        super(GlobalTagsExtraKeys.GLOBALTAGS_USER);
        this.addon = addon;
    }

    @Override
    public GlobalTagsUserSnapshot create(Player player, Extras extras) {
        return new GlobalTagsUserSnapshot(player, extras, this.addon);
    }
}
