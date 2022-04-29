package site.siredvin.turtlematic.api

import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.turtlematic.api.IAutomataCoreTier

enum class AutomataCoreTier(private val defaultInteractionRadius: Int, private val defaultMaxFuelConsumptionRate: Int) :
    IAutomataCoreTier {
    TIER1(2, 2),
    TIER2(4, 3),
    TIER3(8, 4),
    ENORMOUS_TIER(Integer.MAX_VALUE, Integer.MAX_VALUE);

    private var _interactionRadius: ForgeConfigSpec.IntValue? = null
    private var _maxFuelConsumptionRate: ForgeConfigSpec.IntValue? = null

    override val interactionRadius: Int
        get() = if (_interactionRadius == null) 0 else _interactionRadius!!.get()

    override val maxFuelConsumptionRate: Int
        get() = if (_maxFuelConsumptionRate == null) 0 else _maxFuelConsumptionRate!!.get()

    override val settingsPostfix: String
        get() = "AutomataCore"

    override fun addToConfig(builder: ForgeConfigSpec.Builder) {
        _interactionRadius = builder.defineInRange(
            settingsName() + "InteractionRadius", defaultInteractionRadius, 1, 64
        )
        _maxFuelConsumptionRate = builder.defineInRange(
            settingsName() + "MaxFuelConsumptionRate", defaultMaxFuelConsumptionRate, 1, 32
        )
    }
}