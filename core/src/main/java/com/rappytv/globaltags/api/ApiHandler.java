package com.rappytv.globaltags.api;

import com.rappytv.globaltags.api.RequestBody.StringType;
import com.rappytv.globaltags.api.requests.IconSetRequest;
import com.rappytv.globaltags.api.requests.PositionSetRequest;
import com.rappytv.globaltags.api.requests.TagSetRequest;
import com.rappytv.globaltags.util.GlobalIcon;
import com.rappytv.globaltags.util.Util;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.util.I18n;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class ApiHandler {

    public void setTag(String tag) {
        TagSetRequest request = new TagSetRequest(
            Util.getSessionToken(),
            tag
        );
        request.sendAsyncRequest().thenRun(() -> {
            if(!request.isSuccessful()) {
                Util.notify(I18n.translate("globaltags.notifications.error"), request.getError());
                return;
            }
            Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage());
            Util.clearCache(false);
        }).exceptionally((e) -> {
            Util.notify(I18n.translate("globaltags.notifications.error"), e.getMessage());
            return null;
        });
    }

    public void setPosition(PositionType position) {
        PositionSetRequest request = new PositionSetRequest(
            Util.getSessionToken(),
            position
        );
        request.sendAsyncRequest().thenRun(() -> {
            if(!request.isSuccessful()) {
                Util.notify(I18n.translate("globaltags.notifications.error"), request.getError());
                return;
            }
            Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage());
            Util.clearCache(false);
        }).exceptionally((e) -> {
            Util.notify(I18n.translate("globaltags.notifications.error"), e.getMessage());
            return null;
        });
    }

    public void setIcon(GlobalIcon icon) {
        IconSetRequest request = new IconSetRequest(
            Util.getSessionToken(),
            icon
        );
        request.sendAsyncRequest().thenRun(() -> {
            if(!request.isSuccessful()) {
                Util.notify(I18n.translate("globaltags.notifications.error"), request.getError());
                return;
            }
            Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage());
            Util.clearCache(false);
        }).exceptionally((e) -> {
            Util.notify(I18n.translate("globaltags.notifications.error"), e.getMessage());
            return null;
        });
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
        request.sendAsyncRequest().thenRun(() -> {
            if(!request.isSuccessful()) {
                Util.notify(I18n.translate("globaltags.notifications.error"), request.getError());
                return;
            }
            Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage());
            Util.clearCache(false);
        }).exceptionally((e) -> {
            Util.notify(I18n.translate("globaltags.notifications.error"), e.getMessage());
            return null;
        });
    }

    public void reportPlayer(UUID uuid, String reason) {
        ApiRequest request = new ApiRequest(
            "POST",
            "/players/" + uuid + "/report",
            Util.getSessionToken()
        ) {
            @Override
            public RequestBody getBody() {
                return new RequestBody(reason, StringType.REPORT_REASON);
            }
        };
        request.sendAsyncRequest().thenRun(() -> {
            if(!request.isSuccessful()) {
                Util.notify(I18n.translate("globaltags.notifications.error"), request.getError());
                return;
            }
            Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage());
        }).exceptionally((e) -> {
            Util.notify(I18n.translate("globaltags.notifications.error"), e.getMessage());
            return null;
        });
    }
}
