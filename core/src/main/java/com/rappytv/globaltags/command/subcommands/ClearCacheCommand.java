package com.rappytv.globaltags.command.subcommands;

import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.GlobalTagAddon;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public class ClearCacheCommand extends SubCommand {

    private final GlobalTagAPI api;

    public ClearCacheCommand(GlobalTagAPI api) {
        super("clearcache", "cc");
        this.api = api;
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        api.getCache().clear();
        api.getCache().resolveSelf();
        displayMessage(
            Component.empty()
                .append(GlobalTagAddon.prefix)
                .append(Component.translatable(
                    "globaltags.commands.clear_cache.success",
                    NamedTextColor.GREEN
                ))
        );
        return true;
    }
}
