package com.rappytv.globaltags.nametag;

import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.config.GlobalTagConfig;
import com.rappytv.globaltags.config.subconfig.RainbowTagSubconfig;
import com.rappytv.globaltags.config.subconfig.RainbowTagSubconfig.RainbowDirection;
import com.rappytv.globaltags.wrapper.enums.GlobalPosition;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.entity.player.tag.tags.NameTagBackground;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.render.matrix.Stack;
import org.jetbrains.annotations.Nullable;
import java.awt.*;
import java.util.List;
import java.util.UUID;

public class CustomTag extends NameTag {

    private static final List<TextColor> colors = List.of(NamedTextColor.RED, NamedTextColor.GOLD, NamedTextColor.YELLOW, NamedTextColor.GREEN, NamedTextColor.AQUA);
    public static int ticks = 0;
    private int stage = 0;
    private final int black = new Color(0, 0, 0, 70).getRGB();
    private final GlobalTagAPI api;
    private final GlobalTagConfig config;
    private final PositionType position;
    private PlayerInfo<Component> info;

    public CustomTag(GlobalTagAddon addon, PositionType position) {
        this.api = GlobalTagAddon.getAPI();
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
        if(api.getCache().has(uuid))
            info = api.getCache().get(uuid);
        else {
            if(position == PositionType.ABOVE_NAME)
                api.getCache().resolve(uuid);
        }
        if(info == null || info.getTag() == null) return null;
        if(!getGlobalPosition(position).equals(info.getPosition())) return null;

        RainbowTagSubconfig options = this.config.rainbowTags();
        Component component = info.getTag().copy();
        if(options.enabled() && !colors.isEmpty()) {
            component = getRainbowTag((TextComponent) component, options);
            if(options.bold()) component.decorate(TextDecoration.BOLD);
            if(options.italic()) component.decorate(TextDecoration.ITALIC);
            if(options.underscored()) component.decorate(TextDecoration.UNDERLINED);
            if(options.strikethrough()) component.decorate(TextDecoration.STRIKETHROUGH);
            if(ticks >= 5) {
                ticks = 0;
                if(options.direction() == RainbowDirection.LEFT_TO_RIGHT) {
                    if(--stage <= 0) stage = colors.size() - 1;
                } else {
                    if(++stage >= colors.size()) stage = 0;
                }
            }
        }

        return RenderableComponent.of(component);
    }

    private Component getRainbowTag(TextComponent component, RainbowTagSubconfig options) {
        return switch (options.mode()) {
            case FULL_TAG -> component.color(colors.get(stage));
            case SEPERATE_LETTERS -> {
                String currentTag = component.getText();
                Component rainbowTag = Component.empty();
                int colorIndex = stage;
                for(int i = 0; i < currentTag.toCharArray().length; i++) {
                    Component letter = Component.text(currentTag.charAt(i)).color(colors.get(colorIndex));
                    rainbowTag.append(letter);
                    if(++colorIndex >= colors.size()) colorIndex = 0;
                }

                yield rainbowTag;
            }
        };
    }

    @Override
    @SuppressWarnings("deprecation")
    public void render(Stack stack, Entity entity) {
        super.render(stack, entity);
        if(this.getRenderableComponent() == null) return;
        if(info == null) return;

        Laby.labyAPI().renderPipeline().renderSeeThrough(entity, () -> {
            if(info.getIconUrl() != null) Icon.url(info.getIconUrl()).render(
                stack,
                -11,
                0,
                9,
                9
            );
            if(info.getHighestRole() != null) Icon.url(info.getHighestRole().getIconUrl()).render(
                stack,
                getWidth() + 0.9F,
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
}
