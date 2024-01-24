package com.rappytv.globaltags.api.requests;

import com.rappytv.globaltags.api.ApiRequest;
import com.rappytv.globaltags.api.RequestBody;
import com.rappytv.globaltags.util.GlobalIcon;
import net.labymod.api.Laby;

public class IconSetRequest extends ApiRequest {

    private final GlobalIcon icon;

    public IconSetRequest(String key, GlobalIcon icon) {
        super("POST", "/players/" + Laby.labyAPI().getUniqueId() + "/icon", key);
        this.icon = icon;
    }

    @Override
    public RequestBody getBody() {
        return new RequestBody(icon);
    }
}
