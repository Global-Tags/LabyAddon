package com.rappytv.globaltags.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.rappytv.globaltags.wrapper.http.ApiResponse;
import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.screen.widget.widgets.popup.SimpleAdvancedPopup;
import net.labymod.api.client.gui.screen.widget.widgets.popup.SimpleAdvancedPopup.SimplePopupButton;
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
    private static Component roleIconVisibilityResponse = null;

    public static void update(GlobalTagAPI api, ResultType type, Component component) {
        System.out.println("Update " + type.name());
        switch (type) {
            case TAG -> tagResponse = component;
            case POSITION -> positionResponse = component;
            case ICON -> iconResponse = component;
            case ROLE_ICON_VISIBILITY -> roleIconVisibilityResponse = component;
        }
        if (tagResponse == null
            || positionResponse == null
            || iconResponse == null
            || roleIconVisibilityResponse == null) {
            return;
        }
        SimpleAdvancedPopup popup = SimpleAdvancedPopup
            .builder()
            .title(Component.translatable(
                "globaltags.settings.account.updateSettings.result",
                NamedTextColor.AQUA
            ))
            .description(
                Component.empty()
                    .append(Component.translatable(
                        "globaltags.settings.account.updateSettings.tag",
                        tagResponse
                    ))
                    .append(Component.newline())
                    .append(Component.translatable(
                        "globaltags.settings.account.updateSettings.position",
                        positionResponse
                    ))
                    .append(Component.newline())
                    .append(Component.translatable(
                        "globaltags.settings.account.updateSettings.icon",
                        iconResponse
                    ))
                    .append(Component.newline())
                    .append(Component.translatable(
                        "globaltags.settings.account.updateSettings.roleIconVisibility",
                        roleIconVisibilityResponse
                    ))
            )
            .addButton(SimplePopupButton.confirm())
            .build();

        Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
            popup.displayInOverlay();
            api.getCache().renewSelf();
            broadcastTagUpdate();
            tagResponse = null;
            positionResponse = null;
            iconResponse = null;
            roleIconVisibilityResponse = null;
        });
    }

    public static void sendResponseNotification(ApiResponse<String> response) {
        notify(
            Component.translatable(
                "globaltags.general." + (response.isSuccessful() ? "success" : "error")),
            Component.text(response.isSuccessful() ? response.getData() : response.getError())
        );
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

    public enum ResultType {
        TAG,
        POSITION,
        ICON,
        ROLE_ICON_VISIBILITY
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
