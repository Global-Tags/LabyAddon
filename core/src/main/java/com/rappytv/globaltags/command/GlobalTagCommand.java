package com.rappytv.globaltags.command;

import com.rappytv.globaltags.GlobalTagsAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.command.subcommands.ClearCacheCommand;
import com.rappytv.globaltags.command.subcommands.LinkSubcommand;
import com.rappytv.globaltags.command.subcommands.RedeemCommand;
import com.rappytv.globaltags.command.subcommands.RenewCacheCommand;
import com.rappytv.globaltags.command.subcommands.UnlinkSubcommand;
import com.rappytv.globaltags.command.subcommands.VerifyCommand;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;

public class GlobalTagCommand extends Command {

    private final GlobalTagAPI api;
    private final String version;

    public GlobalTagCommand(GlobalTagsAddon addon) {
        super("globaltags", "globaltag", "gt");
        this.api = GlobalTagsAddon.getAPI();
        this.version = addon.addonInfo().getVersion();

        this.translationKey("globaltags.commands.base");
        this.withSubCommand(new ClearCacheCommand(this.api));
        this.withSubCommand(new LinkSubcommand(this.api));
        this.withSubCommand(new RedeemCommand(this.api));
        this.withSubCommand(new RenewCacheCommand(this.api));
        this.withSubCommand(new UnlinkSubcommand(this.api));
        this.withSubCommand(new VerifyCommand(this.api));
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        this.api.getApiHandler().getApiInfo((response) -> {
            TextComponent clearComponent = TextComponent.builder()
                .append(GlobalTagsAddon.prefix())
                .append(Component.translatable(
                    this.getTranslationKey("version"),
                    NamedTextColor.GREEN,
                    Component.text(this.version, NamedTextColor.AQUA)
                ))
                .append(Component.newline())
                .append(GlobalTagsAddon.prefix())
                .append(Component.translatable(
                    this.getTranslationKey("api.version"),
                    NamedTextColor.GREEN,
                    response != null && response.isSuccessful()
                        ? Component.text(response.getData().getVersion(), NamedTextColor.AQUA)
                        : Component.translatable(
                            this.getTranslationKey("api.offline"),
                            NamedTextColor.RED
                        )
                ))
                .append(Component.newline())
                .append(GlobalTagsAddon.prefix())
                .append(Component
                    .translatable(this.getTranslationKey("clearCache.label"))
                    .color(NamedTextColor.LIGHT_PURPLE)
                    .decorate(TextDecoration.UNDERLINED)
                    .hoverEvent(HoverEvent.showText(Component.translatable(
                        this.getTranslationKey("clearCache.hover"),
                        NamedTextColor.AQUA
                    )))
                    .clickEvent(ClickEvent.suggestCommand("/" + prefix + " cc")))
                .build();

            this.displayMessage(clearComponent);
        });
        return true;
    }
}
