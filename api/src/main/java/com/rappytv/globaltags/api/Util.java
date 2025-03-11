package com.rappytv.globaltags.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.rappytv.globaltags.wrapper.http.ApiResponse;
import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.widget.widgets.popup.SimpleAdvancedPopup;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.Type;

public class Util {

    public static final Component unchanged = Component.translatable(
        "globaltags.settings.account.updateSettings.unchanged",
        NamedTextColor.DARK_GRAY
    );
    private static Component tagResponse = null;
    private static Component positionResponse = null;
    private static Component iconResponse = null;

    public static void update(GlobalTagAPI api, ResultType type, Component component) {
        switch (type) {
            case TAG -> tagResponse = component;
            case POSITION -> positionResponse = component;
            case ICON -> iconResponse = component;
        }
        if(tagResponse == null || positionResponse == null || iconResponse == null) return;
        SimpleAdvancedPopup popup = SimpleAdvancedPopup
            .builder()
            .title(Component.text("Update result", NamedTextColor.AQUA))
            .description(Component.translatable(
                "globaltags.settings.account.updateSettings.result",
                tagResponse,
                positionResponse,
                iconResponse
            ))
            .build();

        Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
            popup.displayInOverlay();
            api.getCache().renewSelf();
            broadcastTagUpdate();
            tagResponse = null;
            positionResponse = null;
            iconResponse = null;
        });
    }

    public enum ResultType {
        TAG,
        POSITION,
        ICON
    }

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

    public static Component getResponseComponent(ApiResponse<String> response) {
        return Component.text(
            response.isSuccessful() ? response.getData() : response.getError(),
            response.isSuccessful() ? NamedTextColor.GREEN : NamedTextColor.RED
        );
    }

    public static void broadcastTagUpdate() {
        broadcastTagUpdate(null);
    }

    public static void broadcastTagUpdate(UUID uuid) {
        JsonObject object = new JsonObject();
        if(uuid != null) object.add("uuid", new JsonPrimitive(uuid.toString()));
        LabyConnectSession session = Laby.labyAPI().labyConnect().getSession();
        if(session == null) return;
        session.sendBroadcastPayload("globaltags", object);
    }
}
