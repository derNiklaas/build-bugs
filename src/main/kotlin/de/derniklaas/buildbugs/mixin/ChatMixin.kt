package de.derniklaas.buildbugs.mixin

import de.derniklaas.buildbugs.BugCreator
import de.derniklaas.buildbugs.utils.Utils
import net.minecraft.client.gui.components.ChatComponent
import net.minecraft.client.multiplayer.chat.GuiMessageSource
import net.minecraft.client.multiplayer.chat.GuiMessageTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MessageSignature
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(ChatComponent::class)
abstract class ChatMixin {

    // Since 26.1 the single-argument addMessage overload was removed; all chat lines now funnel
    // through the private addMessage(Component, MessageSignature, GuiMessageSource, GuiMessageTag).
    @Inject(
        at = [At("TAIL")],
        method = ["addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/multiplayer/chat/GuiMessageSource;Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;)V"]
    )
    fun onChatMessage(
        message: Component,
        signature: MessageSignature?,
        source: GuiMessageSource?,
        tag: GuiMessageTag?,
        info: CallbackInfo
    ) {
        // ignore non MCC servers
        if (!Utils.isOnMCCServer()) return

        BugCreator.handleChatMessage(message)
    }
}
