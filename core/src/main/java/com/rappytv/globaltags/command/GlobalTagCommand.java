package com.rappytv.globaltags.command;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.command.subcommands.ClearCacheCommand;
import com.rappytv.globaltags.command.subcommands.LinkDiscordSubcommand;
import com.rappytv.globaltags.command.subcommands.UnlinkDiscordSubcommand;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.util.I18n;

public class GlobalTagCommand extends Command {

    private final GlobalTagAPI api;
    private final String version;

    public GlobalTagCommand(GlobalTagAddon addon) {
        super("globaltags", "globaltag", "gt");
        this.api = GlobalTagAddon.getAPI();
        this.version = addon.addonInfo().getVersion();

        withSubCommand(new ClearCacheCommand(api));
        withSubCommand(new LinkDiscordSubcommand(api));
        withSubCommand(new UnlinkDiscordSubcommand(api));
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        api.getApiHandler().getVersion((version) -> {
            TextComponent clearComponent = TextComponent.builder()
                .append(GlobalTagAddon.prefix)
                .append(Component.text("Version: ", NamedTextColor.GREEN))
                .append(Component.text(this.version + "\n", NamedTextColor.AQUA))
                .append(GlobalTagAddon.prefix)
                .append(Component.text("API Version: ", NamedTextColor.GREEN))
                .append(version != null ? Component.text(version, NamedTextColor.AQUA) : Component.translatable("globaltags.messages.offline", NamedTextColor.RED))
                .append("\n")
                .append(GlobalTagAddon.prefix)
                .append(Component
                    .translatable("globaltags.messages.clearCache")
                    .color(NamedTextColor.LIGHT_PURPLE)
                    .decorate(TextDecoration.UNDERLINED)
                    .hoverEvent(HoverEvent.showText(Component.text("§b" + I18n.translate("globaltags.messages.hoverClearCache"))))
                    .clickEvent(ClickEvent.suggestCommand("/" + prefix + " cc")))
                .build();

            displayMessage(clearComponent);
        });
        return true;
    }
}
