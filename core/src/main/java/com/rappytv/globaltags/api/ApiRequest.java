package com.rappytv.globaltags.api;

import com.google.gson.Gson;
import net.labymod.api.util.I18n;
import org.jetbrains.annotations.Nullable;
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
    private String error;
    private String version;

    public ApiRequest(String method, String path, String key, @Nullable String tag) {
        Gson gson = new Gson();

        try {
            BodyPublisher bodyPublisher = tag == null ?
                BodyPublishers.noBody() :
                BodyPublishers.ofString(gson.toJson(new RequestBody(tag)));

            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://gt.rappytv.com" + path))
                .header("Content-Type", "application/json")
                .header("Authorization", key)
                .method(method, bodyPublisher)
                .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            ResponseBody responseBody = gson.fromJson(response.body(), ResponseBody.class);

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
    public String getError() {
        return error;
    }
    public String getVersion() {
        return version;
    }
}
