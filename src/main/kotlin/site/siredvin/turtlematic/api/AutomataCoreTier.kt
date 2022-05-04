package site.siredvin.turtlematic.api

import net.minecraft.resources.ResourceLocation
import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.turtlematic.api.IAutomataCoreTier

enum class AutomataCoreTier(
    private val defaultInteractionRadius: Int, private val defaultMaxFuelConsumptionRate: Int,
    private val defaultCooldownReduceFactor: Double, private val _traits: Set<ResourceLocation> = emptySet()
) : IAutomataCoreTier {
    TIER1(2, 2, 1.0),
    TIER2(4, 3, 1.0),
    TIER3(8, 4, 0.8, setOf(AutomataCoreTraits.DURABILITY_REFUND_CHANCE)),
    TIER4(16, 6, 0.5, setOf(AutomataCoreTraits.DURABILITY_REFUND)),
    ENORMOUS_TIER(Integer.MAX_VALUE, Integer.MAX_VALUE, 0.0, setOf(AutomataCoreTraits.DURABILITY_REFUND));

    private var _interactionRadius: ForgeConfigSpec.IntValue? = null
    private var _maxFuelConsumptionRate: ForgeConfigSpec.IntValue? = null
    private var _cooldownReduceFactor: ForgeConfigSpec.DoubleValue? = null

    override val interactionRadius: Int
        get() = _interactionRadius?.get() ?: defaultInteractionRadius

    override val maxFuelConsumptionRate: Int
        get() = _maxFuelConsumptionRate?.get() ?: defaultMaxFuelConsumptionRate

    override val cooldownReduceFactor: Double
        get() = _cooldownReduceFactor?.get() ?: defaultCooldownReduceFactor

    override val traits: Set<ResourceLocation>
        get() = _traits

    override val settingsPostfix: String
        get() = "AutomataCore"

    override fun addToConfig(builder: ForgeConfigSpec.Builder) {
        _interactionRadius = builder.defineInRange(
            settingsName() + "InteractionRadius", defaultInteractionRadius, 1, 64
        )
        _maxFuelConsumptionRate = builder.defineInRange(
            settingsName() + "MaxFuelConsumptionRate", defaultMaxFuelConsumptionRate, 1, 32
        )
        _cooldownReduceFactor = builder.defineInRange(
            settingsName() + "CooldownReduceFactor", defaultCooldownReduceFactor, 0.0, 1.0
        )
    }
}