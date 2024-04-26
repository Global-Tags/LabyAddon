package com.rappytv.globaltags.api.requests;

import com.rappytv.globaltags.api.ApiRequest;
import com.rappytv.globaltags.util.GlobalIcon;
import net.labymod.api.Laby;
import net.labymod.api.util.io.web.request.Request.Method;
import java.util.HashMap;
import java.util.Map;

public class IconSetRequest extends ApiRequest {

    private final GlobalIcon icon;

    public IconSetRequest(String key, GlobalIcon icon) {
        super(Method.POST, "/players/" + Laby.labyAPI().getUniqueId() + "/icon", key);
        this.icon = icon;
    }

    @Override
    public Map<String, String> getBody() {
        Map<String, String> body = new HashMap<>();
        body.put("icon", icon.name());
        return body;
    }
}
