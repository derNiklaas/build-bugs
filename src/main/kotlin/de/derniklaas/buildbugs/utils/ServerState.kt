package de.derniklaas.buildbugs.utils

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket
import de.derniklaas.buildbugs.Constants

data class ServerState(
    val serverType: String, val subType: String, val mapName: String
) {
    companion object {
        val UNKNOWN = ServerState(Constants.UNKNOWN, Constants.UNKNOWN, Constants.UNKNOWN)

        fun fromPacket(packet: ClientboundMccServerPacket): ServerState {
            return ServerState(
                packet.serverType, packet.subType, if (packet.serverType in Constants.LOBBIES) "" else "Pre Game"
            )
        }
    }

    /**
     * Returns the fancy name of the current location.
     *
     * If it's a game lobby, it will return the game name and add "Lobby" to it.
     * If it's not known, it will return the [type] and [subType]
     */
    fun getFancyName(type: String = this.serverType): String = when (type) {
        Constants.LOBBY -> {
            if(type == this.subType) "Lobby Lobby?"
            when(subType) {
                "main" -> "Main Lobby"
                Constants.VERDANT_WOODS -> "Verdant Woods"
                Constants.FLORAL_FOREST -> "Floral Forest"
                Constants.DARK_GROVE -> "Dark Grove"
                Constants.SUNKEN_SWAMP -> "Sunken Swamp"
                Constants.TROPICAL_OVERGROWTH -> "Tropical Overgrowth"
                Constants.CORAL_SHORES -> "Coral Shores"
                Constants.TWISTED_SWAMP -> "Twisted Swamp"
                Constants.MIRRORED_OASIS -> "Mirrored Oasis"
                Constants.ANCIENT_SANDS -> "Ancient Sands"
                Constants.BLAZING_CANYON -> "Blazing Canyon"
                Constants.ASHEN_WASTES -> "Ashen Wastes"
                Constants.VOLCANIC_SPRINGS -> "Volcanic Springs"
                else -> "${getFancyName(subType)} Lobby"
            }
        }

        // Game modes
        Constants.PARKOUR_WARRIOR, "dojo" -> "Parkour Warrior"
        Constants.HOLE_IN_THE_WALL, Constants.HOLE_IN_THE_WALL_EVENT -> "HITW"
        Constants.TO_GET_TO_THE_OTHER_SIDE -> "TGTTOS"
        Constants.BATTLE_BOX -> "Battle Box"
        Constants.SKY_BATTLE -> "Sky Battle"
        Constants.DYNABALL -> "Dynaball"
        Constants.ROCKET_SPLEEF_RUSH -> "Rocket Spleef"

        // Event game types
        Constants.HUB -> "Hub"
        Constants.ACE_RACE -> "Ace Race"
        Constants.PARKOUR_TAG -> "Parkour Tag"
        Constants.GRID_RUNNERS -> "Grid Runners"
        Constants.BINGO_BUT_FAST -> "Bingo But Fast"
        Constants.SURVIVAL_GAMES -> "Survival Games"
        Constants.RAILROAD_RUSH -> "Railroad Rush"
        Constants.BUILD_MART -> "Build Mart"
        // Empty because map is already set to the name of the game
        Constants.SANDS_OF_TIME, Constants.MELTDOWN, Constants.DODGEBOLT, Constants.LIMBO, Constants.UNKNOWN -> ""

        "main_lobby" -> "Main Lobby"
        "game_lobbies" -> "Game Lobby"
        "fishing" -> "Fishing"

        else -> "$type $subType"
    }

    /**
     * Creates a copy of this [ServerState] with the [mapName] set to [name].
     */
    fun withMapName(name: String) = copy(mapName = name)

    fun miniMessageString(): String {
        return "type: <green>$serverType</green>, subType: <green>$subType</green>, map: <green>$mapName</green>"
    }
}
