package com.rappytv.globaltags.api;

import com.google.gson.Gson;
import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.config.GlobalTagConfig;
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

        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://gt.rappytv.com" + path))
                .header("Content-Type", "application/json")
                .header("Authorization", key != null ? key : "")
                .header("X-Addon-Version", GlobalTagAddon.version)
                .method(method, getBodyPublisher())
                .build();

            HttpClient client = HttpClient.newHttpClient();
            client.sendAsync(request, BodyHandlers.ofString()).thenAccept(response -> {
                responseBody = gson.fromJson(response.body(), ResponseBody.class);

                if(responseBody.error != null) {
                    error = responseBody.error;
                    successful = false;
                    future.complete(null);
                    return;
                }

                if(responseBody.message != null) this.message = responseBody.message;
                successful = true;
                future.complete(null);
            }).exceptionally((e) -> handleException(e, future));
        } catch (Exception e) {
            handleException(e, future);
        }

        return future;
    }

    private Void handleException(Throwable e, CompletableFuture<Void> future) {
        e.printStackTrace();
        future.complete(null);
        error = GlobalTagConfig.exceptions ? e.getMessage() : I18n.translate("globaltags.notifications.unknownError");
        successful = false;
        return null;
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
