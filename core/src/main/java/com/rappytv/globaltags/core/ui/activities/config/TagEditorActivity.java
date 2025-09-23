package com.rappytv.globaltags.core.ui.activities.config;

import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.core.GlobalTagsAddon;
import com.rappytv.globaltags.core.config.subconfig.AccountConfig;
import com.rappytv.globaltags.core.ui.widgets.config.TagPreviewWidget;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import java.util.Arrays;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.Textures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.color.ColorPickerWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.util.Color;
import net.labymod.api.util.Debounce;

@Link("tag-editor.lss")
@AutoActivity
public class TagEditorActivity extends SimpleActivity {

    private final static List<ColorEntry> numberColors = Arrays.asList(
        new ColorEntry('0', NamedTextColor.BLACK),
        new ColorEntry('1', NamedTextColor.DARK_BLUE),
        new ColorEntry('2', NamedTextColor.DARK_GREEN),
        new ColorEntry('3', NamedTextColor.DARK_AQUA),
        new ColorEntry('4', NamedTextColor.DARK_RED),
        new ColorEntry('5', NamedTextColor.DARK_PURPLE),
        new ColorEntry('6', NamedTextColor.GOLD),
        new ColorEntry('7', NamedTextColor.GRAY),
        new ColorEntry('8', NamedTextColor.DARK_GRAY),
        new ColorEntry('9', NamedTextColor.BLUE)
    );
    private final static List<ColorEntry> symbolColors = Arrays.asList(
        new ColorEntry('a', NamedTextColor.GREEN),
        new ColorEntry('b', NamedTextColor.AQUA),
        new ColorEntry('c', NamedTextColor.RED),
        new ColorEntry('d', NamedTextColor.LIGHT_PURPLE),
        new ColorEntry('e', NamedTextColor.YELLOW),
        new ColorEntry('f', NamedTextColor.WHITE)
    );
    private final static List<DecorationEntry> decorations = Arrays.asList(
        new DecorationEntry('k', TextDecoration.OBFUSCATED),
        new DecorationEntry('l', TextDecoration.BOLD),
        new DecorationEntry('m', TextDecoration.STRIKETHROUGH),
        new DecorationEntry('n', TextDecoration.UNDERLINED),
        new DecorationEntry('o', TextDecoration.ITALIC)
    );
    private final AccountConfig config;
    private final Component errorComponent;
    private final TagPreviewWidget previewWidget;

    private TextFieldWidget editorTextField;

    public TagEditorActivity() {
        this.config = null;
        this.errorComponent = Component.translatable("globaltags.settings.account.tagEditor.error");
        this.previewWidget = null;
    }

    public TagEditorActivity(PlayerInfo<?> info, AccountConfig config) {
        this.config = config;
        this.errorComponent = null;
        String iconUrl = TagPreviewWidget.getIconUrl(info, config.icon().get());
        if (info == null) {
            this.previewWidget = new TagPreviewWidget(
                config.tag().get(),
                iconUrl != null ? Icon.url(iconUrl) : null,
                null
            );
            return;
        }
        this.previewWidget = new TagPreviewWidget(
            config.tag().get(),
            iconUrl != null ? Icon.url(iconUrl) : null,
            config.hideRoleIcon().get()
                ? null
                : info.getRoleIcon() != null
                    ? Icon.url(GlobalTagsAddon.getAPI().getUrls().getRoleIcon(info.getRoleIcon()))
                    : null
        );
    }

