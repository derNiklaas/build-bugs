package de.derniklaas.buildbugs.utils

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import de.derniklaas.buildbugs.Constants

data class ServerState(
    val server: String, val types: Set<String>, val mapName: String
) {
    companion object {
        val UNKNOWN = ServerState(Constants.UNKNOWN, setOf(Constants.UNKNOWN), Constants.UNKNOWN)

        fun fromPacket(packet: ClientboundMccServerPacket): ServerState {
            val isLobby = packet.server in Constants.LOBBIES
            val isFishing = packet.server == Constants.FISHING
            return ServerState(
                packet.server, packet.types.toSet(), if (isLobby || isFishing) "" else "Pre Game"
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
            server in game.types
        } ?: MCCGame.UNKNOWN

        Utils.sendDebugMessage("Determining fancy name for server='$server', types=$types, mapName='$mapName', detected game='${game}'")
        val output = when (game) {
            MCCGame.LOBBY -> {
                val gameMode = MCCGame.entries.firstOrNull { game ->
                    types.any { it in game.types }
                } ?: MCCGame.UNKNOWN

                if (gameMode != MCCGame.LOBBY) {
                    "${gameMode.displayName} Lobby"
                } else if (Constants.MAIN_LOBBY in types) {
                    "Main Lobby"
                } else {
                    ""
                }
            }

            MCCGame.FISHING -> {
                val fishingIsland = FishingIsland.entries.firstOrNull { island ->
                    island.type in types
                } ?: FishingIsland.UNKNOWN

                fishingIsland.displayName
            }

            else -> {
                val gameMode = MCCGame.entries.firstOrNull { game ->
                    types.any { it in game.types }
                } ?: MCCGame.UNKNOWN

                gameMode.displayName
            }
        }

        if (output.isEmpty()) {
            return "$server - ${types.joinToString(", ")}"
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
