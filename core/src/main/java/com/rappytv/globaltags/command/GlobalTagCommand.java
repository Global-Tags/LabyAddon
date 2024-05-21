package com.rappytv.globaltags.command;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.requests.VersionGetRequest;
import com.rappytv.globaltags.command.subcommands.ClearCacheCommand;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.util.I18n;

public class GlobalTagCommand extends Command {

    public GlobalTagCommand() {
        super("globaltags", "globaltag", "gt");

        withSubCommand(new ClearCacheCommand());
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        VersionGetRequest request = new VersionGetRequest();
        request.sendAsyncRequest((response) -> {
            // This is not being logged
            System.out.println("Callback called 2");
            String version = request.getVersion();

            TextComponent clearComponent = TextComponent.builder()
                .append(GlobalTagAddon.prefix + "§aVersion: §b" + GlobalTagAddon.version + "\n")
                .append(GlobalTagAddon.prefix + "§aAPI Version: " + (version != null ? "§b" + version : "§c" + I18n.translate("globaltags.messages.offline")) + "\n")
                .append(GlobalTagAddon.prefix)
                .append(TextComponent.builder()
                    .text("§d§n" + I18n.translate("globaltags.messages.clearCache"))
                    .hoverEvent(HoverEvent.showText(Component.text("§b" + I18n.translate("globaltags.messages.hoverClearCache"))))
                    .clickEvent(ClickEvent.suggestCommand("/" + prefix + " cc"))
                    .build())
                .build();

            displayMessage(clearComponent);
        });
        return true;
    }
}
