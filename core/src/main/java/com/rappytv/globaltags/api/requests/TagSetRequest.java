package com.rappytv.globaltags.api.requests;

import com.rappytv.globaltags.api.ApiRequest;
import com.rappytv.globaltags.api.RequestBody;
import net.labymod.api.Laby;

public class TagSetRequest extends ApiRequest {

    private final String key;

    public TagSetRequest(String token, String key) {
        super("POST", "/players/" + Laby.labyAPI().getUniqueId(), token);
        this.key = key;
    }

    @Override
    public RequestBody getBody() {
        return new RequestBody(key);
    }
}
