package de.derniklaas.buildbugs

import com.mojang.blaze3d.platform.InputConstants
import com.noxcrew.noxesium.NoxesiumFabricMod
import de.derniklaas.buildbugs.utils.Utils
import io.leangen.geantyref.TypeToken
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.Version
import net.minecraft.client.KeyMapping
import net.minecraft.resources.ResourceLocation
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.fabric.FabricClientCommandManager
import org.lwjgl.glfw.GLFW

class BuildBugsClientEntrypoint : ClientModInitializer {

    companion object {
        private const val MOD_ID = "build-bugs"
        val config = BuildBugsConfig.fromFile()
        val version: Version = FabricLoader.getInstance().getModContainer(MOD_ID).get().metadata.version
    }

    override fun onInitializeClient() {
        NoxesiumFabricMod.initialize()
        NoxesiumPacketHandler()
        val manager = FabricClientCommandManager.createNative(ExecutionCoordinator.asyncCoordinator())
        val annotationParser = AnnotationParser(manager, TypeToken.get(FabricClientCommandSource::class.java))
        annotationParser.parse(BuildBugsCommand())
        BuildBugsConfig.createDefaultConfig()

        val resourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, "keybinds")
        val keybindCategory = KeyMapping.Category.register(resourceLocation)

        val reportKeybinding = KeyBindingHelper.registerKeyBinding(
            KeyMapping(
                "key.buildbugs.report", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_U, keybindCategory
            )
        )
        val bugreportKeybinding = KeyBindingHelper.registerKeyBinding(
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
