package com.rappytv.globaltags.core.listeners;

import com.rappytv.globaltags.api.GlobalTagAPI;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.LabyConnectStateUpdateEvent;
import net.labymod.api.event.labymod.labyconnect.session.LabyConnectDisconnectEvent;
import net.labymod.api.labyconnect.protocol.LabyConnectState;

public class LabyConnectDisconnectListener {

    private final GlobalTagAPI api;

    public LabyConnectDisconnectListener(GlobalTagAPI api) {
        this.api = api;
    }

    @Subscribe
    public void onConnect(LabyConnectStateUpdateEvent event) {
        if (event.state() == LabyConnectState.PLAY) {
            this.api.getCache().renewSelf();
        }
    }

    @Subscribe
    public void onDisconnect(LabyConnectDisconnectEvent event) {
        this.api.getCache().clear();
    }
}
