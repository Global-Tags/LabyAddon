package com.rappytv.globaltags.command.subcommands;

import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.Util;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public class UnlinkDiscordSubcommand extends SubCommand {

    private final GlobalTagAPI api;

    public UnlinkDiscordSubcommand(GlobalTagAPI api) {
        super("unlink");
        this.api = api;
    }

    @Override
    public boolean execute(String s, String[] strings) {
        api.getApiHandler().unlinkDiscord((response) -> displayMessage(
            GlobalTagAddon.prefix.copy().append(Util.getResponseComponent(response)))
        );
        return true;
    }
}
