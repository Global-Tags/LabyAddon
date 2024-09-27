package com.rappytv.globaltags.command;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.command.subcommands.ClearCacheCommand;
import com.rappytv.globaltags.command.subcommands.LinkDiscordSubcommand;
import com.rappytv.globaltags.command.subcommands.RenewCacheCommand;
import com.rappytv.globaltags.command.subcommands.UnlinkDiscordSubcommand;
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

    public GlobalTagCommand(GlobalTagAddon addon) {
        super("globaltags", "globaltag", "gt");
        this.api = GlobalTagAddon.getAPI();
        this.version = addon.addonInfo().getVersion();

        withSubCommand(new ClearCacheCommand(api));
        withSubCommand(new LinkDiscordSubcommand(api));
        withSubCommand(new RenewCacheCommand(api));
        withSubCommand(new UnlinkDiscordSubcommand(api));
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        api.getApiHandler().getVersion((version) -> {
            TextComponent clearComponent = TextComponent.builder()
                .append(GlobalTagAddon.prefix)
                .append(Component.translatable(
                    "globaltags.commands.base.version",
                    NamedTextColor.GREEN,
                    Component.text(this.version, NamedTextColor.AQUA)
                ))
                .append(Component.newline())
                .append(GlobalTagAddon.prefix)
                .append(Component.translatable(
                    "globaltags.commands.base.api.version",
                    NamedTextColor.GREEN,
                    version != null && version.successful()
                        ? Component.text(version.data(), NamedTextColor.AQUA)
                        : Component.translatable(
                            "globaltags.commands.base.api.offline",
                            NamedTextColor.RED
                        )
                ))
                .append(Component.newline())
                .append(GlobalTagAddon.prefix)
                .append(Component
                    .translatable("globaltags.commands.base.clear_cache.label")
                    .color(NamedTextColor.LIGHT_PURPLE)
                    .decorate(TextDecoration.UNDERLINED)
                    .hoverEvent(HoverEvent.showText(Component.translatable(
                        "globaltags.commands.base.clear_cache.hover",
                        NamedTextColor.AQUA
                    )))
                    .clickEvent(ClickEvent.suggestCommand("/" + prefix + " cc")))
                .build();

            displayMessage(clearComponent);
        });
        return true;
    }
}
