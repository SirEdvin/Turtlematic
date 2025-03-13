package site.siredvin.turtlematic.computercraft.operations

import com.google.common.math.IntMath
import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.turtlematic.api.IForgeConfigHandler
import site.siredvin.tweakium.modules.operation.SphereOperationContext
import site.siredvin.tweakium.modules.peripheral.api.IPeripheralOperation

enum class SphereOperation(
    private val defaultCooldown: Int,
    private val defaultMaxFreeRadius: Int,
    private val defaultMaxCostRadius: Int,
    private val defaultExtraBlockCost: Double,
) : IPeripheralOperation<SphereOperationContext>,
    IForgeConfigHandler {
    SCAN_BLOCKS(2000, 8, 16, 0.17),
    SCAN_ENTITIES(2000, 8, 16, 0.17),
    SCAN_ITEMS(2000, 8, 16, 0.17),
    ;

    private var cooldown: ForgeConfigSpec.IntValue? = null
    private var maxFreeRadiusConfig: ForgeConfigSpec.IntValue? = null
    private var maxCostRadiusConfig: ForgeConfigSpec.IntValue? = null
    private var extraBlockCostConfig: ForgeConfigSpec.DoubleValue? = null
    override fun addToConfig(builder: ForgeConfigSpec.Builder) {
        cooldown = builder.defineInRange(
            settingsName() + "Cooldown",
            defaultCooldown,
            1000,
            Int.MAX_VALUE,
        )
        maxFreeRadiusConfig = builder.defineInRange(
            settingsName() + "MaxFreeRadius",
            defaultMaxFreeRadius,
            1,
            64,
        )
        maxCostRadiusConfig = builder.defineInRange(
            settingsName() + "MaxCostRadius",
            defaultMaxCostRadius,
            1,
            64,
        )
        extraBlockCostConfig = builder.defineInRange(
            settingsName() + "ExtraBlockCost",
            defaultExtraBlockCost,
            0.1,
            Double.MAX_VALUE,
        )
    }

    override fun getCooldown(context: SphereOperationContext): Int = cooldown!!.get()

    override fun getCost(context: SphereOperationContext): Int {
        if (context.radius <= maxFreeRadiusConfig!!.get()) return 0
        val freeBlockCount = IntMath.pow(2 * maxFreeRadiusConfig!!.get() + 1, 3)
        val allBlockCount = IntMath.pow(2 * context.radius + 1, 3)
        return Math.floor((allBlockCount - freeBlockCount) * extraBlockCostConfig!!.get()).toInt()
    }

    val maxFreeRadius: Int
        get() = maxFreeRadiusConfig!!.get()
    val maxCostRadius: Int
        get() = maxCostRadiusConfig!!.get()

    override fun computerDescription(): Map<String, Any> {
        val data: MutableMap<String, Any> = HashMap()
        data["name"] = settingsName()
        data["type"] = javaClass.name
        data["cooldown"] = cooldown!!.get()
        data["maxFreeRadius"] = maxFreeRadiusConfig!!.get()
        data["maxCostRadius"] = maxCostRadiusConfig!!.get()
        data["extraBlockCost"] = extraBlockCostConfig!!.get()
        return data
    }

    fun free(): SphereOperationContext = SphereOperationContext(maxFreeRadius)

    fun cost(): SphereOperationContext = SphereOperationContext(maxCostRadius)
}
