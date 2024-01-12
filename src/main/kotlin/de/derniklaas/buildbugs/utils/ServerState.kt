package de.derniklaas.buildbugs.utils

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket
import de.derniklaas.buildbugs.Constants

data class ServerState(
    val type: String, val subType: String, val associatedGame: String, val legacyGameId: String, val mapName: String
) {
    companion object {
        val UNKNOWN = ServerState("Unknown", "Unknown", "Unknown", "Unknown", "Unknown")

        fun fromPacket(packet: ClientboundMccServerPacket): ServerState {
            return ServerState(packet.type, packet.subType, packet.associatedGame, packet.legacyGameId, "")
        }
    }

    /**
     * Returns the fancy name of the current location.
     *
     * If it's a game lobby, it will return the game name and add "lobby" to it.
     * If it's not known, it will return the [type] and [subType]
     */
    fun getFancyName() = when (type) {
        Constants.LOBBY -> "Lobby"
        // Game Lobbies
        Constants.GAME_LOBBY -> when (subType) {
            Constants.PARKOUR_WARRIOR -> "Parkour Warrior Lobby"
            Constants.HOLE_IN_THE_WALL -> "HITW Lobby"
            Constants.TO_GET_TO_THE_OTHER_SIDE -> "TGTTOS Lobby"
            Constants.BATTLE_BOX -> "Battle Box Lobby"
            Constants.SKY_BATTLE -> "Sky Battle Lobby"
            Constants.DYNABALL -> "Dynaball Lobby"
            else -> "$subType Lobby"
        }

        // Game modes
        Constants.PARKOUR_WARRIOR -> "Parkour Warrior"
        Constants.HOLE_IN_THE_WALL -> "HITW"
        Constants.TO_GET_TO_THE_OTHER_SIDE -> "TGTTOS"
        Constants.BATTLE_BOX -> "Battle Box"
        Constants.SKY_BATTLE -> "Sky Battle"
        Constants.DYNABALL -> "Dynaball"

        // Event gametypes
        "limbo" -> ""
        UNKNOWN.type -> ""

        else -> "$type $subType"
    }

    /**
     * Creates a copy of this [ServerState] with the [mapName] set to [name].
     */
    fun withMapName(name: String) = copy(mapName = name)
}
