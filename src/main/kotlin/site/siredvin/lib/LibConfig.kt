package site.siredvin.lib

import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.lib.LibConfig

/**
 * Configuration class for tweaks of library
 */
object LibConfig {
    private var testMode = false
    var isInitialCooldownEnabled = true
    var initialCooldownSensetiveLevel = 6000
    private var IS_INITIAL_COOLDOWN_ENABLED: ForgeConfigSpec.BooleanValue? = null
    private var INITIAL_COOLDOWN_SENSENTIVE_LEVEL: ForgeConfigSpec.IntValue? = null
    fun setTestMode(mode: Boolean) {
        testMode = mode
        if (mode) {
            isInitialCooldownEnabled = false
        } else {
            if (IS_INITIAL_COOLDOWN_ENABLED != null) {
                reloadConfig()
            } else {
                isInitialCooldownEnabled = true
            }
        }
    }

    fun build(builder: ForgeConfigSpec.Builder) {
        IS_INITIAL_COOLDOWN_ENABLED = builder.comment("Enables initial cooldown on peripheral initialization")
            .define("isInitialCooldownEnabled", true)
        INITIAL_COOLDOWN_SENSENTIVE_LEVEL =
            builder.comment("Determinates initial cooldown sensentive level, values lower then this value will not trigger initial cooldown")
                .defineInRange("initialCooldownSensetiveLevel", 6000, 0, Int.MAX_VALUE)
    }

    fun reloadConfig() {
        if (!testMode) {
            isInitialCooldownEnabled = IS_INITIAL_COOLDOWN_ENABLED!!.get()
            initialCooldownSensetiveLevel = INITIAL_COOLDOWN_SENSENTIVE_LEVEL!!.get()
        }
    }
}