package de.derniklaas.buildbugs.mixin

import de.derniklaas.buildbugs.BugCreator
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Minecraft::class)
abstract class MinecraftMixin {

    @Inject(at = [At("HEAD")], method = ["disconnect(Lnet/minecraft/client/gui/screens/Screen;ZZ)V"])
    fun onDisconnect(screen: Screen?, b1: Boolean, b2: Boolean, info: CallbackInfo) {
        BugCreator.resetGameState()
    }
}
