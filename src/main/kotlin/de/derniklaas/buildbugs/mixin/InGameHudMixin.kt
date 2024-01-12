package de.derniklaas.buildbugs.mixin

import de.derniklaas.buildbugs.BugCreator
import de.derniklaas.buildbugs.Constants
import de.derniklaas.buildbugs.utils.Utils
import java.util.Optional
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.text.Text
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(InGameHud::class)
abstract class InGameHudMixin {

    /*
     * Possible Subtitles:
     * "[M1-1] [name]" - Main Path, except ending
     * "[B1-1] [name]" - Bonus Path, incl. ending
     * "[S1-1] [name]" - Stage in PKW Survivor
     * "[F-1] [name]" - Finale in PKW Survivor
     */

    private val leapRegex = """\[[M|B|S|F]\d?-\d]""".toRegex()

    @Inject(at = [At("TAIL")], method = ["Lnet/minecraft/class_329;method_34002(Lnet/minecraft/class_2561;)V"])
    fun setSubTitle(title: Text, info: CallbackInfo) {
        // Ignore non MCC Servers
        if (!Utils.isOnMCCServer()) return
        // Only check titles in Parkour Warrior
        if (BugCreator.gameState.type != Constants.PARKOUR_WARRIOR) return

        // Check if the first element matches the regex defined above
        val start = title.content.toString()
        if (!leapRegex.containsMatchIn(start)) return

        val name = title.siblings.first().content.visit { Optional.of(it) }.get()

        Utils.sendDebugMessage("Found course segment '$name'.")
        BugCreator.updateParkourWarriorCourse(name)
    }
}
