package com.rappytv.globaltags.api.requests;

import com.google.gson.JsonObject;
import com.rappytv.globaltags.api.ApiRequest;
import net.labymod.api.util.io.web.request.Callback;
import net.labymod.api.util.io.web.request.Request.Method;
import java.util.Map;

public class VersionGetRequest extends ApiRequest {

    private String version;

    public VersionGetRequest() {
        super(Method.GET, "/", null);
    }

    @Override
    public void sendAsyncRequest(Callback<JsonObject> callback) {
        super.sendAsyncRequest((response) -> {
            if(isSuccessful()) version = responseBody.version;
        });
    }

    @Override
    public Map<String, String> getBody() {
        return null;
    }

    public String getVersion() {
        return version;
    }
}
