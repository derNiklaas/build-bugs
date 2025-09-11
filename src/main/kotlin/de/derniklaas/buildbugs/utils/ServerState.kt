package de.derniklaas.buildbugs.utils

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import de.derniklaas.buildbugs.Constants

data class ServerState(
    val server: String, val types: List<String>, val mapName: String
) {
    companion object {
        val UNKNOWN = ServerState(Constants.UNKNOWN, listOf(Constants.UNKNOWN), Constants.UNKNOWN)

        fun fromPacket(packet: ClientboundMccServerPacket): ServerState {
            val isLobby = packet.types.any { it in Constants.LOBBIES }
            return ServerState(
                packet.server, packet.types, if (isLobby) "" else "Pre Game"
            )
        }
    }

    /**
     * Returns the fancy name of the current location.
     *
     * If it's a game lobby, it will return the game name and add "Lobby" to it.
     * If it's not known, it will return all types
     */
    fun getFancyName(): String {
        val game = MCCGame.entries.firstOrNull { game ->
            game.types.any { it in types }
        } ?: MCCGame.UNKNOWN

        var output = game.displayName

        if (Constants.LOBBY in types && Constants.FISHING !in types) {
            output += if (output.isNotEmpty()) " " else ""
            output += "Lobby"
        }

        val fishingIsland = FishingIsland.entries.firstOrNull { island ->
            island.type in types
        } ?: FishingIsland.UNKNOWN

        if (fishingIsland != FishingIsland.UNKNOWN) {
            output = fishingIsland.displayName
        }

        if (output.isEmpty()) {
            return types.joinToString(", ")
        }
        return output
    }

    /**
     * Creates a copy of this [ServerState] with the [mapName] set to [name].
     */
    fun withMapName(name: String) = copy(mapName = name)

    fun miniMessageString(): String {
        return "server: <green>$server</green>, types: <green>[${types.joinToString(", ")}]</green>, map: <green>$mapName</green>"
    }
}
