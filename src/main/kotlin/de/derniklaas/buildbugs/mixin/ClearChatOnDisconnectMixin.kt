package de.derniklaas.buildbugs.mixin

import de.derniklaas.buildbugs.BuildBugsClientEntrypoint
import net.minecraft.client.MinecraftClient
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(MinecraftClient::class)
abstract class ClearChatOnDisconnectMixin {

    // Clear the chat when disconnecting from a server
    // This is done to prevent the chat from being filled with messages from the previous server
    @Inject(at = [At("HEAD")], method = ["Lnet/minecraft/class_310;method_18096(Lnet/minecraft/class_437;)V"])
    fun onDisconnect(info: CallbackInfo) {
        // ignore if IslandUtils is present as it will also clear the chat
        if(BuildBugsClientEntrypoint.hasIslandUtils) return
        // Clear the chat when disconnecting from a server
        MinecraftClient.getInstance().inGameHud.clear()
    }

}