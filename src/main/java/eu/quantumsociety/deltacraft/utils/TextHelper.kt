package eu.quantumsociety.deltacraft.utils

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent

class TextHelper {
    companion object {
        @JvmStatic
        fun getDivider(): Array<BaseComponent> {
            val divider = "===================================="
            return ComponentBuilder()
                    .append("\n")
                    .append(divider).color(ChatColor.GRAY)
                    .append("\n")
                    .create();
        }

        fun createActionButton(button: Array<BaseComponent>, color: ChatColor = ChatColor.DARK_AQUA): Array<BaseComponent> {
            return ComponentBuilder("")
                    .append("[ ").color(ChatColor.WHITE).bold(true)
                    .append(button).color(color).bold(false)
                    .append(" ]").color(ChatColor.WHITE).bold(true)
                    .create()
        }

        fun insufficientPermissions(permission: String = "¯\\_(ツ)_/¯"): Array<BaseComponent> {
            return ComponentBuilder("")
                    .append("Insufficient permissions!").color(ChatColor.DARK_RED)
                    .event(HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("Missing permission: '$permission'").create()))
                    .create()
        }

        fun infoText(text: String): Array<BaseComponent> {
            return ComponentBuilder(text)
                    .color(ChatColor.YELLOW)
                    .create()
        }

        fun varText(text: String): Array<BaseComponent> {
            return ComponentBuilder(text)
                    .color(ChatColor.WHITE)
                    .create()
        }
    }
}