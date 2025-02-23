package com.rappytv.globaltags.command.subcommands;

import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.api.Util;
import java.text.SimpleDateFormat;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public class RedeemCommand extends SubCommand {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private final GlobalTagAPI api;

    public RedeemCommand(GlobalTagAPI api) {
        super("redeem");
        this.api = api;

        this.translationKey("globaltags.commands.redeem");
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        if (arguments.length < 1) {
            this.displayMessage(
                Component.empty()
                    .append(GlobalTagAddon.prefix)
                    .append(Component.translatable(
                        this.getTranslationKey("enterCode"),
                        NamedTextColor.RED
                    ))
            );
            return true;
        }
        String code = arguments[0];
        this.api.getApiHandler().redeemGiftCode(code, (response) -> {
            Component component = Component.empty()
                .append(GlobalTagAddon.prefix)
                .append(Util.getResponseComponent(response));

            // Append expiration date if available
//            if(response.isSuccessful() && response.getData().getExpiration() != null) {
//                component
//                    .append(Component.newline())
//                    .append(GlobalTagAddon.prefix)
//                    .append(Component.translatable(
//                        this.getTranslationKey("expiration"),
//                        NamedTextColor.AQUA,
//                        dateFormat.format(response.getData().getExpiration())
//                    ));
//            }

            this.displayMessage(component);
        });
        return true;
    }
}