    @Override
    public void initialize(Parent parent) {
        super.initialize(parent);

        if (this.errorComponent != null) {
            this.document.addChild(ComponentWidget.component(this.errorComponent).addId("error"));
            return;
        }

        FlexibleContentWidget container = new FlexibleContentWidget()
            .addId("container");

        FlexibleContentWidget previewWrapper = new FlexibleContentWidget()
            .addId("preview");
        ComponentWidget previewComponent = ComponentWidget.i18n(
                "globaltags.settings.account.tagEditor.preview")
            .addId("heading");
        previewWrapper.addContent(previewComponent);
        previewWrapper.addContent(this.previewWidget);

        FlexibleContentWidget editorWrapper = new FlexibleContentWidget()
            .addId("editor");
        ComponentWidget editorComponent = ComponentWidget.i18n(
                "globaltags.settings.account.tagEditor.editor")
            .addId("heading");
        this.editorTextField = new TextFieldWidget()
            .addId("editor-input");
        this.addText(this.config.tag().get());
        this.editorTextField.updateListener((text) -> {
            this.previewWidget.updateTag(text);
            Debounce.of(
                "globaltags-tag-editor",
                2000,
                () -> {
                    this.config.tag().set(this.editorTextField.getText());
                    Util.notify(
                        Component.translatable("globaltags.settings.account.save.title"),
                        Component.translatable(
                            "globaltags.settings.account.save.description",
                            Component.translatable(
                                "globaltags.settings.account.updateSettings.name")
                        )
                    );
                }
            );
        });
        editorWrapper.addContent(editorComponent);
        editorWrapper.addContent(this.editorTextField);

        FlexibleContentWidget utilsWrapper = new FlexibleContentWidget()
            .addId("utils");
        ComponentWidget utilsComponent = ComponentWidget.i18n(
                "globaltags.settings.account.tagEditor.utils")
            .addId("heading");
        HorizontalListWidget utilsLine1 = new HorizontalListWidget();
        HorizontalListWidget utilsLine2 = new HorizontalListWidget();
        HorizontalListWidget utilsLine3 = new HorizontalListWidget();
        for (ColorEntry entry : numberColors) {
            String label = "&" + entry.character;
            ComponentWidget colorButton = ComponentWidget.text(
                label,
                entry.color
            ).addId("color-button");
            colorButton.setPressable(() -> this.addText(label));
            utilsLine1.addEntry(colorButton);
        }
        for (ColorEntry entry : symbolColors) {
            String label = "&" + entry.character;
            ComponentWidget colorButton = ComponentWidget.text(
                label,
                entry.color
            ).addId("color-button");
            colorButton.setPressable(() -> this.addText(label));
            utilsLine2.addEntry(colorButton);
        }
        for (DecorationEntry entry : decorations) {
            String label = "&" + entry.character;
            ComponentWidget colorButton = ComponentWidget.component(
                Component.text(label).decorate(entry.decoration)
            ).addId("color-button");
            colorButton.setPressable(() -> this.addText(label));
            utilsLine3.addEntry(colorButton);
        }
        HorizontalListWidget utilSquareWrapper = new HorizontalListWidget()
            .addId("square-wrapper");
        ColorPickerWidget utilsColorPicker = ColorPickerWidget.of(Color.WHITE);
        utilsColorPicker.addUpdateListener(utilsColorPicker, (color) -> Debounce.of(
            "globaltags-colorpicker",
            1000,
            () -> this.addText(String.format("<#%06X>", 0xFFFFFF & color.get()))
        ));
        ButtonWidget utilsGradientButton = ButtonWidget.icon(SpriteCommon.PAINT, () ->
            Laby.references().chatExecutor().openUrl(
                "https://globaltags.xyz/gradients",
                false
            )
        ).addId("gradient-button");
        utilsColorPicker.setHoverComponent(
            Component.translatable("globaltags.settings.account.tagEditor.hexChooser"));
        utilsGradientButton.setHoverComponent(
            Component.translatable("globaltags.settings.account.tagEditor.gradientGenerator"));

        utilSquareWrapper.addEntry(utilsColorPicker);
        utilSquareWrapper.addEntry(utilsGradientButton);

        utilsWrapper.addContent(utilsComponent);
        utilsWrapper.addContent(utilsLine1);
        utilsWrapper.addContent(utilsLine2);
        utilsWrapper.addContent(utilsLine3);
        utilsWrapper.addContent(utilSquareWrapper);

        container.addContent(previewWrapper);
        container.addContent(editorWrapper);
        container.addContent(utilsWrapper);

        this.document.addChild(container);
    }

    private void addText(String text) {
        this.editorTextField.setText(this.editorTextField.getText() + text);
        this.editorTextField.setFocused(true);
        this.editorTextField.setCursorAtEnd();
    }

    private record ColorEntry(Character character, TextColor color) {

    }

    private record DecorationEntry(Character character, TextDecoration decoration) {

    }
}
