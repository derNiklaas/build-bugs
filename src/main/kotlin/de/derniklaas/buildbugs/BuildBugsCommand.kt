package de.derniklaas.buildbugs

import de.derniklaas.buildbugs.utils.Utils
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.suggestion.Suggestions
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.suggestion.Suggestion

class BuildBugsCommand {

    @Command("buildbug config clipboard <value>")
    fun config_clipboard(context: CommandContext<*>, @Argument("value") value: Boolean) {
        BuildBugsClientEntrypoint.config.setCopy(value)
        if (BuildBugsClientEntrypoint.config.copyToClipboard) {
            Utils.sendMiniMessage("<green>Enabled</green> automatic copying to clipboard.")
        } else {
            Utils.sendMiniMessage("<red>Disabled</red> automatic copying to clipboard.")
        }
    }

    @Command("buildbug config debug <value>")
    fun config_debug(context: CommandContext<*>, @Argument("value") value: Boolean) {
        BuildBugsClientEntrypoint.config.setDebug(value)
        if (BuildBugsClientEntrypoint.config.debugMode) {
            Utils.sendMiniMessage("<green>Enabled</green> Debug mode.")
            BugCreator.printCurrentGameState()
        } else {
            Utils.sendMiniMessage("<red>Disabled</red> Debug mode.")
        }
    }

    @Command("buildbug config eventip <address>")
    fun config_ip(context: CommandContext<*>, @Argument("address") address: String) {
        BuildBugsClientEntrypoint.config.setEventAddress(address)
        Utils.sendSuccessMessage("Set event IP to <gold>$address</gold>.")

    }

    @Command("buildbug version")
    fun version(context: CommandContext<*>) {
        Utils.sendSuccessMessage("Running BuildBugs ${BuildBugsClientEntrypoint.version}!")
    }

    @Command("buildbug reset_state")
    fun reset_state(context: CommandContext<*>) {
        if (BuildBugsClientEntrypoint.config.debugMode) {
            BugCreator.resetGameState()
        } else {
            Utils.sendErrorMessage("You can only reset the game state in debug mode.")
        }
    }

    @Command("buildbug force_state <game> <type> <map>")
    fun force_state_full(
        context: CommandContext<*>,
        @Argument("game", suggestions = "gameSuggestions") game: String,
        @Argument("type") type: String,
        @Argument("map") map: String
    ) {
        if (!BuildBugsClientEntrypoint.config.debugMode) {
            Utils.sendErrorMessage("You can only force the game state in debug mode.")
            return
        }
        BugCreator.forceGameState(game, type, map)
    }

    @Command("buildbug force_state <game> <type>")
    fun force_state_full(
        context: CommandContext<*>,
        @Argument("game", suggestions = "gameSuggestions") game: String,
        @Argument("type") type: String
    ) {
        if (!BuildBugsClientEntrypoint.config.debugMode) {
            Utils.sendErrorMessage("You can only force the game state in debug mode.")
            return
        }
        BugCreator.forceGameState(game, type, Constants.UNKNOWN)
    }

    fun report(context: CommandContext<*>) {
        BugCreator.report()
    }


    @Suggestions("gameSuggestions")
    fun gameSuggestions(commandContext: CommandContext<*>, input: String): List<Suggestion> {
        return Constants.ALL_TYPES.map { Suggestion.suggestion(it) }
    }
}
