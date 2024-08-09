package com.rappytv.globaltags.command.subcommands;

import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.GlobalTagAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public class LinkDiscordSubcommand extends SubCommand {

    private final GlobalTagAPI api;

    public LinkDiscordSubcommand(GlobalTagAPI api) {
        super("link");
        this.api = api;
    }

    @Override
    public boolean execute(String s, String[] strings) {
        api.getApiHandler().linkDiscord((info) -> {
            if(info.successful()) {
                String code = info.data();
                Laby.references().chatExecutor().copyToClipboard(code);
                displayMessage(GlobalTagAddon.prefix.copy().append(Component.translatable(
                    "globaltags.commands.link.discord.copied",
                    NamedTextColor.GREEN
                )));
            } else displayMessage(
                GlobalTagAddon.prefix.copy().append(Component.text(
                    info.data(),
                    NamedTextColor.RED
                ))
            );
        });
        return true;
    }
}
