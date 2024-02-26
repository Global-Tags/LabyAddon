package com.rappytv.globaltags.nametag;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.requests.InfoGetRequest;
import com.rappytv.globaltags.config.GlobalTagConfig;
import com.rappytv.globaltags.util.PlayerInfo;
import com.rappytv.globaltags.util.TagCache;
import com.rappytv.globaltags.util.Util;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.entity.player.tag.tags.NameTagBackground;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class CustomTag extends NameTag {

    private final Icon admin;
    private final int black = new Color(0, 0, 0, 70).getRGB();
    private final GlobalTagConfig config;
    private final PositionType position;
    private final Set<UUID> resolving = new HashSet<>();
    private PlayerInfo info;

    public CustomTag(GlobalTagAddon addon, PositionType position) {
        this.config = addon.configuration();
        this.position = position;
        admin = Icon.texture(ResourceLocation.create(
            "globaltags",
            "textures/icons/staff.png"
        ));
    }

    @Override
    public float getScale() {
        return (float) config.tagSize().get() / 10;
    }

    @Override
    protected @Nullable RenderableComponent getRenderableComponent() {
        if(!config.enabled().get()) return null;
        if(entity == null || !(entity instanceof Player)) return null;
        UUID uuid = entity.getUniqueId();
        if(!config.showOwnTag().get() && Laby.labyAPI().getUniqueId().equals(uuid))
            return null;

        info = null;
        if(TagCache.has(uuid))
            info = TagCache.get(uuid);
        else {
            if(position == PositionType.ABOVE_NAME && !resolving.contains(uuid)) {
                resolving.add(uuid);
                InfoGetRequest request = new InfoGetRequest(uuid, Util.getSessionToken());
                request.sendAsyncRequest().thenRun(() -> {
                    TagCache.add(uuid, new PlayerInfo(
                        translateColorCodes(request.getTag()),
                        request.getPosition(),
                        request.getIcon(),
                        request.isAdmin()
                    ));
                    resolving.remove(uuid);
                });
            }
        }
        if(info == null || info.getTag() == null) return null;
        if(!position.equals(info.getPosition())) return null;

        return RenderableComponent.of(info.getTag());
    }

    @Override
    public void render(Stack stack, Entity entity) {
        super.render(stack, entity);
        if(this.getRenderableComponent() == null) return;
        if(info == null || info.getIcon() == null) return;

        Laby.labyAPI().renderPipeline().renderSeeThrough(entity, () -> {
            info.getIcon().render(stack, -11, 0, 9, 9);
            if(info.isAdmin()) admin.render(stack, getWidth() + 1.5F, 0, 9, 9);
        });
    }

    @Override
    protected NameTagBackground getCustomBackground() {
        boolean enabled = config.showBackground().get();
        NameTagBackground background = super.getCustomBackground();

        if (background == null)
            background = NameTagBackground.custom(enabled, black);

        background.setEnabled(enabled);
        return background;
    }

    @Override
    public boolean isVisible() {
        return !this.entity.isCrouching() && super.isVisible();
    }

    private Component translateColorCodes(String string) {
        return LegacyComponentSerializer
            .legacyAmpersand()
            .deserialize(string);
    }
}
