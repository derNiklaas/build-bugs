package de.derniklaas.buildbugs.utils

import de.derniklaas.buildbugs.BuildBugsClientEntrypoint
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minecraft.client.MinecraftClient

object Utils {

    /**
     * Sends a [message] in the MiniMessage format.
     * See https://docs.advntr.dev/minimessage/format.html for the specs.
     */
    fun sendMiniMessage(message: String, prefix: Boolean = true) {
        val player = MinecraftClient.getInstance().player ?: return
        val mm = MiniMessage.miniMessage()

        val parsed = mm.deserialize("${if (prefix) "<gold>[Build Bugs]</gold> " else ""}$message")
        player.sendMessage(parsed)
    }

    /**
     * Sends the [message] in green.
     */
    fun sendSuccessMessage(message: String, prefix: Boolean = true) {
        sendMiniMessage("<green>$message</green>", prefix)
    }

    /**
     * Sends the [message] in red.
     */
    fun sendErrorMessage(message: String, prefix: Boolean = true) {
        sendMiniMessage("<red>$message</red>", prefix)
    }

    /**
     * Sends the [message] in gray.
     */
    fun sendDebugMessage(message: String, prefix: Boolean = true) {
        if (BuildBugsClientEntrypoint.config.debugMode) {
            sendMiniMessage("<gray>$message</gray>", prefix)
        }
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
}
