package de.derniklaas.buildbugs.utils

import de.derniklaas.buildbugs.BuildBugsClientEntrypoint
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object Utils {

    /**
     * Sends a [message] to the player.
     */
    fun sendChatMessage(message: String, color: Formatting = Formatting.WHITE) {
        val msg = Text.literal(message).styled {
            it.withColor(color)
        }
        sendChatMessage(msg)
    }

    /**
     * Sends a [message] to the player.
     */
    fun sendChatMessage(message: Text) {
        val prefix = Text.literal("[Build Bugs] ").styled {
            it.withColor(Formatting.GOLD)
        }
        MinecraftClient.getInstance().inGameHud.chatHud.addMessage(prefix.append(message))
    }

    fun getFormattedText(message: Text): Text {
        val prefix = Text.literal("[Build Bugs] ").styled {
            it.withColor(Formatting.GOLD)
        }
        return prefix.append(message)
    }

    fun getFormattedText(message: String, color: Formatting = Formatting.WHITE): Text {
        val msg = Text.literal(message).styled {
            it.withColor(color)
        }
        return getFormattedText(msg)
    }

    /**
     * Checks if the player is connected to a MCC related server.
     */
    fun isOnMCCServer(): Boolean {
        val server = MinecraftClient.getInstance().currentServerEntry ?: return false
        return isOnIsland() || server.address == BuildBugsClientEntrypoint.config.eventIP
    }

    /**
     * Checks if the player is connected to MCC Island.
     */
    fun isOnIsland(): Boolean {
        val server = MinecraftClient.getInstance().currentServerEntry ?: return false
        return server.address.endsWith("mccisland.net") || server.address.endsWith("mccisland.com")
    }

    fun sendDebugMessage(message: String) {
        if(BuildBugsClientEntrypoint.config.debugMode) {
            sendChatMessage(message, Formatting.GRAY)
        }
    }
}
