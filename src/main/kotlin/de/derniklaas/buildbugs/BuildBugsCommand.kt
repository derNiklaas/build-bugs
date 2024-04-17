package de.derniklaas.buildbugs

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import de.derniklaas.buildbugs.utils.Utils
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

object BuildBugsCommand {
    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
        dispatcher.register(literal<FabricClientCommandSource>("buildbug").then(
            literal<FabricClientCommandSource>("config").then(
                literal<FabricClientCommandSource>("clipboard").then(argument(
                    "value", BoolArgumentType.bool()
                ).executes {
                    val value = BoolArgumentType.getBool(it, "value")
                    BuildBugsClientEntrypoint.config.setCopy(value)
                    if (BuildBugsClientEntrypoint.config.copyToClipboard) {
                        Utils.sendMiniMessage("<green>Enabled</green> automatic copying to clipboard.")
                    } else {
                        Utils.sendMiniMessage("<red>Disabled</red> automatic copying to clipboard.")
                    }
                    return@executes Command.SINGLE_SUCCESS
                })
            ).then(
                literal<FabricClientCommandSource>("debug").then(argument(
                    "value", BoolArgumentType.bool()
                ).executes {
                    val value = BoolArgumentType.getBool(it, "value")
                    BuildBugsClientEntrypoint.config.setDebug(value)

                    if (BuildBugsClientEntrypoint.config.debugMode) {
                        Utils.sendMiniMessage("<green>Enabled</green> Debug mode.")
                        BugCreator.printCurrentGameState()
                    } else {
                        Utils.sendMiniMessage("<red>Disabled</red> Debug mode.")
                    }

                    return@executes Command.SINGLE_SUCCESS
                })
            ).then(
                literal<FabricClientCommandSource?>("eventip").then(argument(
                    "value", StringArgumentType.string()
                ).executes {
                    val value = StringArgumentType.getString(it, "value")
                    BuildBugsClientEntrypoint.config.setEventAddress(value)

                    Utils.sendSuccessMessage("Set event IP to <gold>$value</gold>.")

                    return@executes Command.SINGLE_SUCCESS
                })
            )
        ).then(literal<FabricClientCommandSource>("version").executes {
            Utils.sendSuccessMessage("Running BuildBugs ${BuildBugsClientEntrypoint.version}!")
            return@executes Command.SINGLE_SUCCESS
        }).then(literal<FabricClientCommandSource>("resetstate").executes {
            if (BuildBugsClientEntrypoint.config.debugMode) {
                BugCreator.resetGameState()
            } else {
                Utils.sendErrorMessage("You can only reset the game state in debug mode.")
            }
            return@executes Command.SINGLE_SUCCESS
        }).executes {
            BugCreator.report()
            return@executes Command.SINGLE_SUCCESS
        })
    }
}
