package com.rappytv.globaltags.ui.activities.config;

import com.rappytv.globaltags.GlobalTagsAddon;
import com.rappytv.globaltags.api.Util;
import com.rappytv.globaltags.config.subconfig.AccountConfig;
import com.rappytv.globaltags.ui.widgets.config.TagPreviewWidget;
import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.enums.GlobalPermission;
import com.rappytv.globaltags.wrapper.http.schemas.IconUploadSchema;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.Textures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.models.OperatingSystem;
import net.labymod.api.util.Debounce;
import net.labymod.api.util.I18n;

@Link("icon-chooser.lss")
@AutoActivity
public class IconSelectorActivity extends SimpleActivity {

    private final PlayerInfo<?> info;
    private final AccountConfig config;
    private final Component errorComponent;
    private final TagPreviewWidget previewWidget;

    public IconSelectorActivity() {
        this.info = null;
        this.config = null;
        this.errorComponent = Component.translatable("globaltags.settings.account.tagEditor.error");
        this.previewWidget = null;
    }

    public IconSelectorActivity(PlayerInfo<?> info, AccountConfig config) {
        this.info = info;
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
        boolean hasCustomIconPermission =
            this.info != null && this.info.hasPermission(GlobalPermission.CUSTOM_ICON);

        FlexibleContentWidget container = new FlexibleContentWidget()
            .addId("container");

        FlexibleContentWidget previewWrapper = new FlexibleContentWidget();
        ComponentWidget previewComponent = ComponentWidget.i18n(
            "globaltags.settings.account.iconSelector.heading.preview"
        ).addId("heading");

        previewWrapper.addContent(previewComponent);
        previewWrapper.addContent(this.previewWidget);

        FlexibleContentWidget uploadWrapper = new FlexibleContentWidget();

        FlexibleContentWidget dropdownWrapper = new FlexibleContentWidget();
        ComponentWidget dropdownComponent = ComponentWidget.i18n(
            "globaltags.settings.account.iconSelector.heading.dropdown"
        ).addId("heading");
        DropdownWidget<GlobalIcon> iconDropdown = new DropdownWidget<>();
        for (GlobalIcon icon : GlobalIcon.values()) {
            iconDropdown.add(icon);
        }
        iconDropdown.setTranslationKeyPrefix("globaltags.settings.account.iconSelector.entries");
        iconDropdown.setSelected(this.config.icon().get());
        uploadWrapper.setVisible(iconDropdown.getSelected() == GlobalIcon.CUSTOM);
        iconDropdown.setChangeListener((icon) -> {
            uploadWrapper.setVisible(icon == GlobalIcon.CUSTOM);
            String iconUrl = TagPreviewWidget.getIconUrl(this.info, icon);
            this.previewWidget.updateGlobalIcon(iconUrl != null ? Icon.url(iconUrl) : null);
            Debounce.of(
                "globaltags-icon-chooser",
                2000,
                () -> {
                    this.config.icon().set(iconDropdown.getSelected());
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

        ComponentWidget uploadComponent = ComponentWidget.i18n(
                "globaltags.settings.account.iconSelector.heading.uploader")
            .addId("heading");
        ButtonWidget uploadButton = ButtonWidget.i18n(
            "globaltags.settings.account.iconSelector.selector.button",
            SpriteCommon.PICTURE,
            () -> Laby.references().fileDialogs().open(
                I18n.translate("globaltags.settings.account.iconSelector.selector.title"),
                Path.of("/"),
                I18n.translate("globaltags.settings.account.iconSelector.selector.description"),
                new String[]{"png"},
                false,
                (paths) -> {
                    if (paths.length != 1) {
                        return;
                    }
                    try {
                        this.uploadIcon(paths[0]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            )
        );
        uploadButton.setHoverComponent(Component.translatable(
            "globaltags.settings.account.iconSelector.selector.hover."
                + (hasCustomIconPermission ? "note" : "noPermission"),
            hasCustomIconPermission ? NamedTextColor.WHITE : NamedTextColor.RED
        ));
        uploadButton.setEnabled(hasCustomIconPermission);

        dropdownWrapper.addContent(dropdownComponent);
        dropdownWrapper.addContent(iconDropdown);

        uploadWrapper.addContent(uploadComponent);
        uploadWrapper.addContent(uploadButton);
        if (hasCustomIconPermission && (!OperatingSystem.isOSX() || Laby.labyAPI().labyModLoader()
            .isAddonDevelopmentEnvironment())) {
            uploadWrapper.addContent(ComponentWidget.i18n(
                "globaltags.settings.account.iconSelector.selector.hint"
            ).addId("hint"));
        }

        container.addContent(previewWrapper);
        container.addContent(dropdownWrapper);
        container.addContent(uploadWrapper);

        this.document.addChild(container);
    }

    @Override
    public boolean fileDropped(MutableMouse mouse, List<Path> paths) {
        if (!paths.isEmpty()) {
            Path path = paths.getFirst();
            String fileName = path.getFileName().toString();
            if (fileName.endsWith(".png")) {
                try {
                    this.uploadIcon(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return super.fileDropped(mouse, paths);
    }

    private void uploadIcon(Path path) throws IOException {
        GlobalTagsAddon.getAPI().getApiHandler().uploadIcon(
            this.info.getUUID(),
            path,
            (response) -> {
                if (!response.isSuccessful()) {
                    Util.notify(
                        I18n.translate("globaltags.general.error"),
                        response.getError()
                    );
                    return;
                }
                IconUploadSchema data = response.getData();
                Util.notify(
                    I18n.translate("globaltags.general.success"),
                    data.getMessage()
                );
                this.previewWidget.updateGlobalIcon(
                    GlobalIcon.CUSTOM,
                    this.info.getUUID(),
                    data.getHash()
                );
                Util.broadcastTagUpdate();
            }
        );
    }
}
