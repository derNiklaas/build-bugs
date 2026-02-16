package de.derniklaas.buildbugs.mixin

import de.derniklaas.buildbugs.BugCreator
import de.derniklaas.buildbugs.Constants
import de.derniklaas.buildbugs.utils.Utils
import net.minecraft.client.gui.components.ChatComponent
import net.minecraft.network.chat.ClickEvent
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

        message.style.clickEvent?.let {
            if (it is ClickEvent.CopyToClipboard && (it.value.startsWith(Constants.BUG_REPORT_URL) || it.value.startsWith(Constants.BUG_REPORT_URL_NEW))) {
                BugCreator.setClipboard(it.value)
            }
        }
        message.siblings.forEach {
            it.style.clickEvent?.let { event ->
                if (event is ClickEvent.CopyToClipboard && (event.value.startsWith(Constants.BUG_REPORT_URL) || event.value.startsWith(Constants.BUG_REPORT_URL_NEW))) {
                    BugCreator.setClipboard(event.value)
                }
            }
        }
    }
}
