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
import net.minecraft.util.Formatting

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
                        it.source.sendFeedback(
                            Utils.getFormattedText(
                                "Copy to Clipboard enabled.", Formatting.GREEN
                            )
                        )
                    } else {
                        it.source.sendFeedback(
                            Utils.getFormattedText(
                                "Copy to Clipboard disabled.", Formatting.RED
                            )
                        )
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
                        it.source.sendFeedback(Utils.getFormattedText("Debug mode enabled.", Formatting.GREEN))
                        BugCreator.printCurrentGameState()
                    } else {
                        it.source.sendFeedback(Utils.getFormattedText("Debug mode disabled.", Formatting.RED))
                    }

                    return@executes Command.SINGLE_SUCCESS
                })
            ).then(
                literal<FabricClientCommandSource?>("eventip").then(
                    argument(
                        "value", StringArgumentType.string()
                    ).executes {
                        val value = StringArgumentType.getString(it, "value")
                        BuildBugsClientEntrypoint.config.setEventAddress(value)

                        it.source.sendFeedback(Utils.getFormattedText("Set event IP to '$value'.", Formatting.GREEN))

                        return@executes Command.SINGLE_SUCCESS
                    }
                )
            )
        ).then(literal<FabricClientCommandSource>("version").executes {
            val feedback = Utils.getFormattedText(
                "Running BuildBugs v${BuildBugsClientEntrypoint.version}!", Formatting.GREEN
            )
            it.source.sendFeedback(feedback)
            return@executes Command.SINGLE_SUCCESS
        }).executes {
            BugCreator.report()
            return@executes Command.SINGLE_SUCCESS
        })
    }
}
