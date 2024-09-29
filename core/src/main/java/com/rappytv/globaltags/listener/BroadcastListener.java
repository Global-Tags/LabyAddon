package com.rappytv.globaltags.listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rappytv.globaltags.api.GlobalTagAPI;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.session.LabyConnectBroadcastEvent;
import java.util.UUID;

public class BroadcastListener {

    private final GlobalTagAPI api;

    public BroadcastListener(GlobalTagAPI api) {
        this.api = api;
    }

    @Subscribe
    public void onBroadcastReceive(LabyConnectBroadcastEvent event) {
        if(!event.getKey().equals("globaltags")) return;
        JsonElement payload = event.getPayload();
        if(payload.isJsonObject()) {
            JsonObject object = payload.getAsJsonObject();
            if(object.has("uuid")) {
                try {
                    UUID uuid = UUID.fromString(object.get("uuid").getAsString());

                    api.getCache().renew(uuid);
                    return;
                } catch (IllegalStateException e) {}
            }
        }

        api.getCache().renew(event.getSender());
    }
}
