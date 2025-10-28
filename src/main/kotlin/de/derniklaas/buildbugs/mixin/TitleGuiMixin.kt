package de.derniklaas.buildbugs.mixin

import de.derniklaas.buildbugs.BugCreator
import de.derniklaas.buildbugs.Constants
import de.derniklaas.buildbugs.utils.Utils
import net.minecraft.client.gui.Gui
import net.minecraft.network.chat.Component
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Gui::class)
abstract class TitleGuiMixin {

    /*
     * Possible Subtitles:
     * "[M1-1] [name]" - Main Path, except ending
     * "[B1-1] [name]" - Bonus Path, incl. ending
     * "[S1-1] [name]" - Stage in PKW Survivor
     * "[F-1] [name]" - Finale in PKW Survivor
     */

    private val leapRegex = """\[[MBSF]\d?-\d]""".toRegex()

    @Inject(at = [At("TAIL")], method = ["Lnet/minecraft/class_329;method_34002(Lnet/minecraft/class_2561;)V"])
    fun setSubTitle(title: Component, info: CallbackInfo) {
        // Ignore non MCC Servers
        if (!Utils.isOnMCCServer()) return
        // Only check titles in Parkour Warrior
        if (BugCreator.gameState.server != Constants.PARKOUR_WARRIOR) return

        val courseTitle = title.getString(Int.MAX_VALUE)
        if (!leapRegex.containsMatchIn(courseTitle)) return

        // Extract the name from the title
        val name = courseTitle.split("] ")[1]

        Utils.sendDebugMessage("Found course segment <green>$name</green>.")
        BugCreator.updateMap(name)
    }
}
