package de.derniklaas.buildbugs

import com.noxcrew.noxesium.network.NoxesiumPackets
import com.noxcrew.noxesium.network.clientbound.ClientboundMccGameStatePacket
import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket
import de.derniklaas.buildbugs.utils.Utils

class NoxesiumPacketHandler {

    init {
        NoxesiumPackets.MCC_SERVER.addListener(this) { _, packet, _ ->
            onServerPacket(packet)
        }
        NoxesiumPackets.MCC_GAME_STATE.addListener(this) { _, packet, _ ->
            onStatePacket(packet)
        }
    }

    private fun onServerPacket(packet: ClientboundMccServerPacket) {
        BugCreator.handleServerStatePacket(packet, packet.serverType != Constants.PARKOUR_WARRIOR)

        // Set default map names in PKW
        if (packet.serverType == Constants.PARKOUR_WARRIOR) {
            if (Utils.isOnEventServer()) {
                BugCreator.updateMap("Start Area")
            } else {
                when (packet.subType) {
                    Constants.PARKOUR_WARRIOR_DAILY, Constants.PARKOUR_WARRIOR_DOJO -> {
                        BugCreator.updateMap("Dojo Entrance")
                    }

                    Constants.PARKOUR_WARRIOR_SURVIVOR -> {
                        BugCreator.updateMap("Survivor Start")
                    }
                }
            }
        }
    }

    private fun onStatePacket(packet: ClientboundMccGameStatePacket) {
        // Set the map to Post Game during the Podium Phase
        if (packet.stage == Constants.PODIUM_PHASE) {
            BugCreator.updateMap("Post Game")
            return
        }

        // ignore parkour warrior updates, as they only contain "Parkour Warrior Survivor" or nothing
        // also ignore the podium phase as it overwrites the map part
        if (BugCreator.gameState.serverType == Constants.PARKOUR_WARRIOR && Utils.isOnIsland()) {
            // Provide debug info
            Utils.sendDebugMessage("Blocked GameStatePacket: (name: <green>${packet.mapName}</green>, id: ${packet.mapId}, phase: ${packet.phaseType}, stage: ${packet.stage})")

            return
        }

        Utils.sendDebugMessage("Received GameStatePacket: (name: <green>${packet.mapName}</green>, id: ${packet.mapId}, phase: ${packet.phaseType}, stage: ${packet.stage})")
        BugCreator.updateMap(packet.mapName)
    }

}