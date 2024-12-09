package com.rappytv.globaltags.command.subcommands;

import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.Util;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public class UnlinkSubcommand extends SubCommand {

    private final GlobalTagAPI api;

    public UnlinkSubcommand(GlobalTagAPI api) {
        super("unlink");
        this.api = api;
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        switch (arguments.length > 0 ? arguments[0].toLowerCase() : "none") {
            case "discord":
                this.api.getApiHandler().unlinkDiscord((response) -> this.displayMessage(
                    GlobalTagAddon.prefix.copy().append(Util.getResponseComponent(response)))
                );
                break;
            case "email":
                this.api.getApiHandler().unlinkEmail((response) -> this.displayMessage(
                    GlobalTagAddon.prefix.copy().append(Util.getResponseComponent(response)))
                );
                break;
            default:
                this.displayMessage(
                    Component.empty()
                        .append(GlobalTagAddon.prefix)
                        .append(Component.translatable(
                            "globaltags.commands.usage",
                            NamedTextColor.RED,
                            Component.text("/gt unlink <discord/email>")
                        ))
                );
        }
        return true;
    }
}
