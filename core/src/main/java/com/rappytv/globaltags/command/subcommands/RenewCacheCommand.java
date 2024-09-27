package com.rappytv.globaltags.command.subcommands;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public class RenewCacheCommand extends SubCommand {

    private final GlobalTagAPI api;

    public RenewCacheCommand(GlobalTagAPI api) {
        super("renewcache", "renew", "rc");
        this.api = api;
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        api.getCache().renewAll();
        displayMessage(
            Component.empty()
                .append(GlobalTagAddon.prefix)
                .append(Component.translatable(
                    "globaltags.commands.renew_cache.success",
                    NamedTextColor.GREEN
                ))
        );
        return true;
    }
}
