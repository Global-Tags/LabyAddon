package com.rappytv.globaltags.listener;

import com.rappytv.globaltags.util.TagCache;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;

public class ServerNavigationListener {

    @Subscribe
    public void onLeave(ServerDisconnectEvent event) {
        TagCache.clear();
    }
}
