package com.rappytv.globaltags.nametag;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.util.TagCache;
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
    private final PositionType positionType;

    public CustomTag(GlobalTagAddon addon, PositionType positionType) {
        this.addon = addon;
        this.positionType = positionType;
    }

    @Override
    public float getScale() {
        return addon.configuration().tagSize().get();
    }

    @Override
    protected @Nullable RenderableComponent getRenderableComponent() {
        if(!addon.configuration().enabled().get()) return null;
        if(entity == null || !(entity instanceof Player)) return null;
        if(!addon.configuration().position().get().equals(positionType)) return null;
        UUID uuid = entity.getUniqueId();
        if(!addon.configuration().showOwnTag().get() && Laby.labyAPI().getUniqueId().equals(uuid)) return null;

        String tag;
        if(TagCache.has(uuid))
            tag = TagCache.get(uuid);
        else {
            tag = addon.getApiHandler().getTag(uuid);
            if(tag == null) return null;
            TagCache.add(uuid, tag);
        }

        return RenderableComponent.of(Component.text(
            tag.replace('&', '§')
        ));
    }
}
