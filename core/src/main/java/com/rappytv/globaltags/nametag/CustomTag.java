package com.rappytv.globaltags.nametag;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.util.PlayerInfo;
import com.rappytv.globaltags.util.TagCache;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.render.font.RenderableComponent;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public class CustomTag extends NameTag {

    private final GlobalTagAddon addon;

    public CustomTag(GlobalTagAddon addon) {
        this.addon = addon;
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

        PlayerInfo info;
        if(TagCache.has(uuid))
            info = TagCache.get(uuid);
        else {
            info = addon.getApiHandler().getInfo(uuid);
            TagCache.add(uuid, info);
        }
        if(info.getTag() == null) return null;
        if(!addon.configuration().position().get().equals(info.getPosition())) return null;

        return RenderableComponent.of(Component.text(
            info.getTag().replace('&', '§')
        ));
    }
}
