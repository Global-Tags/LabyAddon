package com.rappytv.globaltags.ui.activities.config;

import com.rappytv.globaltags.GlobalTagsAddon;
import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.config.subconfig.AccountConfig;
import com.rappytv.globaltags.ui.widgets.config.TagPreviewWidget;
import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;

@Link("tag-editor.lss")
@AutoActivity
public class TagEditorActivity extends SimpleActivity {

    private final AccountConfig config;
    private final TagPreviewWidget previewWidget;

    public TagEditorActivity(PlayerInfo<?> info, AccountConfig config) {
        this.config = config;
        if (info == null) {
            this.previewWidget = new TagPreviewWidget((Component) null, null, null);
            GlobalTagsAddon.getAPI().getCache().resolveSelf((newInfo) -> {
                if (newInfo == null) {
                    this.displayPreviousScreen();
                    return;
                }
                this.previewWidget.updateTag(newInfo.getPlainTag());
                this.previewWidget.updateGlobalIcon(newInfo.getGlobalIcon(), newInfo.getUUID(),
                    newInfo.getGlobalIconHash());
                this.previewWidget.updateRoleIcon(newInfo.getRoleIcon());
            });
            return;
        }
        this.previewWidget = new TagPreviewWidget(
            config.tag().get(),
            config.icon().get() != GlobalIcon.NONE
                ? Icon.url(TagPreviewWidget.getIconUrl(info, config.icon().get()))
                : null,
            config.hideRoleIcon().get()
                ? null
                : info.getRoleIcon() != null
                    ? Icon.url(GlobalTagsAddon.getAPI().getUrls().getRoleIcon(info.getRoleIcon()))
                    : null
        );
    }

    @Override
    public void initialize(Parent parent) { // TODO: Actually translate strings
        super.initialize(parent);

        FlexibleContentWidget container = new FlexibleContentWidget()
            .addId("container");

        DivWidget previewDiv = new DivWidget()
            .addId("preview");
        ComponentWidget previewComponent = ComponentWidget.i18n("Preview")
            .addId("heading");
        previewDiv.addChild(previewComponent);
        previewDiv.addChild(this.previewWidget);

        DivWidget editorDiv = new DivWidget()
            .addId("editor");
        ComponentWidget editorComponent = ComponentWidget.i18n("Editor")
            .addId("heading");
        TextFieldWidget editorTextField = new TextFieldWidget()
            .addId("editor-input");
        editorTextField.setText(this.config.tag().get());
        editorTextField.updateListener(this.previewWidget::updateTag);
        editorTextField.setFocused(true);
        editorTextField.setCursorAtEnd();
        // TODO: Update view index somehow
        editorDiv.addChild(editorComponent);
        editorDiv.addChild(editorTextField);

        DivWidget utilsDiv = new DivWidget()
            .addId("utils");
        ComponentWidget utilsComponent = ComponentWidget.i18n("Utils")
            .addId("heading");
        // TODO: Add utils
        utilsDiv.addChild(utilsComponent);

        HorizontalListWidget buttons = new HorizontalListWidget()
            .addId("buttons");
        ButtonWidget saveButton = ButtonWidget.component(Component.translatable(
            "Save",
            NamedTextColor.GREEN
        ), () -> {
            this.config.tag().set(editorTextField.getText());
            // TODO: Go back to AccountConfig
            Util.notify(
                Component.translatable("Tag changed..."),
                Component.translatable(
                    "Don't forget to upload your changes by clicking on the button below!")
            );
        });
        ButtonWidget cancelButton = ButtonWidget.component(Component.translatable(
            "Cancel",
            NamedTextColor.RED
        ), () -> { /* TODO: Go back to AccountConfig */ });
        buttons.addEntry(saveButton);
        buttons.addEntry(cancelButton);

        container.addContent(previewDiv);
        container.addContent(editorDiv);
        container.addContent(utilsDiv);
        container.addContent(buttons);

        this.document.addChild(container);
    }
}
