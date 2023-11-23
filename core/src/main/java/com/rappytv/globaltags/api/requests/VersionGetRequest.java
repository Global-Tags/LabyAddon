package com.rappytv.globaltags.api.requests;

import com.rappytv.globaltags.api.ApiRequest;
import com.rappytv.globaltags.api.RequestBody;
import java.util.concurrent.CompletableFuture;

public class VersionGetRequest extends ApiRequest {

    private String version;

    public VersionGetRequest() {
        super("GET", "/", null);
    }

    @Override
    public CompletableFuture<Void> sendAsyncRequest() {
        CompletableFuture<Void> future = super.sendAsyncRequest();
        if(isSuccessful()) version = responseBody.version;
        return future;
    }

    @Override
    public RequestBody getBody() {
        return null;
    }

    public String getVersion() {
        return version;
    }
}
