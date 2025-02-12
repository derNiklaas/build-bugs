package de.derniklaas.buildbugs

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket
import de.derniklaas.buildbugs.utils.ServerState
import de.derniklaas.buildbugs.utils.Utils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.font.TextFieldHelper
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.core.BlockPos

object BugCreator {

    var gameState: ServerState = ServerState.UNKNOWN
        private set

    /**
     * Gathers information to more easily create a build bug report.
     */
    fun report() {
        val client = Minecraft.getInstance()
        val server = client.currentServer ?: return
        val player = client.player ?: return

        // Check if the player is connected to a server
        if (server.type() == ServerData.Type.LAN) {
            Utils.sendErrorMessage("You are not connected to a server.")
            return
        }

        // Check if the player is connected to a MCC related server
        if (!Utils.isOnMCCServer()) {
            Utils.sendErrorMessage("You are not connected to a MCC related server.")
            return
        }

        // Get the "area" of the player
        val area = gameState.getFancyName()
        val blockPos = player.blockPosition()
        val map = gameState.mapName

        val minecraftMessage = getCopyMessage(area, map, blockPos).trim()
        val discordMessage = getCopyMessage(area, map, blockPos, true)
        Utils.sendMiniMessage(
            "<click:copy_to_clipboard:'${
                discordMessage.replace(
                    "'", "\\\'"
                )
            }'>$minecraftMessage <yellow><bold>[CLICK TO COPY]</bold></yellow></click>", true
        )

        if (BuildBugsClientEntrypoint.config.copyToClipboard) {
            setClipboard(discordMessage)
        }
    }

    /**
     * Updates [gameState] when a new packet is received.
     */
    fun handleServerStatePacket(packet: ClientboundMccServerPacket, printState: Boolean) {
        gameState = ServerState.fromPacket(packet)
        if (printState) {
            printCurrentGameState()
        }
    }

    /**
     * Updates the map saved in [gameState] with the given [name].
     */
    fun updateMap(name: String) {
        gameState = gameState.withMapName(name)
        printCurrentGameState()
    }

    private fun getCopyMessage(area: String, map: String, position: BlockPos, discord: Boolean = false): String {
        val start = if (area.isNotBlank()) "$area, " else ""
        val codeBlock = if (!discord) "" else "`"
        return "[$start${if (map.isNotBlank() && map != Constants.UNKNOWN) map else "$codeBlock${position.x} ${position.y} ${position.z}$codeBlock"}] "
    }

    /**
     * Prints the current [gameState] to the chat.
     */
    fun printCurrentGameState() {
        Utils.sendDebugMessage("Current gameState: ${gameState.miniMessageString()}")
    }

    /**
     * Sets the content of the Clipboard to [text].
     */
    fun setClipboard(text: String) {
        TextFieldHelper.setClipboardContents(Minecraft.getInstance(), text)
        Utils.sendMiniMessage("<i>Copied </i>${if (BuildBugsClientEntrypoint.config.debugMode) "<green>${text.trim()}</green> " else ""}<i>to clipboard.</i>")
    }

    /**
     * Resets the [gameState] to [ServerState.UNKNOWN].
     */
    fun resetGameState() {
        gameState = ServerState.UNKNOWN
        Utils.sendDebugMessage("Reset gameState.")
    }

    /**
     * Forces the [gameState] to the given [type], [subType] and [map].
     */
    fun forceGameState(type: String, subType: String, map: String) {
        gameState = ServerState(type, subType, map)
        Utils.sendDebugMessage("Forced gameState to ${gameState.miniMessageString()}")
    }
}
