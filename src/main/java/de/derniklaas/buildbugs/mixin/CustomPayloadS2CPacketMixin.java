package de.derniklaas.buildbugs.mixin;

import de.derniklaas.buildbugs.BuildBugsClientEntrypoint;
import de.derniklaas.buildbugs.utils.Utils;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Temporarily disable logging of packets as networking has changed

//@Mixin(CustomPayloadS2CPacket.class)
public class CustomPayloadS2CPacketMixin {
    /*
    @Inject(at = @At("HEAD"), method = "readPayload")
    private static void onReceive(Identifier id, PacketByteBuf buffer, CallbackInfoReturnable<CustomPayload> cir) {
        if (!BuildBugsClientEntrypoint.Companion.getConfig().getLogIncomingPackets()) return;

        Utils.INSTANCE.sendDebugMessage("INBOUND: " + id + " -> " + buffer.readableBytes() + " bytes", true);
    }
    */
}
