package eu.quantumsociety.deltacraft.utils

import eu.quantumsociety.deltacraft.utils.enums.Permissions
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

        @JvmStatic
        fun createActionButton(button: Array<BaseComponent>, color: ChatColor = ChatColor.DARK_AQUA): Array<BaseComponent> {
            return ComponentBuilder("")
                    .append("[ ").color(ChatColor.WHITE).bold(true)
                    .append(button).color(color).bold(false)
                    .append(" ]").color(ChatColor.WHITE).bold(true)
                    .create()
        }

        @JvmStatic
        fun insufficientPermissions(permission: Permissions): Array<BaseComponent> {
            return this.insufficientPermissions("Insufficient permissions!", permission.path)
        }

        @JvmStatic
        fun insufficientPermissions(customMsg: String = "Insufficient permissions!", permission: String = "¯\\_(ツ)_/¯"): Array<BaseComponent> {
            return ComponentBuilder("")
                    .append(customMsg).color(ChatColor.DARK_RED)
                    .event(HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder("Missing permission: '$permission'").create()))
                    .create()
        }

        @JvmStatic
        fun infoText(text: String): Array<BaseComponent> {
            return this.infoText(text, ChatColor.YELLOW)
        }

        @JvmStatic
        fun infoText(text: String, color: ChatColor? = ChatColor.YELLOW): Array<BaseComponent> {
            return ComponentBuilder(text)
                    .color(color)
                    .create()
        }

        @JvmStatic
        fun attentionText(text: String): Array<BaseComponent> {
            return ComponentBuilder().append(infoText(text))
                    .bold(true)
                    .create()
        }

        @JvmStatic
        fun varText(text: String): Array<BaseComponent> {
            return ComponentBuilder(text)
                    .color(ChatColor.WHITE)
                    .create()
        }
    }
}