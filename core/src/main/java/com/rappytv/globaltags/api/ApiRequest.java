package com.rappytv.globaltags.api;

import com.google.gson.Gson;
import com.rappytv.globaltags.GlobalTagAddon;
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

public abstract class ApiRequest {

    private boolean successful;
    private String message;
    private String error;
    protected ResponseBody responseBody;

    public ApiRequest(String method, String path, String key) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://gt.rappytv.com" + path))
                .header("Content-Type", "application/json")
                .header("Authorization", key != null ? key : "")
                .header("X-Addon-Version", GlobalTagAddon.version)
                .method(method, getBodyPublisher())
                .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            responseBody = new Gson().fromJson(response.body(), ResponseBody.class);

            if(responseBody.error != null) {
                error = responseBody.error;
                successful = false;
                return;
            }

            this.message = responseBody.message;
            successful = true;
        } catch (IOException | InterruptedException | URISyntaxException | NullPointerException e) {
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
    public String getError() {
        return error;
    }
    private BodyPublisher getBodyPublisher() {
        if(getBody() == null) return BodyPublishers.noBody();
        return BodyPublishers.ofString(new Gson().toJson(getBody()));
    }
    public abstract RequestBody getBody();
}
