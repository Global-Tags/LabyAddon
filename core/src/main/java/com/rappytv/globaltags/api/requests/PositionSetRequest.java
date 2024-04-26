package com.rappytv.globaltags.api.requests;

import com.rappytv.globaltags.api.ApiRequest;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.util.io.web.request.Request.Method;
import java.util.HashMap;
import java.util.Map;

public class PositionSetRequest extends ApiRequest {

    private final PositionType position;

    public PositionSetRequest(String key, PositionType type) {
        super(Method.POST, "/players/" + Laby.labyAPI().getUniqueId() + "/position", key);
        this.position = type;
    }

    @Override
    public Map<String, String> getBody() {
        Map<String, String> body = new HashMap<>();
        body.put("position", position.name().split("_")[0]);
        return body;
    }
}
