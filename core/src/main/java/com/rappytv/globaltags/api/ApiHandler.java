package com.rappytv.globaltags.api;

import com.rappytv.globaltags.api.requests.IconSetRequest;
import com.rappytv.globaltags.api.requests.PositionSetRequest;
import com.rappytv.globaltags.api.requests.TagSetRequest;
import com.rappytv.globaltags.util.GlobalIcon;
import com.rappytv.globaltags.util.Util;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.util.I18n;
import net.labymod.api.util.io.web.request.Request.Method;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class ApiHandler {

    public void setTag(String tag) {
        TagSetRequest request = new TagSetRequest(
            Util.getSessionToken(),
            tag
        );
        request.sendAsyncRequest((response -> {
            if(!request.isSuccessful()) {
                Util.notify(I18n.translate("globaltags.notifications.error"), request.getError());
                return;
            }
            Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage());
            Util.clearCache(false);
        }));
    }

    public void setPosition(PositionType position) {
        PositionSetRequest request = new PositionSetRequest(
            Util.getSessionToken(),
            position
        );
        request.sendAsyncRequest((response -> {
            if(!request.isSuccessful()) {
                Util.notify(I18n.translate("globaltags.notifications.error"), request.getError());
                return;
            }
            Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage());
            Util.clearCache(false);
        }));
    }

    public void setIcon(GlobalIcon icon) {
        IconSetRequest request = new IconSetRequest(
            Util.getSessionToken(),
            icon
        );
        request.sendAsyncRequest((response -> {
            if(!request.isSuccessful()) {
                Util.notify(I18n.translate("globaltags.notifications.error"), request.getError());
                return;
            }
            Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage());
            Util.clearCache(false);
        }));
    }

    public void resetTag() {
        ApiRequest request = new ApiRequest(
            Method.DELETE,
            "/players/" + Laby.labyAPI().getUniqueId(),
            Util.getSessionToken()
        ) {
            @Override
            public Map<String, String> getBody() {
                return null;
            }
        };
        request.sendAsyncRequest((response)-> {
            if(!request.isSuccessful()) {
                Util.notify(I18n.translate("globaltags.notifications.error"), request.getError());
                return;
            }
            Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage());
            Util.clearCache(false);
        });
    }

    public void reportPlayer(UUID uuid, String reason) {
        ApiRequest request = new ApiRequest(
            Method.POST,
            "/players/" + uuid + "/report",
            Util.getSessionToken()
        ) {
            @Override
            public Map<String, String> getBody() {
                Map<String, String> body = new HashMap<>();
                body.put("reason", reason);
                return body;
            }
        };
        request.sendAsyncRequest((response) -> {
            if(!request.isSuccessful()) {
                Util.notify(I18n.translate("globaltags.notifications.error"), request.getError());
                return;
            }
            Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage());
        });
    }

    public void banPlayer(UUID uuid, String reason) {
        ApiRequest request = new ApiRequest(
            Method.POST,
            "/players/" + uuid + "/ban",
            Util.getSessionToken()
        ) {
            @Override
            public Map<String, String> getBody() {
                Map<String, String> body = new HashMap<>();
                body.put("reason", reason);
                return body;
            }
        };
        request.sendAsyncRequest((response) -> {
            if(!request.isSuccessful()) {
                Util.notify(I18n.translate("globaltags.notifications.error"), request.getError());
                return;
            }
            Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage());
        });
    }
}
