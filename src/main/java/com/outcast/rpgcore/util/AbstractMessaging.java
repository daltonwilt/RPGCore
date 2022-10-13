package com.outcast.rpgcore.util;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;

//===========================================================================================================
// An abstraction that helps facilitate messaging facades
//===========================================================================================================

public class AbstractMessaging {

    private String prefix;

    public AbstractMessaging(String prefix) {
        this.prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + prefix + ChatColor.DARK_GRAY + "] ";
    }

    public TextComponent formatInfo(Object... message) {
        return Component.text(prefix + ChatColor.DARK_AQUA + message.toString());
    }

    public TextComponent formatError(Object... message) {
        return Component.text(prefix + ChatColor.RED + message.toString());
    }

    public void info(Audience player, Object... message) {
        player.sendMessage(formatInfo(message));
    }

    public void error(Audience player, Object... message) {
        player.sendMessage(formatError(message));
    }
}
