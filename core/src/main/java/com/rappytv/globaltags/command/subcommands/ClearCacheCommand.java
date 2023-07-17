package com.rappytv.globaltags.command.subcommands;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.util.Util;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.util.I18n;

public class ClearCacheCommand extends SubCommand {

    public ClearCacheCommand() {
        super("clearcache", "cc");
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        displayMessage(GlobalTagAddon.prefix + (Util.clearCache(false) ? "§a" + I18n.translate("globaltags.notifications.cacheCleared") : "§c" + I18n.translate("globaltags.notifications.cacheEmpty")));
        return true;
    }
}
