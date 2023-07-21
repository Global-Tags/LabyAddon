package com.rappytv.globaltags.command;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.command.subcommands.ClearCacheCommand;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.util.I18n;

public class GlobalTagCommand extends Command {

    private final GlobalTagAddon addon;

    public GlobalTagCommand(GlobalTagAddon addon) {
        super("globaltags", "globaltag", "gt");
        this.addon = addon;

        withSubCommand(new ClearCacheCommand());
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {

        String apiVersion = addon.getApiHandler().getApiVersion();

        TextComponent clearComponent = TextComponent.builder()
            .append(GlobalTagAddon.prefix + "§aVersion: §b" + GlobalTagAddon.version + "\n")
            .append(GlobalTagAddon.prefix + "§aAPI Version: " + (apiVersion != null ? "§b" + apiVersion : "§c" + I18n.translate("globaltags.messages.offline")) + "\n")
            .append(GlobalTagAddon.prefix)
            .append(TextComponent.builder()
                .text("§d§n" + I18n.translate("globaltags.messages.clearCache"))
                .hoverEvent(HoverEvent.showText(Component.text("§b" + I18n.translate("globaltags.messages.hoverClearCache"))))
                .clickEvent(ClickEvent.suggestCommand("/" + prefix + " cc"))
                .build())
            .build();

        displayMessage(clearComponent);
        return true;
    }
}
