package com.rappytv.globaltags.api.requests;

import com.rappytv.globaltags.api.ApiRequest;
import net.labymod.api.Laby;
import net.labymod.api.util.io.web.request.Request.Method;
import java.util.HashMap;
import java.util.Map;

public class TagSetRequest extends ApiRequest {

    private final String tag;

    public TagSetRequest(String token, String tag) {
        super(Method.POST, "/players/" + Laby.labyAPI().getUniqueId(), token);
        this.tag = tag;
    }

    @Override
    public Map<String, String> getBody() {
        Map<String, String> body = new HashMap<>();
        body.put("tag", tag);
        return body;
    }
}
