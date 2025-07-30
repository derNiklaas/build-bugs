package de.derniklaas.buildbugs.mixin

import de.derniklaas.buildbugs.BugCreator
import net.minecraft.client.Minecraft
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Minecraft::class)
abstract class ClearChatOnDisconnectMixin {

    @Inject(at = [At("HEAD")], method = ["Lnet/minecraft/class_310;method_55505()V"])
    fun onDisconnect(info: CallbackInfo) {
        BugCreator.resetGameState()
    }
}
