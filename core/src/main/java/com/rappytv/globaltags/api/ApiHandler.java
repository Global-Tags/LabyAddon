package com.rappytv.globaltags.api;

import com.rappytv.globaltags.api.requests.InfoGetRequest;
import com.rappytv.globaltags.api.requests.PositionSetRequest;
import com.rappytv.globaltags.api.requests.TagSetRequest;
import com.rappytv.globaltags.api.requests.VersionGetRequest;
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
        VersionGetRequest request = new VersionGetRequest();

        return request.getVersion();
    }

    public PlayerInfo getInfo(UUID uuid) {
        InfoGetRequest request = new InfoGetRequest(
            uuid,
            Util.getSessionToken()
        );

        return new PlayerInfo(request.getTag(), request.getPosition());
    }

    public void setTag(String tag) {
        TagSetRequest request = new TagSetRequest(
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
        PositionSetRequest request = new PositionSetRequest(
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
        ) {
            @Override
            public RequestBody getBody() {
                return null;
            }
        };

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
        ) {
            @Override
            public RequestBody getBody() {
                return null;
            }
        };

        if(!request.isSuccessful()) {
            Util.notify(I18n.translate("globaltags.notifications.error"), request.getError(), true);
            return;
        }
        Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage(), false);
    }
}
