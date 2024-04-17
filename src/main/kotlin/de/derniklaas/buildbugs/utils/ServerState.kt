package de.derniklaas.buildbugs.utils

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket
import de.derniklaas.buildbugs.Constants

data class ServerState(
    val type: String, val subType: String, val mapName: String
) {
    companion object {
        val UNKNOWN = ServerState("Unknown", "Unknown", "Unknown")

        fun fromPacket(packet: ClientboundMccServerPacket): ServerState {
            return ServerState(
                packet.type,
                packet.subType,
                if (packet.type in listOf(Constants.LOBBY, Constants.GAME_LOBBY)) "" else "Pre Game"
            )
        }
    }

    /**
     * Returns the fancy name of the current location.
     *
     * If it's a game lobby, it will return the game name and add "Lobby" to it.
     * If it's not known, it will return the [type] and [subType]
     */
    fun getFancyName(type: String = this.type): String = when (type) {
        Constants.LOBBY -> "Lobby"
        // Game Lobbies
        Constants.GAME_LOBBY -> {
            // realistically this will never happen, but I don't want infinite recursion
            if (subType == Constants.GAME_LOBBY) "Game Lobby?"
            else "${getFancyName(subType)} Lobby"
        }

        // Game modes
        Constants.PARKOUR_WARRIOR -> "Parkour Warrior"
        Constants.HOLE_IN_THE_WALL -> "HITW"
        Constants.TO_GET_TO_THE_OTHER_SIDE -> "TGTTOS"
        Constants.BATTLE_BOX -> "Battle Box"
        Constants.SKY_BATTLE -> "Sky Battle"
        Constants.DYNABALL -> "Dynaball"
        Constants.ROCKET_SPLEEF_RUSH -> "Rocket Spleef"

        // Event gametypes
        "limbo" -> ""
        UNKNOWN.type -> ""

        else -> "$type $subType"
    }

    /**
     * Creates a copy of this [ServerState] with the [mapName] set to [name].
     */
    fun withMapName(name: String) = copy(mapName = name)

    fun miniMessageString(): String {
        return "type: <green>$type</green>, subType: <green>$subType</green>, map: <green>$mapName</green>"
    }
}
