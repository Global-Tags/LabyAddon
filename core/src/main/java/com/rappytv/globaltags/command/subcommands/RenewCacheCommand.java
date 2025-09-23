package com.rappytv.globaltags.command.subcommands;

import com.rappytv.globaltags.GlobalTagsAddon;
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
        this.api.getCache().renewAll();
        this.displayMessage(
            Component.empty()
                .append(GlobalTagsAddon.prefix())
                .append(Component.translatable(
                    "globaltags.commands.renewCache.success",
                    NamedTextColor.GREEN
                ))
        );
        return true;
    }
}
