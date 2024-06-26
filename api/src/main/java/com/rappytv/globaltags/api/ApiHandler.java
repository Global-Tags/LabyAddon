package com.rappytv.globaltags.api;

import com.rappytv.globaltags.types.GlobalIcon;
import com.rappytv.globaltags.types.PlayerInfo;
import com.rappytv.globaltags.types.PlayerInfo.Suspension;
import com.rappytv.globaltags.util.TagCache;
import com.rappytv.globaltags.util.Util;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.util.io.web.request.Request.Method;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class ApiHandler {

    private ApiHandler() {}

    public static void getVersion(Consumer<String> consumer) {
        ApiRequest request = new ApiRequest(
            Method.GET,
            "/",
            null
        ) {
            @Override
            public Map<String, Object> getBody() {
                return null;
            }
        };
        request.sendAsyncRequest((response) -> consumer.accept(request.responseBody.version));
    }

    public static void getInfo(Consumer<PlayerInfo> consumer) {
        getInfo(Laby.labyAPI().getUniqueId(), consumer);
    }

    public static void getInfo(UUID uuid, Consumer<PlayerInfo> consumer) {
        ApiRequest request = new ApiRequest(
            Method.GET,
            "/players/" + uuid,
            Util.getSessionToken()
        ) {
            @Override
            public Map<String, Object> getBody() {
                return null;
            }
        };
        request.sendAsyncRequest((response) -> {
            if(!request.isSuccessful()) {
                consumer.accept(null);
                return;
            }
            consumer.accept(new PlayerInfo(
                uuid,
                request.responseBody.tag,
                request.responseBody.position,
                request.responseBody.icon,
                request.responseBody.admin,
                request.responseBody.ban
            ));
        });
    }

    public static void setTag(String tag, Consumer<ApiResponse> consumer) {
        setTag(Laby.labyAPI().getUniqueId(), tag, consumer);
    }

    public static void setTag(UUID uuid, String tag, Consumer<ApiResponse> consumer) {
        ApiRequest request = new ApiRequest(
            Method.POST,
            "/players/" + uuid,
            Util.getSessionToken()
        ) {
            @Override
            public Map<String, Object> getBody() {
                return Map.of("tag", tag);
            }
        };
        request.sendAsyncRequest((response -> {
            if(!request.isSuccessful()) {
                consumer.accept(new ApiResponse(false, request.getError()));
                return;
            }
            TagCache.clear();
            TagCache.resolveSelf((info) -> consumer.accept(new ApiResponse(true, request.getMessage())));
        }));
    }

    public static void setPosition(PositionType position, Consumer<ApiResponse> consumer) {
        setPosition(Laby.labyAPI().getUniqueId(), position, consumer);
    }

    public static void setPosition(UUID uuid, PositionType position, Consumer<ApiResponse> consumer) {
        ApiRequest request = new ApiRequest(
            Method.POST,
            "/players/" + uuid + "/position",
            Util.getSessionToken()
        ) {
            @Override
            public Map<String, Object> getBody() {
                return Map.of("position", position.name().split("_")[0]);
            }
        };
        request.sendAsyncRequest((response) -> {
            if(!request.isSuccessful()) {
                consumer.accept(new ApiResponse(false, request.getError()));
                return;
            }
            TagCache.clear();
            TagCache.resolveSelf((info) -> consumer.accept(new ApiResponse(true, request.getMessage())));
        });
    }

    public static void setIcon(GlobalIcon icon, Consumer<ApiResponse> consumer) {
        setIcon(Laby.labyAPI().getUniqueId(), icon, consumer);
    }

    public static void setIcon(UUID uuid, GlobalIcon icon, Consumer<ApiResponse> consumer) {
        ApiRequest request = new ApiRequest(
            Method.POST,
            "/players/" + uuid + "/icon",
            Util.getSessionToken()
        ) {
            @Override
            public Map<String, Object> getBody() {
                return Map.of("icon", icon.name());
            }
        };
        request.sendAsyncRequest((response) -> {
            if(!request.isSuccessful()) {
                consumer.accept(new ApiResponse(false, request.getError()));
                return;
            }
            TagCache.clear();
            TagCache.resolveSelf((info) -> consumer.accept(new ApiResponse(true, request.getMessage())));
        });
    }

    public static void resetTag(Consumer<ApiResponse> consumer) {
        resetTag(Laby.labyAPI().getUniqueId(), consumer);
    }

    public static void resetTag(UUID uuid, Consumer<ApiResponse> consumer) {
        ApiRequest request = new ApiRequest(
            Method.DELETE,
            "/players/" + uuid,
            Util.getSessionToken()
        ) {
            @Override
            public Map<String, Object> getBody() {
                // https://github.com/elysiajs/elysia/issues/495
                return Map.of("placeholder", "body");
            }
        };
        request.sendAsyncRequest((response) -> {
            if(!request.isSuccessful()) {
                consumer.accept(new ApiResponse(false, request.getError()));
                return;
            }
            TagCache.clear();
            TagCache.resolveSelf((info) -> consumer.accept(new ApiResponse(true, request.getMessage())));
        });
    }

    public static void reportPlayer(UUID uuid, String reason, Consumer<ApiResponse> consumer) {
        ApiRequest request = new ApiRequest(
            Method.POST,
            "/players/" + uuid + "/report",
            Util.getSessionToken()
        ) {
            @Override
            public Map<String, Object> getBody() {
                return Map.of("reason", reason);
            }
        };
        request.sendAsyncRequest((response) -> {
            if(!request.isSuccessful()) {
                consumer.accept(new ApiResponse(false, request.getError()));
                return;
            }
            consumer.accept(new ApiResponse(true, request.getMessage()));
        });
    }

    public static void banPlayer(UUID uuid, String reason, Consumer<ApiResponse> consumer) {
        ApiRequest request = new ApiRequest(
            Method.POST,
            "/players/" + uuid + "/ban",
            Util.getSessionToken()
        ) {
            @Override
            public Map<String, Object> getBody() {
                return Map.of("reason", reason);
            }
        };
        request.sendAsyncRequest((response) -> {
            if(!request.isSuccessful()) {
                consumer.accept(new ApiResponse(false, request.getError()));
                return;
            }
            TagCache.clear();
            TagCache.resolveSelf((info) -> consumer.accept(new ApiResponse(true, request.getMessage())));
        });
    }

    public static void unbanPlayer(UUID uuid, Consumer<ApiResponse> consumer) {
        ApiRequest request = new ApiRequest(
            Method.DELETE,
            "/players/" + uuid + "/ban",
            Util.getSessionToken()
        ) {
            @Override
            public Map<String, Object> getBody() {
                // https://github.com/elysiajs/elysia/issues/495
                return Map.of("placeholder", "body");
            }
        };
        request.sendAsyncRequest((response) -> {
            if(!request.isSuccessful()) {
                consumer.accept(new ApiResponse(false, request.getError()));
                return;
            }
            TagCache.clear();
            TagCache.resolveSelf((info) -> consumer.accept(new ApiResponse(true, request.getMessage())));
        });
    }

    public static void editBan(UUID uuid, Suspension suspension, Consumer<ApiResponse> consumer) {
        ApiRequest request = new ApiRequest(
            Method.PUT,
            "/players/" + uuid + "/ban",
            Util.getSessionToken()
        ) {
            @Override
            public Map<String, Object> getBody() {
                Objects.requireNonNull(suspension.getReason(), "Reason must not be null");
                return Map.of("reason", suspension.getReason(), "appealable", suspension.isAppealable());
            }
        };
        request.sendAsyncRequest((response) -> {
            if(!request.isSuccessful()) {
                consumer.accept(new ApiResponse(false, request.getError()));
                return;
            }
            TagCache.clear();
            TagCache.resolveSelf((info) -> consumer.accept(new ApiResponse(true, request.getMessage())));
        });
    }

    public static void appealBan(String reason, Consumer<ApiResponse> consumer) {
        ApiRequest request = new ApiRequest(
            Method.POST,
            "/players/" + Laby.labyAPI().getUniqueId() + "/ban/appeal",
            Util.getSessionToken()
        ) {
            @Override
            public Map<String, Object> getBody() {
                return Map.of("reason", reason);
            }
        };
        request.sendAsyncRequest((response) -> {
            if(!request.isSuccessful()) {
                consumer.accept(new ApiResponse(false, request.getError()));
                return;
            }
            consumer.accept(new ApiResponse(true, request.getMessage()));
        });
    }

    public static void toggleAdmin(UUID uuid, Consumer<ApiResponse> consumer) {
        ApiRequest request = new ApiRequest(
            Method.POST,
            "/players/" + uuid + "/admin",
            Util.getSessionToken()
        ) {
            @Override
            public Map<String, Object> getBody() {
                // https://github.com/elysiajs/elysia/issues/495
                return Map.of("placeholder", "body");
            }
        };
        request.sendAsyncRequest((response) -> {
            if(!request.isSuccessful()) {
                consumer.accept(new ApiResponse(false, request.getError()));
                return;
            }
            TagCache.clear();
            TagCache.resolveSelf((info) -> consumer.accept(new ApiResponse(true, request.getMessage())));
        });
    }

    public static class ApiResponse {

        private final boolean successful;
        private final Component message;

        public ApiResponse(boolean successful, String message) {
            this.successful = successful;
            this.message = Component.text(message, successful ? NamedTextColor.GREEN : NamedTextColor.RED);
        }

        public boolean isSuccessful() {
            return successful;
        }

        public Component getMessage() {
            return message;
        }
    }
}
