package de.derniklaas.buildbugs

import com.noxcrew.noxesium.network.clientbound.ClientboundMccGameStatePacket
import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket
import de.derniklaas.buildbugs.utils.ServerState
import de.derniklaas.buildbugs.utils.Utils
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.Clipboard
import net.minecraft.text.ClickEvent
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.BlockPos

object BugCreator {

    private var gameState: ServerState = ServerState.UNKNOWN
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
            Utils.sendChatMessage("You are not connected to a server.", Formatting.RED)
            return
        }

        // Check if the player is connected to a MCC related server
        if (!Utils.isOnMCCServer()) {
            Utils.sendChatMessage("You are not connected to a MCC related server.", Formatting.RED)
            return
        }

        // Get the "area" of the player
        val area = gameState.getFancyName()
        val blockPos = player.blockPos
        val map = gameState.mapName

        val minecraftMessage = getCopyMessage(area, map, blockPos)
        val discordMessage = getCopyMessage(area, map, blockPos, true)

        val message = Text.literal(minecraftMessage).styled {
            it.withColor(Formatting.WHITE).withClickEvent(
                ClickEvent(
                    ClickEvent.Action.COPY_TO_CLIPBOARD, discordMessage
                )
            )
        }.append(Text.literal("[CLICK TO COPY]").styled {
            it.withColor(Formatting.YELLOW).withBold(true).withClickEvent(
                ClickEvent(
                    ClickEvent.Action.COPY_TO_CLIPBOARD, discordMessage
                )
            )
        })
        Utils.sendChatMessage(message)

        if (BuildBugsClientEntrypoint.config.copyToClipboard) {
            setClipboard(client, discordMessage)
        }
    }

    /**
     * Updates [gameState] when a new packet is received.
     */
    fun handleServerStatePacket(packet: ClientboundMccServerPacket) {
        gameState = ServerState.fromPacket(packet)
    }

    /**
     * Updates the map saved in [gameState] when a new packet is received.
     */
    fun handleGameStatePacket(packet: ClientboundMccGameStatePacket) {
        gameState = gameState.withMapName(packet.mapName)
    }

    private fun getCopyMessage(area: String, map: String, position: BlockPos, discord: Boolean = false): String {
        val start = if (Utils.isOnIsland()) "$area, " else ""
        val codeBlock = if (!discord) "" else "``"
        return "[$start${if (map != "") map else "$codeBlock${position.x} ${position.y} ${position.z}$codeBlock"}] "
    }

    /**
     * Prints the current [gameState] to the chat.
     */
    fun printCurrentGameState() {
        Utils.sendChatMessage("Current gameState object: $gameState", Formatting.GRAY)
    }

    /**
     * Sets the content of the Clipboard to [text].
     */
    fun setClipboard(client: MinecraftClient, text: String) {
        clipboard.setClipboard(client.window.handle, text)
        Utils.sendChatMessage("Copied ${if (BuildBugsClientEntrypoint.config.debugMode) "'$text' " else ""}to clipboard.", Formatting.GREEN)
    }
}
