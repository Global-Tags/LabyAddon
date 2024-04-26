package com.rappytv.globaltags.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.config.GlobalTagConfig;
import net.labymod.api.util.I18n;
import net.labymod.api.util.io.web.request.Callback;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class ApiRequest {

    private final Gson gson = new Gson();
    private boolean successful;
    private String message;
    private String error;
    protected ResponseBody responseBody;

    private final Method method;
    private final String path;
    private final String key;

    public ApiRequest(Method method, String path, String key) {
        this.method = method;
        this.path = path;
        this.key = key;
    }

    public void sendAsyncRequest(Callback<JsonObject> callback) {
        Request.ofGson(JsonObject.class)
            .url("https://gt.rappytv.com" + path)
            .method(method)
            .body(getBody() != null ? getBody() : new HashMap<>())
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", key != null ? key : "")
            .addHeader("X-Addon-Version", GlobalTagAddon.version)
            .async()
            .execute((response) -> {
                responseBody = gson.fromJson(response.get(), ResponseBody.class);

                if(responseBody.error != null) {
                    error = responseBody.error;
                    successful = false;
                    return;
                }

                if(responseBody.message != null) this.message = responseBody.message;
                successful = true;
                callback.accept(response);
            });
    }

    public boolean isSuccessful() {
        return successful;
    }
    public String getMessage() {
        return message;
    }
    public String getError() {
        return error;
    }
    public abstract Map<String, String> getBody();
}
