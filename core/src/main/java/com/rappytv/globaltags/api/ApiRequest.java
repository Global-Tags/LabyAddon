package com.rappytv.globaltags.api;

import com.google.gson.Gson;
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
import java.util.UUID;

public class ApiRequest {

    private boolean successful;
    private String message;
    private String tag;
    private String error;

    public ApiRequest(String method, String key, UUID uuid, @Nullable String tag, String additionalPath) {
        Gson gson = new Gson();

        try {
            BodyPublisher bodyPublisher = tag == null ?
                BodyPublishers.noBody() :
                BodyPublishers.ofString(gson.toJson(new RequestBody(tag)));

            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/players/" + uuid + "/" + additionalPath))
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

            this.message = responseBody.message;
            this.tag = responseBody.tag;
            successful = true;
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
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
}
