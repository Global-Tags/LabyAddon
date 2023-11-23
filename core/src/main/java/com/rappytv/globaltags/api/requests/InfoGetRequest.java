package com.rappytv.globaltags.api.requests;

import com.rappytv.globaltags.api.ApiRequest;
import com.rappytv.globaltags.api.RequestBody;
import java.util.UUID;

public class InfoGetRequest extends ApiRequest {

    private String tag;
    private String position;

    public InfoGetRequest(UUID uuid, String key) {
        super("GET", "/players/" + uuid, key);
        if(isSuccessful()) {
            this.tag = responseBody.error;
            this.position = responseBody.position;
        }
    }

    @Override
    public RequestBody getBody() {
        return null;
    }

    public String getTag() {
        return tag;
    }
    public String getPosition() {
        return position;
    }
}
