package com.rappytv.globaltags.listeners;

import com.rappytv.globaltags.GlobalTagsAddon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.world.WorldEnterEvent;

public class WorldEnterListener {

    @Subscribe
    public void onWorldEnter(WorldEnterEvent event) {
        GlobalTagsAddon.getAPI().getCache().resolveSelf();
    }
}
