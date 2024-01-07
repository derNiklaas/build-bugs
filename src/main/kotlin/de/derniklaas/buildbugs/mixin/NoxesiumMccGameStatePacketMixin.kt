package de.derniklaas.buildbugs.mixin

import com.noxcrew.noxesium.network.clientbound.ClientboundMccGameStatePacket
import de.derniklaas.buildbugs.BugCreator
import de.derniklaas.buildbugs.BuildBugsClientEntrypoint
import de.derniklaas.buildbugs.utils.Utils
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.util.Formatting
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(ClientboundMccGameStatePacket::class)
abstract class NoxesiumMccGameStatePacketMixin {

    @Inject(at = [At("HEAD")], method = ["receive"])
    fun receive(player: ClientPlayerEntity, responseSender: PacketSender, info: CallbackInfo) {
        val packet = (this as Object) as ClientboundMccGameStatePacket

        // ignore parkour warrior updates, as they only contain "Parkour Warrior Survivor" or nothing
        if (BugCreator.gameState.type == "parkour-warrior") {
            // Provide debug info
            if (BuildBugsClientEntrypoint.config.debugMode) {
                Utils.sendChatMessage(
                    "Blocked GameStatePacket: (mapName=${packet.mapName}, mapId=${packet.mapId}, phaseType=${packet.phaseType}, stage=${packet.stage})",
                    Formatting.GRAY
                )
            }

            return
        }

        BugCreator.handleGameStatePacket(packet)
        if (BuildBugsClientEntrypoint.config.debugMode) {
            Utils.sendChatMessage(
                "Received GameStatePacket: (mapName=${packet.mapName}, mapId=${packet.mapId}, phaseType=${packet.phaseType}, stage=${packet.stage})",
                Formatting.GRAY
            )
            BugCreator.printCurrentGameState()
        }
    }
}
