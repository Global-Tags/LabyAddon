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
            if (!response.isSuccessful()) {
                this.displayMessage(
                    Component.empty()
                        .append(GlobalTagAddon.prefix)
                        .append(Component.text(response.getError(), NamedTextColor.RED))
                );
                return;
            }
            Component component = Component.empty()
                .append(GlobalTagAddon.prefix)
                .append(Component.text(response.getData().message, NamedTextColor.GREEN));

            if (response.getData().getExpiresAt() != null) {
                component
                    .append(Component.space())
                    .append(Component.translatable(
                        this.getTranslationKey("expiration"),
                        NamedTextColor.GREEN,
                        Component.text(
                            dateFormat.format(response.getData().getExpiresAt()),
                            NamedTextColor.AQUA
                        )
                    ));
            }

            this.displayMessage(component);
            this.api.getCache().renewSelf();
            Util.broadcastTagUpdate();
        });
        return true;
    }
}
