package de.derniklaas.buildbugs.mixin

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket
import de.derniklaas.buildbugs.BugCreator
import de.derniklaas.buildbugs.Constants
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.network.ClientPlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(ClientboundMccServerPacket::class)
abstract class NoxesiumMccServerPacketMixin {

    @Inject(at = [At("HEAD")], method = ["receive"])
    fun receive(player: ClientPlayerEntity, responseSender: PacketSender, info: CallbackInfo) {
        val packet = (this as Object) as ClientboundMccServerPacket
        BugCreator.handleServerStatePacket(packet, packet.type != Constants.PARKOUR_WARRIOR)

        // Set default map names in PKW
        if(packet.type == Constants.PARKOUR_WARRIOR) {
            when(packet.subType) {
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
