package com.rappytv.globaltags.api;

import com.google.gson.Gson;
import com.rappytv.globaltags.GlobalTagAddon;
import net.labymod.api.util.I18n;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;

public abstract class ApiRequest {

    private final Gson gson = new Gson();
    private boolean successful;
    private String message;
    private String error;
    protected ResponseBody responseBody;

    private final String method;
    private final String path;
    private final String key;

    public ApiRequest(String method, String path, String key) {
        this.method = method;
        this.path = path;
        this.key = key;
    }

    public CompletableFuture<Void> sendAsyncRequest() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        System.out.println("Send Async");

        try {
            System.out.println("Try");
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://gt.rappytv.com" + path))
                .header("Content-Type", "application/json")
                .header("Authorization", key != null ? key : "")
                .header("X-Addon-Version", GlobalTagAddon.version)
                .method(method, getBodyPublisher())
                .build();

            HttpClient client = HttpClient.newHttpClient();
            System.out.println("1");
            client.sendAsync(request, BodyHandlers.ofString()).thenAccept(response -> {
                System.out.println("2");
                responseBody = gson.fromJson(response.body(), ResponseBody.class);

                if(responseBody.error != null) {
                    error = responseBody.error;
                    System.out.println("Error: " + error);
                    successful = false;
                    return;
                }

                if(responseBody.message != null) this.message = responseBody.message;
                successful = true;
                future.complete(null);
            });
        } catch (Exception e) {
            e.printStackTrace();
            future.complete(null);
            error = I18n.translate("globaltags.notifications.unknownError");
            successful = false;
        }

        return future;
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
        return BodyPublishers.ofString(gson.toJson(getBody()));
    }
    public abstract RequestBody getBody();
}
