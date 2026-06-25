package de.derniklaas.buildbugs.mixin

import com.noxcrew.noxesium.api.network.payload.NoxesiumPayloadGroup
import com.noxcrew.noxesium.api.util.TriConsumer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect
import java.util.UUID

/**
 * Isolates exceptions thrown by individual Noxesium packet listeners.
 *
 * By redirecting the per-listener dispatch call, we give each listener its own try/catch, so a
 * single misbehaving listener can no longer block the others.
 */
@Mixin(value = [NoxesiumPayloadGroup::class], remap = false)
abstract class NoxesiumPayloadGroupMixin {

    @Redirect(
        method = ["handle"],
        at = At(
            value = "INVOKE",
            target = "Lcom/noxcrew/noxesium/api/network/payload/NoxesiumPayloadGroup;" +
                "acceptAny(Lcom/noxcrew/noxesium/api/util/TriConsumer;Ljava/lang/Object;Ljava/util/UUID;Ljava/lang/Object;)V"
        )
    )
    fun buildbugsIsolateListenerExceptions(
        instance: NoxesiumPayloadGroup,
        consumer: TriConsumer<Any?, Any?, UUID>,
        reference: Any?,
        context: UUID,
        payload: Any?
    ) {
        // Mirrors NoxesiumPayloadGroup#acceptAny, but contains the failure to this one listener.
        try {
            consumer.accept(reference, payload, context)
        } catch (throwable: Throwable) {
           throwable.printStackTrace()
        }
    }
}
