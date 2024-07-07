package com.rappytv.globaltags.util;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer;
import net.labymod.api.client.gui.screen.widget.widgets.popup.SimpleAdvancedPopup;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.TokenStorage.Purpose;
import net.labymod.api.labyconnect.TokenStorage.Token;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Util {

    public static final Component unchanged = Component.translatable(
        "globaltags.settings.tags.updateSettings.unchanged",
        NamedTextColor.DARK_GRAY
    );
    private static Component tagResponse = null;
    private static Component fontResponse = null;
    private static Component positionResponse = null;
    private static Component iconResponse = null;

    public static void notify(String title, String text) {
        notify(
            Component.text(title),
            Component.text(text)
        );
    }

    public static void notify(Component title, Component description) {
        Notification.builder()
            .title(title)
            .text(description)
            .type(Type.SYSTEM)
            .buildAndPush();
    }

    public static void update(ResultType type, Component component) {
        switch (type) {
            case TAG -> tagResponse = component;
            case FONT -> fontResponse = component;
            case POSITION -> positionResponse = component;
            case ICON -> iconResponse = component;
        }
        if(tagResponse == null || positionResponse == null || iconResponse == null) return;
        SimpleAdvancedPopup popup = SimpleAdvancedPopup
            .builder()
            .title(Component.text("Update result", NamedTextColor.AQUA))
            .description(Component.translatable(
                "globaltags.settings.tags.updateSettings.result",
                tagResponse,
                fontResponse,
                positionResponse,
                iconResponse
            ))
            .build();

        Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
            popup.displayInOverlay();
            TagCache.clear();
            TagCache.resolveSelf();
            tagResponse = null;
            fontResponse = null;
            positionResponse = null;
            iconResponse = null;
        });
    }

    public enum ResultType {
        TAG,
        FONT,
        POSITION,
        ICON
    }

    @NotNull
    public static Component translateColorCodes(String string) {
        if(string == null) string = "";
        return LegacyComponentSerializer
            .legacyAmpersand()
            .deserialize(string);
    }

    public static @Nullable String getSessionToken() {
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
