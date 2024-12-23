package de.derniklaas.buildbugs

import de.derniklaas.buildbugs.utils.Utils
import io.leangen.geantyref.TypeToken
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.Version
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
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
        NoxesiumPacketHandler()
        val manager = FabricClientCommandManager.createNative(ExecutionCoordinator.asyncCoordinator())
        val annotationParser = AnnotationParser(manager, TypeToken.get(FabricClientCommandSource::class.java))
        annotationParser.parse(BuildBugsCommand())
        BuildBugsConfig.createDefaultConfig()
        val reportKeybinding = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.buildbugs.report", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_U, "category.buildbugs"
            )
        )
        val bugreportKeybinding = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.buildbugs.bugreport", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_I, "category.buildbugs"
            )
        )

        ClientTickEvents.END_CLIENT_TICK.register {
            if (reportKeybinding.wasPressed()) {
                BugCreator.report()
            }
            if (bugreportKeybinding.wasPressed()) {
                if (!Utils.isOnMCCServer()) {
                    Utils.sendErrorMessage("You are not connected to a MCC related server.")
                    return@register
                }
                val player = it.player ?: return@register
                player.networkHandler.sendCommand("bugreport Generated using BuildBugs Mod - contact on discord: derniklaas")
            }
        }
    }
}
