package com.rappytv.globaltags.nametag;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.requests.InfoGetRequest;
import com.rappytv.globaltags.config.GlobalTagConfig;
import com.rappytv.globaltags.util.GlobalIcon;
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
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.render.matrix.Stack;
import org.jetbrains.annotations.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class CustomTag extends NameTag {

    private final GlobalTagConfig config;
    private final PositionType position;
    private final Set<UUID> resolving = new HashSet<>();
    private PlayerInfo info;

    public CustomTag(GlobalTagAddon addon, PositionType position) {
        this.config = addon.configuration();
        this.position = position;
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
                        request.getIcon()
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
        if(info == null || info.getIcon() == GlobalIcon.NONE) return;

        Laby.labyAPI().renderPipeline().renderSeeThrough(entity, () -> {
            if(!info.getIcon().resourceLocation().exists()) return;
            Icon icon = Icon.texture(info.getIcon().resourceLocation());

            icon.render(stack, -11, 0, 9, 9);
        });
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
