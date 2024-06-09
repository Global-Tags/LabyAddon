package com.rappytv.globaltags.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.labymod.api.Laby;
import net.labymod.api.util.io.web.request.Callback;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import net.labymod.api.util.io.web.request.types.GsonRequest;
import java.util.Map;

public abstract class ApiRequest {

    private static boolean localizedResponses;
    private static String version;

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
        GsonRequest<JsonObject> request = Request.ofGson(JsonObject.class)
            .url("http://localhost:5000" + path)
            .method(method)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", key != null ? key : "")
            .addHeader("X-Addon-Version", version)
            .handleErrorStream()
            .async();

        Map<String, String> body = getBody();
        if(body != null)
            request.json(body);

        if(localizedResponses)
            request.addHeader("X-Minecraft-Language", Laby.labyAPI().minecraft().options().getCurrentLanguage());

        request.execute((response) -> {
            if(response.hasException()) {
                successful = false;
                error = response.exception().getMessage();
                callback.accept(response);
                response.exception().printStackTrace();
                return;
            }
            responseBody = gson.fromJson(response.get(), ResponseBody.class);

            if(responseBody.error != null) {
                error = responseBody.error;
                successful = false;
                callback.accept(response);
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

    public static void useLocalizedResponses(boolean value) {
        localizedResponses = value;
    }
    public static void addonVersion(String value) {
        version = value;
    }
    public static String getVersion() {
        return version;
    }
}
