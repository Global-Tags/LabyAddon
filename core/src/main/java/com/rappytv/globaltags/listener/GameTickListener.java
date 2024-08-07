package com.rappytv.globaltags.listener;

import com.rappytv.globaltags.nametag.CustomTag;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;

public class GameTickListener {

    @Subscribe
    public void onTick(GameTickEvent event) {
        if(event.phase() != Phase.POST) return;
        CustomTag.ticks++;
    }
}
