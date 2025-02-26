package com.rappytv.globaltags.listeners;

import com.rappytv.globaltags.GlobalTagAddon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.world.WorldEnterEvent;

public class WorldEnterListener {

    @Subscribe
    public void onWorldEnter(WorldEnterEvent event) {
        GlobalTagAddon.getAPI().getCache().resolveSelf();
    }
}
