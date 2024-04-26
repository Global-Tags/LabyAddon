package com.rappytv.globaltags.api.requests;

import com.google.gson.JsonObject;
import com.rappytv.globaltags.api.ApiRequest;
import net.labymod.api.util.io.web.request.Callback;
import net.labymod.api.util.io.web.request.Request.Method;
import java.util.Map;
import java.util.UUID;

public class InfoGetRequest extends ApiRequest {

    private String tag;
    private String position;
    private String icon;
    private boolean admin;

    public InfoGetRequest(UUID uuid, String key) {
        super(Method.GET, "/players/" + uuid, key);
    }

    @Override
    public void sendAsyncRequest(Callback<JsonObject> callback) {
        super.sendAsyncRequest((response) -> {
            if(isSuccessful()) {
                this.tag = responseBody.tag;
                this.position = responseBody.position;
                this.icon = responseBody.icon;
                this.admin = responseBody.admin;
            }
        });
    }

    @Override
    public Map<String, String> getBody() {
        return null;
    }

    public String getTag() {
        return tag;
    }
    public String getPosition() {
        return position;
    }
    public String getIcon() {
        return icon;
    }
    public boolean isAdmin() {
        return admin;
    }
}
