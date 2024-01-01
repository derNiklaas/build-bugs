package de.derniklaas.buildbugs.utils

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket

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
        // ------ MCC Island -------
        "lobby" -> "Lobby"
        // Game Lobbies
        "lobby-game" -> when (subType) {
            "parkour-warrior" -> "Parkour Warrior Lobby"
            "hitw" -> "HITW Lobby"
            "tgttos" -> "TGTTOS Lobby"
            "battle-box" -> "Battle Box Lobby"
            "sky-battle" -> "Sky Battle Lobby"
            "dynaball" -> "Dynaball Lobby"
            else -> "$subType Lobby"
        }

        // Game modes
        "parkour-warrior" -> "Parkour Warrior"
        "hole-in-the-wall" -> "HITW"
        "tgttos" -> "TGTTOS"
        "dynaball" -> "Dynaball"
        "battle-box" -> "Battle Box"
        "sky-battle" -> "Sky Battle"

        // ------ MCC Event -------
        // Event doesn't have types yet, so we ignore it for now
        "limbo" -> ""

        else -> "$type $subType"
    }

    /**
     * Creates a copy of this [ServerState] with the [mapName] set to [name].
     */
    fun withMapName(name: String) = copy(mapName = name)
}
