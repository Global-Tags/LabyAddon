package com.rappytv.globaltags.command;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.ApiHandler;
import com.rappytv.globaltags.api.ApiRequest;
import com.rappytv.globaltags.command.subcommands.ClearCacheCommand;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.util.I18n;

public class GlobalTagCommand extends Command {

    public GlobalTagCommand() {
        super("globaltags", "globaltag", "gt");

        withSubCommand(new ClearCacheCommand());
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        ApiHandler.getVersion((version) -> {
            TextComponent clearComponent = TextComponent.builder()
                .append(GlobalTagAddon.prefix)
                .append(Component.text("Version: ", NamedTextColor.GREEN))
                .append(Component.text(ApiRequest.getVersion() + "\n", NamedTextColor.AQUA))
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
