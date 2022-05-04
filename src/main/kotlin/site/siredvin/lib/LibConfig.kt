package site.siredvin.lib

import net.minecraftforge.common.ForgeConfigSpec

/**
 * Configuration class for tweaks of library
 */
object LibConfig {
    val isInitialCooldownEnabled: Boolean
        get() = IS_INITIAL_COOLDOWN_ENABLED?.get() ?: true
    val initialCooldownSensetiveLevel: Int
        get() = INITIAL_COOLDOWN_SENSENTIVE_LEVEL?.get() ?: 6000
    val cooldownTrasholdLevel: Int
        get() = INITIAL_COOLDOWN_SENSENTIVE_LEVEL?.get() ?: 100
    val xpToFuelRate: Int
        get() = XP_TO_FUEL_RATE?.get() ?: 10

    private var IS_INITIAL_COOLDOWN_ENABLED: ForgeConfigSpec.BooleanValue? = null
    private var INITIAL_COOLDOWN_SENSENTIVE_LEVEL: ForgeConfigSpec.IntValue? = null
    private var COOLDOWN_TRASHOLD_LEVEL: ForgeConfigSpec.IntValue? = null
    private var XP_TO_FUEL_RATE: ForgeConfigSpec.IntValue? = null

    fun build(builder: ForgeConfigSpec.Builder) {
        builder.push("libconfig")
        IS_INITIAL_COOLDOWN_ENABLED = builder.comment("Enables initial cooldown on peripheral initialization")
            .define("isInitialCooldownEnabled", true)
        INITIAL_COOLDOWN_SENSENTIVE_LEVEL = builder.comment("Determinates initial cooldown sensentive level, values lower then this value will not trigger initial cooldown")
            .defineInRange("initialCooldownSensetiveLevel", 6000, 0, Int.MAX_VALUE)
        COOLDOWN_TRASHOLD_LEVEL = builder.comment("Determinates trashold for cooldown to be stored")
            .defineInRange("cooldownTrashholdLevel", 100, 0, Int.MAX_VALUE)
        XP_TO_FUEL_RATE = builder.comment("Determinates amount xp to correspond one fuel point").defineInRange("xpToFuelRate", 10, 1, Int.MAX_VALUE)
        builder.pop()
    }
}