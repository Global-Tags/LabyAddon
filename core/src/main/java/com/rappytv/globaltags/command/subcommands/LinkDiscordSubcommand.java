package com.rappytv.globaltags.command.subcommands;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.ApiHandler;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.NamedTextColor;

public class LinkDiscordSubcommand extends SubCommand {

    public LinkDiscordSubcommand() {
        super("link");
    }

    @Override
    public boolean execute(String s, String[] strings) {
        ApiHandler.linkDiscord((info) -> {
            if(info.isSuccessful()) {
                String code = ((TextComponent) info.getMessage()).getText();
                Laby.references().chatExecutor().copyToClipboard(code);
                displayMessage(GlobalTagAddon.prefix.copy().append(
                    Component.translatable("globaltags.messages.code", NamedTextColor.GREEN)
                ));
            } else displayMessage(
                GlobalTagAddon.prefix.copy().append(info.getMessage())
            );
        });
        return true;
    }
}
