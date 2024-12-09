package com.rappytv.globaltags.api;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.component.format.TextDecoration;
import net.labymod.api.util.Color;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.include.com.google.common.collect.BiMap;
import org.spongepowered.include.com.google.common.collect.HashBiMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Originally from net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer
 */
public class GlobalTagDeserializer {

    private static final char RESET_CHAR = 'r';
    private static final GlobalTagDeserializer INSTANCE = new GlobalTagDeserializer();

    private static final BiMap<TextColor, Character> TEXT_COLOR_CHARS = HashBiMap.create(16);
    private static final BiMap<TextDecoration, Character> DECORATION_CHARS = HashBiMap.create(5);

    static {
        TEXT_COLOR_CHARS.put(NamedTextColor.BLACK, '0');
        TEXT_COLOR_CHARS.put(NamedTextColor.DARK_BLUE, '1');
        TEXT_COLOR_CHARS.put(NamedTextColor.DARK_GREEN, '2');
        TEXT_COLOR_CHARS.put(NamedTextColor.DARK_AQUA, '3');
        TEXT_COLOR_CHARS.put(NamedTextColor.DARK_RED, '4');
        TEXT_COLOR_CHARS.put(NamedTextColor.DARK_PURPLE, '5');
        TEXT_COLOR_CHARS.put(NamedTextColor.GOLD, '6');
        TEXT_COLOR_CHARS.put(NamedTextColor.GRAY, '7');
        TEXT_COLOR_CHARS.put(NamedTextColor.DARK_GRAY, '8');
        TEXT_COLOR_CHARS.put(NamedTextColor.BLUE, '9');
        TEXT_COLOR_CHARS.put(NamedTextColor.GREEN, 'a');
        TEXT_COLOR_CHARS.put(NamedTextColor.AQUA, 'b');
        TEXT_COLOR_CHARS.put(NamedTextColor.RED, 'c');
        TEXT_COLOR_CHARS.put(NamedTextColor.LIGHT_PURPLE, 'd');
        TEXT_COLOR_CHARS.put(NamedTextColor.YELLOW, 'e');
        TEXT_COLOR_CHARS.put(NamedTextColor.WHITE, 'f');

        DECORATION_CHARS.put(TextDecoration.BOLD, 'l');
        DECORATION_CHARS.put(TextDecoration.ITALIC, 'o');
        DECORATION_CHARS.put(TextDecoration.UNDERLINED, 'n');
        DECORATION_CHARS.put(TextDecoration.STRIKETHROUGH, 'm');
        DECORATION_CHARS.put(TextDecoration.OBFUSCATED, 'k');
    }

    private final char character = '&';

    private GlobalTagDeserializer() {
        // Static access only
    }

    public static Component deserialize(String input) {
        return INSTANCE.deserializeString(input);
    }

    public Component deserializeString(@Nullable String input) {
        if (input == null) {
            return Component.empty();
        }

        int nextSection = this.nextColor(input, input.length() - 1);
        if (nextSection == -1) {
            return Component.text(input);
        }

        boolean reset = false;
        int pos = input.length();

        TextComponent.Builder current = null;
        List<Component> parts = new ArrayList<>();

        do {
            char format;
            TextColor textColor = null;
            TextDecoration decoration = null;
            int from;

            char formatIndicator = input.charAt(nextSection);
            if (formatIndicator == this.character) {
                format = input.charAt(nextSection + 1);
                from = nextSection + 2;
            } else if (formatIndicator == '#') {
                format = formatIndicator;
                int nextHexEnd = input.indexOf('>', nextSection);
                if (nextHexEnd != -1) {
                    String hex = input.substring(nextSection, nextHexEnd);
                    int value;
                    try {
                        value = Integer.parseInt(hex.substring(1), 16);
                    } catch (IllegalArgumentException ignored) {
                        value = Color.WHITE.getValue();
                    }

                    textColor = TextColor.color(value);

                    from = nextHexEnd + 1;
                } else {
                    from = nextSection;
                }
            } else {
                format = RESET_CHAR;
                from = nextSection;
            }

            if (format != RESET_CHAR && format != '#') {
                textColor = TEXT_COLOR_CHARS.inverse().get(format);
                if (textColor == null) {
                    decoration = DECORATION_CHARS.inverse().get(format);
                }
            }

            if (format == RESET_CHAR || textColor != null || decoration != null) {
                if (from != pos) {
                    if (current != null) {
                        if (reset) {
                            parts.add(current.build());
                            reset = false;
                            current = Component.text();
                        } else {
                            current = Component.text().append(current.build());
                        }
                    } else {
                        current = Component.text();
                    }

                    current.text(input.substring(from, pos));
                } else if (current == null) {
                    current = Component.text();
                }

                if (!reset) {
                    if (format == RESET_CHAR) {
                        reset = true;
                    } else if (textColor != null) {
                        current.color(textColor);
                        reset = true;
                    } else {
                        current.decorate(decoration);
                    }
                }

                pos = formatIndicator == '#' ? nextSection - 1 : nextSection;
            }

            nextSection = this.nextColor(input, nextSection - 1);
        } while (nextSection != -1);

        if (current != null) {
            parts.add(current.build());
        }

        final String remaining = pos > 0 ? input.substring(0, pos) : "";
        if (parts.size() == 1 && remaining.isEmpty()) {
            return parts.getFirst();
        } else {
            Collections.reverse(parts);
            return Component.text().text(remaining).append(parts).build();
        }
    }

    private int nextColor(String input, int fromIndex) {
        if (fromIndex < 0) {
            return -1;
        }

        int nextSection = input.lastIndexOf(this.character, fromIndex);
        if (nextSection > input.length() - 2) {
            // there cannot be a legacy color code at the end of the string
            nextSection = -1;
        }

        int nextHexEnd = input.lastIndexOf('>', fromIndex);
        if (nextHexEnd == -1) {
            // no hex color, so use the next section
            return nextSection;
        }

        int nextHexStart = input.lastIndexOf("<#", nextHexEnd);
        if (nextHexStart == -1) {
            // no hex color, so use the next section
            return nextSection;
        }

        if (nextHexStart > nextSection) {
            return nextHexStart + 1;
        }

        return nextSection;
    }
}
