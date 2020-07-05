package eu.quantumsociety.deltacraft.utils

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ComponentBuilder

class TextHelper {
    companion object {
        @JvmStatic
        fun getDivider(): Array<BaseComponent> {
            val divider = "===================================="
            return ComponentBuilder(divider).color(ChatColor.GRAY).create();
        }

        fun createActionButton(button: Array<BaseComponent>, color: ChatColor = ChatColor.DARK_AQUA): Array<BaseComponent> {
            return ComponentBuilder("")
                    .append("[ ").color(ChatColor.DARK_GRAY).bold(true)
                    .append(button).color(color)
                    .append(" ]").color(ChatColor.DARK_GRAY).bold(true)
                    .create()
        }
    }
}