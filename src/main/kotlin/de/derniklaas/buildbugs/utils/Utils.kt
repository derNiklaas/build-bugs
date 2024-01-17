package de.derniklaas.buildbugs.utils

import de.derniklaas.buildbugs.BuildBugsClientEntrypoint
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.minecraft.client.MinecraftClient

object Utils {

    /**
     * Sends a [message] in the MiniMessage format.
     * See https://docs.advntr.dev/minimessage/format.html for the specs.
     */
    fun sendMiniMessage(message: String, prefix: Boolean = true, vararg replacements: TagResolver) {
        val player = MinecraftClient.getInstance().player ?: return
        val mm = MiniMessage.miniMessage()

        val parsed = mm.deserialize("${if (prefix) "<gold>[Build Bugs]</gold> " else ""}$message", *replacements)
        player.sendMessage(parsed)
    }

    /**
     * Sends the [message] in green.
     */
    fun sendSuccessMessage(message: String, prefix: Boolean = true, vararg replacements: TagResolver) {
        sendMiniMessage("<green>$message</green>", prefix, *replacements)
    }

    /**
     * Sends the [message] in red.
     */
    fun sendErrorMessage(message: String, prefix: Boolean = true, vararg replacements: TagResolver) {
        sendMiniMessage("<red>$message</red>", prefix, *replacements)
    }

    /**
     * Sends the [message] in gray.
     */
    fun sendDebugMessage(message: String, prefix: Boolean = true, vararg replacements: TagResolver) {
        if (BuildBugsClientEntrypoint.config.debugMode) {
            sendMiniMessage("<gray>$message</gray>", prefix, *replacements)
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
