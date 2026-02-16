package de.derniklaas.buildbugs.mixin

import de.derniklaas.buildbugs.BugCreator
import de.derniklaas.buildbugs.utils.Utils
import net.minecraft.client.gui.components.ChatComponent
import net.minecraft.network.chat.Component
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(ChatComponent::class)
abstract class ChatMixin {

    @Inject(at = [At("TAIL")], method = ["Lnet/minecraft/client/gui/components/ChatComponent;addMessage(Lnet/minecraft/network/chat/Component;)V"])
    fun onChatMessage(message: Component, info: CallbackInfo) {
        // ignore non MCC servers
        if (!Utils.isOnMCCServer()) return

        BugCreator.handleChatMessage(message)
    }
}
