package de.derniklaas.buildbugs.mixin

import com.noxcrew.noxesium.network.clientbound.ClientboundMccGameStatePacket
import de.derniklaas.buildbugs.BugCreator
import de.derniklaas.buildbugs.Constants
import de.derniklaas.buildbugs.utils.Utils
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.network.ClientPlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(ClientboundMccGameStatePacket::class)
abstract class NoxesiumMccGameStatePacketMixin {

    @Inject(at = [At("HEAD")], method = ["receive"])
    fun receive(player: ClientPlayerEntity, responseSender: PacketSender, info: CallbackInfo) {
        val packet = (this as Object) as ClientboundMccGameStatePacket

        // Set the map to Post Game during the Podium Phase
        if (packet.stage == Constants.PODIUM_PHASE) {
            BugCreator.updateMap("Post Game")
            return
        }

        // ignore parkour warrior updates, as they only contain "Parkour Warrior Survivor" or nothing
        // also ignore the podium phase as it overwrites the map part
        if (BugCreator.gameState.type == Constants.PARKOUR_WARRIOR && Utils.isOnIsland()) {
            // Provide debug info
            Utils.sendDebugMessage("Blocked GameStatePacket: (name: <green>${packet.mapName}</green>, id: ${packet.mapId}, phase: ${packet.phaseType}, stage: ${packet.stage})")

            return
        }

        Utils.sendDebugMessage("Received GameStatePacket: (name: <green>${packet.mapName}</green>, id: ${packet.mapId}, phase: ${packet.phaseType}, stage: ${packet.stage})")
        BugCreator.updateMap(packet.mapName)
    }
}
