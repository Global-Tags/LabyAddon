package com.rappytv.globaltags.api.requests;

import com.rappytv.globaltags.api.ApiRequest;
import com.rappytv.globaltags.api.RequestBody;

public class VersionGetRequest extends ApiRequest {

    private String version;

    public VersionGetRequest() {
        super("GET", "/", null);

        if(isSuccessful()) version = responseBody.version;
    }

    @Override
    public RequestBody getBody() {
        return null;
    }

    public String getVersion() {
        return version;
    }
}
