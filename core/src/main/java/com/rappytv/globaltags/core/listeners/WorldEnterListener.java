package com.rappytv.globaltags.core.listeners;

import com.rappytv.globaltags.core.GlobalTagsAddon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.world.WorldEnterEvent;

public class WorldEnterListener {

    @Subscribe
    public void onWorldEnter(WorldEnterEvent event) {
        GlobalTagsAddon.getAPI().getCache().renewSelf();
    }
}
