package com.rappytv.globaltags.command.subcommands;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.api.Util;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public class VerifyCommand extends SubCommand {

    private final GlobalTagAPI api;

    public VerifyCommand(GlobalTagAPI api) {
        super("verify");
        this.api = api;
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        switch (arguments.length > 0 ? arguments[0].toLowerCase() : "none") {
            case "email":
                if(arguments.length < 2) {
                    displayMessage(
                        Component.empty()
                            .append(GlobalTagAddon.prefix)
                            .append(Component.translatable(
                                "globaltags.commands.usage",
                                NamedTextColor.RED,
                                Component.text("/gt verify email <code>")
                            ))
                    );
                    return true;
                }

                api.getApiHandler().verifyEmail(arguments[1], (response) -> displayMessage(
                        GlobalTagAddon.prefix.copy().append(Util.getResponseComponent(response))
                ));
                break;
            default:
                displayMessage(
                    Component.empty()
                        .append(GlobalTagAddon.prefix)
                        .append(Component.translatable(
                            "globaltags.commands.usage",
                            NamedTextColor.RED,
                            Component.text("/gt verify email <code>")
                        ))
                );
        }
        return true;
    }
}
