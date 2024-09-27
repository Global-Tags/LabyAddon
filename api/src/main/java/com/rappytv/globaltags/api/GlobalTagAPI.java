package com.rappytv.globaltags.api;

import com.rappytv.globaltags.wrapper.GlobalTagsAPI;
import com.rappytv.globaltags.wrapper.enums.AuthProvider;
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

public class GlobalTagAPI extends GlobalTagsAPI<Component> {

    private final Agent agent;
    private final Supplier<String> language;

    public GlobalTagAPI(Agent agent, Supplier<String> language) {
        this.agent = agent;
        this.language = language;
    }

    @Override
    public @NotNull Agent getAgent() {
        return agent;
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
        return Laby.labyAPI().getUniqueId();
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
