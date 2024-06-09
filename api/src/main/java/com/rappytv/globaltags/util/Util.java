package com.rappytv.globaltags.util;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.TokenStorage.Purpose;
import net.labymod.api.labyconnect.TokenStorage.Token;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.Type;
import net.labymod.api.util.I18n;
import org.jetbrains.annotations.Nullable;

public class Util {

    public static void notify(String title, String text) {
        Notification.builder()
            .title(net.labymod.api.client.component.Component.text(title))
            .text(net.labymod.api.client.component.Component.text(text))
            .type(Type.SOCIAL)
            .buildAndPush();
    }

    public static Component translateColorCodes(String string) {
        if(string == null) return null;
        return LegacyComponentSerializer
            .legacyAmpersand()
            .deserialize(string);
    }

    public static boolean clearCache(boolean notify) {
        if(TagCache.isEmpty()) {
            if(notify) Util.notify(
                I18n.translate("globaltags.notifications.error"),
                I18n.translate("globaltags.notifications.cacheEmpty")
            );
            return false;
        }
        TagCache.clear();
        TagCache.resolve(Laby.labyAPI().getUniqueId());
        return true;
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
