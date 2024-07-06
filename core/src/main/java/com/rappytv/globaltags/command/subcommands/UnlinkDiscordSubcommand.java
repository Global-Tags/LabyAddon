package com.rappytv.globaltags.command.subcommands;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.ApiHandler;
import net.labymod.api.client.chat.command.SubCommand;

public class UnlinkDiscordSubcommand extends SubCommand {

    public UnlinkDiscordSubcommand() {
        super("unlink");
    }

    @Override
    public boolean execute(String s, String[] strings) {
        ApiHandler.unlinkDiscord((response) -> displayMessage(
            GlobalTagAddon.prefix.copy().append(response.getMessage()))
        );
        return true;
    }
}
