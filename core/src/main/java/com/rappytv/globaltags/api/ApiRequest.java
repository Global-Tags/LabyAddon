package com.rappytv.globaltags.api;

import com.google.gson.Gson;
import com.rappytv.globaltags.GlobalTagAddon;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.util.I18n;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class ApiRequest {

    private boolean successful;
    private String message;
    private String tag;
    private String position;
    private String error;
    private String version;

    public ApiRequest(String method, String path, String key) {
        this(method, path, key, BodyPublishers.noBody());
    }
    public ApiRequest(String method, String path, String key, String tag) {
        this(method, path, key, BodyPublishers.ofString(new Gson().toJson(new RequestBody(tag))));
    }
    public ApiRequest(String method, String path, String key, PositionType type) {
        this(method, path, key, BodyPublishers.ofString(new Gson().toJson(new RequestBody(type))));
    }

    private ApiRequest(String method, String path, String key, BodyPublisher bodyPublisher) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://gt.rappytv.com" + path))
                .header("Content-Type", "application/json")
                .header("Authorization", key)
                .header("X-Addon-Version", GlobalTagAddon.version)
                .method(method, bodyPublisher)
                .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            ResponseBody responseBody = new Gson().fromJson(response.body(), ResponseBody.class);

            if(responseBody.error != null) {
                error = responseBody.error;
                successful = false;
                return;
            }
            if(responseBody.version != null) {
                version = responseBody.version;
                successful = true;
                return;
            }

            this.message = responseBody.message;
            this.tag = responseBody.tag;
            this.position = responseBody.position;
            successful = true;
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
            error = I18n.translate("globaltags.notifications.unknownError");
            successful = false;
        }
    }

    public boolean isSuccessful() {
        return successful;
    }
    public String getMessage() {
        return message;
    }
    public String getTag() {
        return tag;
    }
    public String getPosition() {
        return position;
    }
    public String getError() {
        return error;
    }
    public String getVersion() {
        return version;
    }
}
