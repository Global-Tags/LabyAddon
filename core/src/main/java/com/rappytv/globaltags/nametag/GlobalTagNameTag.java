package com.rappytv.globaltags.nametag;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.config.GlobalTagConfig;
import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.enums.GlobalPosition;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import java.awt.*;
import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.entity.player.tag.tags.NameTagBackground;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.render.matrix.Stack;
import org.jetbrains.annotations.Nullable;

public class GlobalTagNameTag extends NameTag {

    private final int black = new Color(0, 0, 0, 70).getRGB();
    private final GlobalTagAPI api;
    private final GlobalTagConfig config;
    private final PositionType position;
    private PlayerInfo<Component> info;

    public GlobalTagNameTag(GlobalTagAddon addon, PositionType position) {
        this.api = GlobalTagAddon.getAPI();
        this.config = addon.configuration();
        this.position = position;
    }

    @Override
    public float getScale() {
        return (float) this.config.tagSize().get() / 10;
    }

    @Override
    protected @Nullable RenderableComponent getRenderableComponent() {
        if(!this.config.enabled().get()) return null;
        if(this.entity == null || !(this.entity instanceof Player)) return null;
        UUID uuid = this.entity.getUniqueId();
        if(!this.config.showOwnTag().get() && Laby.labyAPI().getUniqueId().equals(uuid))
            return null;

        this.info = null;
        if(this.api.getCache().has(uuid))
            this.info = this.api.getCache().get(uuid);
        else {
            if(this.position == PositionType.ABOVE_NAME)
                this.api.getCache().resolve(uuid);
        }
        if(this.info == null || this.info.getTag() == null) return null;
        if(!this.getGlobalPosition(this.position).equals(this.info.getPosition())) return null;

        return RenderableComponent.of(this.info.getTag());
    }

    @Override
    @SuppressWarnings("deprecation")
    public void render(Stack stack, Entity entity) {
        super.render(stack, entity);
        if(this.getRenderableComponent() == null) return;
        if(this.info == null) return;

        Laby.labyAPI().renderPipeline().renderSeeThrough(entity, () -> {
            if(this.info.getGlobalIcon() != GlobalIcon.NONE) Icon.url(this.info.getIconUrl()).render(
                stack,
                -11,
                0,
                9,
                9
            );
            if(this.info.getRoleIcon() != null) Icon.url(this.api.getUrls().getRoleIcon(this.info.getRoleIcon())).render(
                stack,
                this.getWidth() + 0.9F,
                -1.2F,
                11,
                11
            );
        });
    }

    private GlobalPosition getGlobalPosition(PositionType type) {
        try {
            return GlobalPosition.valueOf(type.name().split("_")[0]);
        } catch (Exception e) {
            return GlobalPosition.ABOVE;
        }
    }

    @Override
    protected NameTagBackground getCustomBackground() {
        boolean enabled = this.config.showBackground().get();
        NameTagBackground background = super.getCustomBackground();

        if (background == null)
            background = NameTagBackground.custom(enabled, this.black);

        background.setEnabled(enabled);
        return background;
    }

    @Override
    public boolean isVisible() {
        return !this.entity.isCrouching() && super.isVisible();
    }
}
