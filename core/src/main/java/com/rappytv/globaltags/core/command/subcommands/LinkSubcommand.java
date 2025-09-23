package com.rappytv.globaltags.core.command.subcommands;

import com.rappytv.globaltags.api.GlobalTagAPI;
import com.rappytv.globaltags.core.GlobalTagsAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextDecoration;

public class LinkSubcommand extends SubCommand {

    private final GlobalTagAPI api;

    public LinkSubcommand(GlobalTagAPI api) {
        super("link");
        this.api = api;
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        switch (arguments.length > 0 ? arguments[0].toLowerCase() : "none") {
            case "discord":
                this.api.getApiHandler().linkDiscord((info) -> {
                    if (info.isSuccessful()) {
                        String code = info.getData();
                        Laby.references().chatExecutor().copyToClipboard(code);
                        this.displayMessage(GlobalTagsAddon.prefix().append(Component.translatable(
                            "globaltags.commands.link.discord.copied",
                            NamedTextColor.GREEN
                        )));
                    } else {
                        this.displayMessage(
                            GlobalTagsAddon.prefix().append(Component.text(
                                info.getError(),
                                NamedTextColor.RED
                            ))
                        );
                    }
                });
                break;
            case "email":
                if (arguments.length < 2) {
                    this.displayMessage(
                        Component.empty()
                            .append(GlobalTagsAddon.prefix())
                            .append(Component.translatable(
                                "globaltags.commands.usage",
                                NamedTextColor.RED,
                                Component.text("/gt link email <address>")
                            ))
                    );
                    return true;
                }

                this.api.getApiHandler().linkEmail(arguments[1], (info) -> {
                    if (info.isSuccessful()) {
                        this.displayMessage(
                            Component.empty()
                                .append(GlobalTagsAddon.prefix())
                                .append(Component.text(info.getData(), NamedTextColor.GREEN))
                                .append(Component.newline())
                                .append(GlobalTagsAddon.prefix())
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
                        this.displayMessage(
                            Component.empty()
                                .append(GlobalTagsAddon.prefix())
                                .append(Component.text(info.getError(), NamedTextColor.RED))
                        );
                    }
                });
                break;
            default:
                this.displayMessage(
                    Component.empty()
                        .append(GlobalTagsAddon.prefix())
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
