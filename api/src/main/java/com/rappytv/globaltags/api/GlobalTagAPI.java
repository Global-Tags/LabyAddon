package com.rappytv.globaltags.api;

import com.rappytv.globaltags.wrapper.GlobalTagsAPI;
import com.rappytv.globaltags.wrapper.enums.AuthProvider;
import com.rappytv.globaltags.wrapper.http.ApiHandler;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import com.rappytv.globaltags.wrapper.model.PlayerInfo.Cache;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.TokenStorage.Purpose;
import net.labymod.api.labyconnect.TokenStorage.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;
import java.util.function.Supplier;

public class GlobalTagAPI implements GlobalTagsAPI<Component> {

    private final Cache<Component> cache = new Cache<>(this);
    private final ApiHandler<Component> apiHandler = new ApiHandler<>(this);

    private final Supplier<String> addonVersion;
    private final Supplier<String> language;

    public GlobalTagAPI(Supplier<String> addonVersion, Supplier<String> language) {
        this.addonVersion = addonVersion;
        this.language = language;
    }

    @Override
    public @NotNull String getApiBase() {
        return "https://gt.rappytv.com";
    }

    @Override
    public @NotNull String getAgent() {
        return "LabyAddon v" + addonVersion.get();
    }

    @Override
    public @NotNull String getLanguageCode() {
        return language.get();
    }

    @Override
    public @NotNull Component translateColorCodes(@Nullable String string) {
        if(string == null) string = "";
        return LegacyComponentSerializer
            .legacyAmpersand()
            .deserialize(string);
    }

    @Override
    public @Nullable UUID getClientUUID() {
        return null;
    }

    @NotNull
    @Override
    public PlayerInfo.Cache<Component> getCache() {
        return cache;
    }

    @Override
    public @NotNull ApiHandler<Component> getApiHandler() {
        return apiHandler;
    }

    @Override
    public @NotNull AuthProvider getAuthType() {
        return AuthProvider.LABYCONNECT;
    }

    @Override
    public @Nullable String getAuthorization() {
        LabyConnectSession session = Laby.labyAPI().labyConnect().getSession();
        if(session == null) return null;

        Token token = session.tokenStorage().getToken(
            Purpose.JWT,
            session.self().getUniqueId()
        );

        if(token == null || token.isExpired()) return null;

        return token.getToken();
    }
}
