package de.derniklaas.buildbugs

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.Version
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

class BuildBugsClientEntrypoint : ClientModInitializer {

    companion object {
        private const val MOD_ID = "build-bugs"
        val config = BuildBugsConfig.fromFile()
        val version: Version = FabricLoader.getInstance().getModContainer(MOD_ID).get().metadata.version
    }

    override fun onInitializeClient() {
        BuildBugsConfig.createDefaultConfig()
        val reportKeybinding = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.buildbugs.report", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_U, "category.buildbugs"
            )
        )

        ClientCommandRegistrationCallback.EVENT.register(BuildBugsCommand::register)

        ClientTickEvents.END_CLIENT_TICK.register {
            if (reportKeybinding.wasPressed()) {
                BugCreator.report()
            }
        }
    }
}
