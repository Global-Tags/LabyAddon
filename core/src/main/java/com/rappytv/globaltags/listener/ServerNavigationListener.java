package com.rappytv.globaltags.listener;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.wrapper.model.PlayerInfo.Cache;
import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;

public class ServerNavigationListener {

    private final Cache<Component> cache;

    public ServerNavigationListener() {
        this.cache = GlobalTagAddon.getAPI().getCache();
    }

    @Subscribe
    public void onLeave(ServerDisconnectEvent event) {
        this.cache.clear();
        this.cache.resolveSelf();
    }
}
