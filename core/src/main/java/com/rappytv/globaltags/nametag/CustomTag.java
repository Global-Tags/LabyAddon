package com.rappytv.globaltags.nametag;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.requests.InfoGetRequest;
import com.rappytv.globaltags.util.PlayerInfo;
import com.rappytv.globaltags.util.TagCache;
import com.rappytv.globaltags.util.Util;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.render.font.RenderableComponent;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public class CustomTag extends NameTag {

    private final GlobalTagAddon addon;
    private final PositionType position;

    public CustomTag(GlobalTagAddon addon, PositionType position) {
        this.addon = addon;
        this.position = position;
    }

    @Override
    public float getScale() {
        return (float) addon.configuration().tagSize().get() / 10;
    }

    @Override
    protected @Nullable RenderableComponent getRenderableComponent() {
        if(!addon.configuration().enabled().get()) return null;
        if(entity == null || !(entity instanceof Player)) return null;
        UUID uuid = entity.getUniqueId();
        if(!addon.configuration().showOwnTag().get() && Laby.labyAPI().getUniqueId().equals(uuid)) return null;

        PlayerInfo info = null;
        if(TagCache.has(uuid))
            info = TagCache.get(uuid);
        else {
            InfoGetRequest request = new InfoGetRequest(uuid, Util.getSessionToken());
            request.sendAsyncRequest().thenRun(() ->
                TagCache.add(uuid, new PlayerInfo(request.getTag(), request.getPosition()))
            );
        }
        if(info == null || info.getTag() == null) return null;
        if(!position.equals(info.getPosition())) return null;

        return RenderableComponent.of(Component.text(
            info.getTag().replace('&', '§')
        ));
    }

    @Override
    public boolean isVisible() {
        return !this.entity.isCrouching() && super.isVisible();
    }
}
