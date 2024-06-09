package com.rappytv.globaltags.command.subcommands;

import com.rappytv.globaltags.GlobalTagAddon;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public class ClearCacheCommand extends SubCommand {

    public ClearCacheCommand() {
        super("clearcache", "cc");
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        displayMessage(
            Component.empty()
                .append(GlobalTagAddon.prefix)
                .append(Component.translatable("globaltags.notifications.cacheCleared", NamedTextColor.GREEN))
        );
        return true;
    }
}
