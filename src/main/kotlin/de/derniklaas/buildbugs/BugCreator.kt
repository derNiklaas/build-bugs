package de.derniklaas.buildbugs

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket
import de.derniklaas.buildbugs.utils.ServerState
import de.derniklaas.buildbugs.utils.Utils
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.Clipboard
import net.minecraft.util.math.BlockPos

object BugCreator {

    var gameState: ServerState = ServerState.UNKNOWN
        private set
    private val clipboard: Clipboard = Clipboard()

    /**
     * Gathers information to more easily create a build bug report.
     */
    fun report() {
        val client = MinecraftClient.getInstance()
        val server = client.currentServerEntry ?: return
        val player = client.player ?: return

        // Check if the player is connected to a server
        if (server.isLocal) {
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
        val blockPos = player.blockPos
        val map = gameState.mapName

        val minecraftMessage = getCopyMessage(area, map, blockPos).trim()
        val discordMessage = getCopyMessage(area, map, blockPos, true)
        Utils.sendMiniMessage(
            "<click:copy_to_clipboard:'${
                discordMessage.replace(
                    "'",
                    "\\\'"
                )
            }'>$minecraftMessage <yellow><bold>[CLICK TO COPY]</bold></yellow></click>", true
        )

        if (BuildBugsClientEntrypoint.config.copyToClipboard) {
            setClipboard(client, discordMessage)
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
        return "[$start${if (map.isNotBlank() && map != ServerState.UNKNOWN.mapName) map else "$codeBlock${position.x} ${position.y} ${position.z}$codeBlock"}] "
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
    fun setClipboard(client: MinecraftClient, text: String) {
        clipboard.setClipboard(client.window.handle, text)
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
