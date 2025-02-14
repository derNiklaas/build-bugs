package de.derniklaas.buildbugs.utils

import de.derniklaas.buildbugs.BuildBugsClientEntrypoint
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.minecraft.client.Minecraft

object Utils {

    /**
     * Sends a [message] in the MiniMessage format.
     * See https://docs.advntr.dev/minimessage/format.html for the specs.
     */
    fun sendMiniMessage(message: String, prefix: Boolean = true, vararg replacements: TagResolver) {
        val player = Minecraft.getInstance().player ?: return
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
    fun isOnMCCServer() = isOnEventServer() || isOnIsland()

    /**
     * Checks if the player is connected to the server specified in the config.
     */
    fun isOnEventServer(): Boolean {
        val server = Minecraft.getInstance().currentServer ?: return false
        return server.ip == BuildBugsClientEntrypoint.config.eventIP
    }

    /**
     * Checks if the player is connected to MCC Island.
     */
    fun isOnIsland(): Boolean {
        val server = Minecraft.getInstance().currentServer ?: return false
        return server.ip.endsWith("mccisland.net") || server.ip.endsWith("mccisland.com")
    }
}
