package com.rappytv.globaltags.api;

import com.rappytv.globaltags.util.PlayerInfo;
import com.rappytv.globaltags.util.Util;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.util.I18n;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class ApiHandler {

    public String getApiVersion() {
        ApiRequest request = new ApiRequest(
            "GET",
            "/",
            ""
        );

        return request.getVersion();
    }

    public PlayerInfo getInfo(UUID uuid) {
        ApiRequest request = new ApiRequest(
            "GET",
            "/players/" + uuid,
            Util.getSessionToken()
        );

        return new PlayerInfo(request.getTag(), request.getPosition());
    }

    public void setTag(String tag) {
        ApiRequest request = new ApiRequest(
            "POST",
            "/players/" + Laby.labyAPI().getUniqueId(),
            Util.getSessionToken(),
            tag
        );

        if(!request.isSuccessful()) {
            Util.notify(I18n.translate("globaltags.notifications.error"), request.getError(), true);
            return;
        }
        Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage(), false);
    }

    public void setPosition(PositionType position) {
        ApiRequest request = new ApiRequest(
            "POST",
            "/players/" + Laby.labyAPI().getUniqueId() + "/position",
            Util.getSessionToken(),
            position
        );

        if(!request.isSuccessful()) {
            Util.notify(I18n.translate("globaltags.notifications.error"), request.getError(), true);
            return;
        }
        Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage(), false);
    }

    public void resetTag() {
        ApiRequest request = new ApiRequest(
            "DELETE",
            "/players/" + Laby.labyAPI().getUniqueId(),
            Util.getSessionToken()
        );

        if(!request.isSuccessful()) {
            Util.notify(I18n.translate("globaltags.notifications.error"), request.getError(), true);
            return;
        }
        Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage(), false);
    }

    public void reportPlayer(UUID uuid) {
        ApiRequest request = new ApiRequest(
            "POST",
            "/players/" + uuid + "/report",
            Util.getSessionToken()
        );

        if(!request.isSuccessful()) {
            Util.notify(I18n.translate("globaltags.notifications.error"), request.getError(), true);
            return;
        }
        Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage(), false);
    }
}
