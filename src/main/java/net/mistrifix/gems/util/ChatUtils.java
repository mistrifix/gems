package net.mistrifix.gems.util;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class ChatUtils {

    public static String colored(@NotNull String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
