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

    @Inject(at = [At("TAIL")], method = ["Lnet/minecraft/class_338;method_1812(Lnet/minecraft/class_2561;)V"])
    fun onChatMessage(message: Component, info: CallbackInfo) {
        // ignore non MCC servers
        if (!Utils.isOnMCCServer()) return

        message.style.clickEvent?.let {
            if (it.action == ClickEvent.Action.COPY_TO_CLIPBOARD && it.value.startsWith(Constants.BUG_REPORT_URL)) {
                BugCreator.setClipboard(it.value)
            }
        }
        message.siblings.forEach {
            it.style.clickEvent?.let { event ->
                if (event.action == ClickEvent.Action.COPY_TO_CLIPBOARD && event.value.startsWith(Constants.BUG_REPORT_URL)) {
                    BugCreator.setClipboard(event.value)
                }
            }
        }
    }
}
