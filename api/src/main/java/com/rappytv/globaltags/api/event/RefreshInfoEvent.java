package com.rappytv.globaltags.api.event;

import net.labymod.api.event.Event;

public record RefreshInfoEvent(boolean refetch) implements Event {

    public RefreshInfoEvent() {
        this(false);
    }
}
