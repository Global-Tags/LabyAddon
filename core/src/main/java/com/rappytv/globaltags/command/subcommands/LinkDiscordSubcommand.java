package com.rappytv.globaltags.command.subcommands;

import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.GlobalTagAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;

public class LinkDiscordSubcommand extends SubCommand {

    private final GlobalTagAPI api;

    public LinkDiscordSubcommand(GlobalTagAPI api) {
        super("link");
        this.api = api;
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        switch (arguments.length > 0 ? arguments[0].toLowerCase() : "none") {
            case "discord":
                api.getApiHandler().linkDiscord((info) -> {
                    if (info.successful()) {
                        String code = info.data();
                        Laby.references().chatExecutor().copyToClipboard(code);
                        displayMessage(GlobalTagAddon.prefix.copy().append(Component.translatable(
                            "globaltags.commands.link.discord.copied",
                            NamedTextColor.GREEN
                        )));
                    } else {
                        displayMessage(
                            GlobalTagAddon.prefix.copy().append(Component.text(
                                info.data(),
                                NamedTextColor.RED
                            ))
                        );
                    }
                });
                break;
            case "email":
                if (arguments.length < 2) {
                    displayMessage(
                        Component.empty()
                            .append(GlobalTagAddon.prefix)
                            .append(Component.translatable(
                                "globaltags.commands.usage",
                                NamedTextColor.RED,
                                Component.text("/gt link email <address>")
                            ))
                    );
                    return true;
                }

                api.getApiHandler().linkEmail(arguments[1], (info) -> {
                    if (info.successful()) {
                        displayMessage(
                            Component.empty()
                                .append(GlobalTagAddon.prefix)
                                .append(Component.text(info.data(), NamedTextColor.GREEN))
                                .append(Component.newline())
                                .append(GlobalTagAddon.prefix)
                                .append(
                                    Component.translatable(
                                            "globaltags.commands.link.email.verify",
                                            NamedTextColor.AQUA
                                        )
                                        .decorate(TextDecoration.UNDERLINED)
                                        .clickEvent(ClickEvent.suggestCommand("/gt verify email "))
                                        .hoverEvent(HoverEvent.showText(
                                            Component.translatable(
                                                "globaltags.commands.link.email.hover")
                                        ))
                                )
                        );
                    } else {
                        displayMessage(
                            Component.empty()
                                .append(GlobalTagAddon.prefix)
                                .append(Component.text(info.data(), NamedTextColor.RED))
                        );
                    }
                });
                break;
            default:
                displayMessage(
                    Component.empty()
                        .append(GlobalTagAddon.prefix)
                        .append(Component.translatable(
                            "globaltags.commands.usage",
                            NamedTextColor.RED,
                            Component.text("/gt link <discord/email>")
                        ))
                );
        }
        return true;
    }
}
