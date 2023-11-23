package com.rappytv.globaltags.api.requests;

import com.rappytv.globaltags.api.ApiRequest;
import com.rappytv.globaltags.api.RequestBody;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.tag.PositionType;

public class PositionSetRequest extends ApiRequest {

    private final PositionType position;

    public PositionSetRequest(String key, PositionType type) {
        super("POST", "/players/" + Laby.labyAPI().getUniqueId() + "/position", key);
        this.position = type;
    }

    @Override
    public RequestBody getBody() {
        return new RequestBody(position);
    }
}
