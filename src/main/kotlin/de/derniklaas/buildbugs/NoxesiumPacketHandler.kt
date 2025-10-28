package de.derniklaas.buildbugs

import com.noxcrew.noxesium.core.mcc.ClientboundMccGameStatePacket
import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import com.noxcrew.noxesium.core.mcc.MccPackets
import de.derniklaas.buildbugs.utils.Utils

class NoxesiumPacketHandler {

    init {
        MccPackets.CLIENTBOUND_MCC_SERVER.addListener<Any, ClientboundMccServerPacket>(
            this,
            ClientboundMccServerPacket::class.java
        ) { _, packet, _ ->
            onServerPacket(packet)
        }
        MccPackets.CLIENTBOUND_MCC_GAME_STATE.addListener<Any, ClientboundMccGameStatePacket>(
            this,
            ClientboundMccGameStatePacket::class.java
        ) { _, packet, _ ->
            onStatePacket(packet)
        }
    }

    private fun onServerPacket(packet: ClientboundMccServerPacket) {
        BugCreator.handleServerStatePacket(packet, packet.server != Constants.PARKOUR_WARRIOR)

        // Set default map names in PKW
        if (Constants.PARKOUR_WARRIOR in packet.types) {
            if (Utils.isOnEventServer()) {
                BugCreator.updateMap("Start Area")
                return
            }

            if (Constants.PARKOUR_WARRIOR_DOJO in packet.types || Constants.PARKOUR_WARRIOR_DAILY in packet.types) {
                BugCreator.updateMap("Dojo Entrance")
                return
            } else if (Constants.PARKOUR_WARRIOR_SURVIVOR in packet.types) {
                BugCreator.updateMap("Survivor Start")
                return
            }
        }
    }

    private fun onStatePacket(packet: ClientboundMccGameStatePacket) {
        // Set the map to Post Game during the Podium Phase
        if (packet.stage.lowercase() == Constants.PODIUM_PHASE) {
            BugCreator.updateMap("Post Game")
            return
        }

        // ignore parkour warrior updates, as they only contain "Parkour Warrior Survivor" or nothing
        // also ignore the podium phase as it overwrites the map part
        if (Constants.PARKOUR_WARRIOR in BugCreator.gameState.types && Utils.isOnIsland()) {
            // Provide debug info
            Utils.sendDebugMessage("Blocked GameStatePacket: (name: <green>${packet.mapName}</green>, id: ${packet.mapId}, phase: ${packet.phaseType}, stage: ${packet.stage})")

            return
        }

        Utils.sendDebugMessage("Received GameStatePacket: (name: <green>${packet.mapName}</green>, id: ${packet.mapId}, phase: ${packet.phaseType}, stage: ${packet.stage})")
        BugCreator.updateMap(packet.mapName)
    }
}
