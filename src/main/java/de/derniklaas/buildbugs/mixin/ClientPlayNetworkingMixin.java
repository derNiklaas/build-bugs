package de.derniklaas.buildbugs.mixin;

import de.derniklaas.buildbugs.BuildBugsClientEntrypoint;
import de.derniklaas.buildbugs.utils.Utils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Temporarily disable logging of packets as networking has changed

//@Mixin(ClientPlayNetworking.class)
public class ClientPlayNetworkingMixin {
    /*
    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/util/Identifier;Lnet/minecraft/network/PacketByteBuf;)V")
    private static void onReceive(Identifier channelName, PacketByteBuf buffer, CallbackInfo ci) {
        if (!BuildBugsClientEntrypoint.Companion.getConfig().getLogOutgoingPackets()) return;

        Utils.INSTANCE.sendDebugMessage("OUTBOUND: " + channelName + " -> " + buffer.readableBytes() + " bytes", true);
    }
    */
}
