package com.rappytv.globaltags.config.activity;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.api.Util.ResultType;
import com.rappytv.globaltags.config.widget.TagPreviewWidget;
import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.enums.GlobalPosition;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.Links;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;

@Links({@Link("tag-update.lss"), @Link("preview.lss")})
@AutoActivity
public class TagUpdateActivity extends SimpleActivity {

    private final GlobalTagAPI api;
    private final PlayerInfo<?> currentInfo;

    public TagUpdateActivity(PlayerInfo<?> currentInfo) {
        this.api = GlobalTagAddon.getAPI();
        this.currentInfo = currentInfo;
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);
        boolean dataExists = this.currentInfo != null;

        FlexibleContentWidget windowWidget = new FlexibleContentWidget()
            .addId("window");

        ComponentWidget previewLabel = ComponentWidget.i18n("Preview")
            .addId("label");

        String tag = dataExists ? this.currentInfo.getPlainTag() : "";
        GlobalIcon globalIcon = dataExists ? this.currentInfo.getGlobalIcon() : GlobalIcon.NONE;
        String globalIconUrl = this.getIconUrl(this.api, globalIcon);
        String roleIconUrl = dataExists ? this.currentInfo.getHighestRoleIcon() : null;

        TagPreviewWidget previewWidget = new TagPreviewWidget(
            tag,
            globalIconUrl != null ? Icon.url(globalIconUrl) : null,
            roleIconUrl != null ? Icon.url(roleIconUrl) : null
        );

        ComponentWidget inputLabel = ComponentWidget.i18n("Tag")
            .addId("label");

        TextFieldWidget inputWidget = new TextFieldWidget();
        inputWidget.setText(tag);
        inputWidget.updateListener(previewWidget::updateTag);

        ComponentWidget positionLabel = ComponentWidget.i18n("Position")
            .addId("label");

        DropdownWidget<GlobalPosition> positionWidget = new DropdownWidget<>();
        for (GlobalPosition position : GlobalPosition.values()) {
            positionWidget.add(position);
        }
        positionWidget.setSelected(
            dataExists ? this.currentInfo.getPosition() : GlobalPosition.ABOVE);
        positionWidget.setTranslationKeyPrefix("globaltags.settings.tags.position.entries.");

        ComponentWidget iconLabel = ComponentWidget.i18n("Icon")
            .addId("label");

        DropdownWidget<GlobalIcon> iconWidget = new DropdownWidget<>();
        for (GlobalIcon icon : GlobalIcon.values()) {
            iconWidget.add(icon);
        }
        iconWidget.setSelected(globalIcon);
        iconWidget.setTranslationKeyPrefix("globaltags.settings.tags.globalIcon.entries.");
        iconWidget.setChangeListener(icon -> previewWidget.updateGlobalIcon(
            icon,
            dataExists ? this.currentInfo.getUUID() : null,
            dataExists ? this.currentInfo.getGlobalIconHash() : null
        ));

        ButtonWidget submitButton = ButtonWidget.i18n("Update Tag", () -> {
            if (this.api.getAuthorization() == null) {
                Util.notify(
                    Component.translatable("globaltags.general.error"),
                    Component.translatable("globaltags.settings.tags.tagPreview.labyConnect")
                );
                return;
            }
            this.api.getCache().renewSelf(info -> {
                if (info == null || !info.getPlainTag()
                    .equals(inputWidget.getText())) {
                    this.api.getApiHandler().setTag(
                        inputWidget.getText(), (response) -> {
                            if (response.isSuccessful()) {
                                Util.update(this.api, ResultType.TAG,
                                    Component.text("✔", NamedTextColor.GREEN));
                            } else {
                                Util.update(this.api, ResultType.TAG,
                                    Component.text(response.getError(), NamedTextColor.RED));
                            }
                        });
                } else {
                    Util.update(this.api, ResultType.TAG, Util.unchanged);
                }
                if (info == null || !info.getPosition()
                    .equals(positionWidget.getSelected())) {
                    this.api.getApiHandler().setPosition(
                        positionWidget.getSelected(), (response) -> {
                            if (response.isSuccessful()) {
                                Util.update(this.api, ResultType.POSITION,
                                    Component.text("✔", NamedTextColor.GREEN));
                            } else {
                                Util.update(this.api, ResultType.POSITION,
                                    Component.text(response.getError(), NamedTextColor.RED));
                            }
                        });
                } else {
                    Util.update(this.api, ResultType.POSITION, Util.unchanged);
                }
                if (info == null || !info.getGlobalIcon()
                    .equals(iconWidget.getSelected())) {
                    this.api.getApiHandler().setIcon(
                        iconWidget.getSelected(), (response) -> {
                            if (response.isSuccessful()) {
                                Util.update(this.api, ResultType.ICON,
                                    Component.text("✔", NamedTextColor.GREEN));
                            } else {
                                Util.update(this.api, ResultType.ICON,
                                    Component.text(response.getError(), NamedTextColor.RED));
                            }
                        });
                } else {
                    Util.update(this.api, ResultType.ICON, Util.unchanged);
                }
            });
        });
        submitButton.addId("submit-button");

        DivWidget previewDiv = new DivWidget().addId("preview-div");
        previewDiv.addChild(previewLabel);
        previewDiv.addChild(previewWidget);

        DivWidget inputDiv = new DivWidget().addId("input-div");
        inputDiv.addChild(inputLabel);
        inputDiv.addChild(inputWidget);

        DivWidget positionDiv = new DivWidget().addId("position-div");
        positionDiv.addChild(positionLabel);
        positionDiv.addChild(positionWidget);

        DivWidget iconDiv = new DivWidget().addId("icon-div");
        iconDiv.addChild(iconLabel);
        iconDiv.addChild(iconWidget);

        HorizontalListWidget dropdownListWidget = new HorizontalListWidget()
            .addId("dropdown-list");
        dropdownListWidget.addEntry(positionDiv);
        dropdownListWidget.addEntry(iconDiv);

        windowWidget.addContent(previewDiv);
        windowWidget.addContent(inputDiv);
        windowWidget.addContent(dropdownListWidget);
        windowWidget.addContent(submitButton);

        this.document.addChild(windowWidget);
    }

    private String getIconUrl(GlobalTagAPI api, GlobalIcon icon) {
        return this.currentInfo != null && icon == GlobalIcon.CUSTOM
            && this.currentInfo.getGlobalIconHash() != null
            ? api.getUrls().getCustomIcon(api.getClientUUID(), this.currentInfo.getGlobalIconHash())
            : icon != GlobalIcon.NONE ? api.getUrls().getDefaultIcon(icon) : null;
    }
}
