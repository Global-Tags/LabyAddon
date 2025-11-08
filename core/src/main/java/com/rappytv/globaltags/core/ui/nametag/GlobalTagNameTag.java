package com.rappytv.globaltags.core.ui.nametag;

import com.rappytv.globaltags.core.ui.snapshot.GlobalTagsExtraKeys;
import com.rappytv.globaltags.core.ui.snapshot.GlobalTagsUserSnapshot;
import com.rappytv.globaltags.wrapper.enums.GlobalPosition;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.entity.player.tag.tags.ComponentNameTag;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.render.state.entity.EntitySnapshot;
import net.labymod.api.laby3d.render.queue.SubmissionCollector;
import net.labymod.api.laby3d.render.queue.submissions.IconSubmission.DisplayMode;
import org.jetbrains.annotations.NotNull;

public class GlobalTagNameTag extends ComponentNameTag {

    private static final float globalIconSize = 9;
    private static final float staffIconSize = 11;

    private final PositionType registeredPosition;
    private final Supplier<Float> scaleSupplier;
    private GlobalTagsUserSnapshot globaltagsUser;

    public GlobalTagNameTag(PositionType position, Supplier<Float> scaleSupplier) {
        this.registeredPosition = position;
        this.scaleSupplier = scaleSupplier;
    }

    @Override
    public float getScale() {
        return this.scaleSupplier.get();
    }

    @Override
    protected @NotNull List<Component> buildComponents(EntitySnapshot snapshot) {
        this.globaltagsUser = snapshot.get(GlobalTagsExtraKeys.GLOBALTAGS_USER);
        if (this.globaltagsUser == null) {
            return super.buildComponents(snapshot);
        }
        PlayerInfo<Component> info = this.globaltagsUser.getPlayerInfo();
        if (snapshot.isDiscrete()
            || info == null
            || info.getTag() == null
            || !this.getGlobalPosition(this.registeredPosition).equals(info.getPosition())
            || this.globaltagsUser.isHidden()
            || !this.globaltagsUser.passedSelfCheck()) {
            return super.buildComponents(snapshot);
        }

        return Collections.singletonList(info.getTag());
    }

    @Override
    public void render(Stack stack, SubmissionCollector submissionCollector,
        EntitySnapshot snapshot) {
        super.render(stack, submissionCollector, snapshot);
        PlayerInfo<Component> info = this.globaltagsUser.getPlayerInfo();
        if (info == null) {
            return;
        }

        if (info.hasGlobalIcon()) {
            submissionCollector.submitIcon(
                stack,
                Icon.url(Objects.requireNonNull(info.getIconUrl())),
                DisplayMode.NORMAL,
                -11,
                -1,
                globalIconSize,
                globalIconSize,
                -1
            );
        }
        Icon staffIcon = this.globaltagsUser.getStaffIcon();
        if (staffIcon != null) {
            submissionCollector.submitIcon(
                stack,
                staffIcon,
                DisplayMode.NORMAL,
                this.getWidth() + 0.9F,
                -1.8F,
                staffIconSize,
                staffIconSize,
                -1
            );
        }
    }

    private GlobalPosition getGlobalPosition(PositionType type) {
        return switch (type) {
            case ABOVE_NAME -> GlobalPosition.ABOVE;
            case BELOW_NAME -> GlobalPosition.BELOW;
            case RIGHT_TO_NAME -> GlobalPosition.RIGHT;
            case LEFT_TO_NAME -> GlobalPosition.LEFT;
        };
    }
}
