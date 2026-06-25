package de.derniklaas.buildbugs

import com.mojang.blaze3d.platform.InputConstants
import com.noxcrew.noxesium.core.fabric.mcc.MccNoxesiumEntrypoint
import de.derniklaas.buildbugs.utils.Utils
import io.leangen.geantyref.TypeToken
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.Version
import net.minecraft.client.KeyMapping
import net.minecraft.resources.Identifier
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.fabric.FabricClientCommandManager
import org.lwjgl.glfw.GLFW

class BuildBugsClientEntrypoint : MccNoxesiumEntrypoint() {

    override fun getVersion(): String = "1"

    companion object {
        private const val MOD_ID = "build-bugs"
        val config = BuildBugsConfig.fromFile()
        val version: Version = FabricLoader.getInstance().getModContainer(MOD_ID).get().metadata.version

        /**
         * Strong reference to the packet handler so it is never garbage collected.
         *
         * Noxesium registers packet listeners against a [java.lang.ref.WeakReference] to the
         * handler instance and silently drops them once that reference is collected. Without
         * keeping it alive here, the MCC server/game-state packets stop being delivered, leaving
         * [BugCreator.gameState] stuck on [de.derniklaas.buildbugs.utils.ServerState.UNKNOWN].
         */
        private var packetHandler: NoxesiumPacketHandler? = null
    }

    override fun initialize() {
        packetHandler = NoxesiumPacketHandler()
        val manager = FabricClientCommandManager.createNative(ExecutionCoordinator.asyncCoordinator())
        val annotationParser = AnnotationParser(manager, TypeToken.get(FabricClientCommandSource::class.java))
        annotationParser.parse(BuildBugsCommand())
        BuildBugsConfig.createDefaultConfig()

        val resourceLocation = Identifier.fromNamespaceAndPath(MOD_ID, "keybinds")
        val keybindCategory = KeyMapping.Category.register(resourceLocation)

        val reportKeybinding = KeyMappingHelper.registerKeyMapping(
            KeyMapping(
                "key.buildbugs.report", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_U, keybindCategory
            )
        )
        val bugreportKeybinding = KeyMappingHelper.registerKeyMapping(
            KeyMapping(
                "key.buildbugs.bugreport", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_I, keybindCategory
            )
        )

        // ensure only executed once
        var clickedReportKeybind = false
        var clickedBugreportKeybind = false

        ClientTickEvents.END_CLIENT_TICK.register {
            if (reportKeybinding.isDown) {
                if (clickedReportKeybind) return@register
                clickedReportKeybind = true
                BugCreator.report()
            } else if (clickedReportKeybind) {
                clickedReportKeybind = false
            }
            if (bugreportKeybinding.isDown) {
                if (clickedBugreportKeybind) return@register
                clickedBugreportKeybind = true
                if (!Utils.isOnMCCServer()) {
                    Utils.sendErrorMessage("You are not connected to a MCC related server.")
                    return@register
                }
                val player = it.player ?: return@register
                player.connection.sendCommand("bugreport Generated using BuildBugs Mod - contact on discord: derniklaas")
            } else if (clickedBugreportKeybind) {
                clickedBugreportKeybind = false
            }
        }
    }
}
