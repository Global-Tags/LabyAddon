package com.rappytv.globaltags.listeners;

import com.rappytv.globaltags.api.GlobalTagAPI;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.session.LabyConnectDisconnectEvent;

public class LabyConnectDisconnectListener {

    private final GlobalTagAPI api;

    public LabyConnectDisconnectListener(GlobalTagAPI api) {
        this.api = api;
    }

    @Subscribe
    public void onDisconnect(LabyConnectDisconnectEvent event) {
        this.api.getCache().clear();
    }
}
