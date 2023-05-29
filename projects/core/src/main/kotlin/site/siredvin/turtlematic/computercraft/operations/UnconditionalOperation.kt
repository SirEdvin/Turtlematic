package site.siredvin.turtlematic.computercraft.operations

import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.peripheralium.api.peripheral.IPeripheralOperation

enum class UnconditionalOperation(
    private val defaultCooldown: Int,
    private val defaultCost: Int,
) : IPeripheralOperation<Any?> {
    XP_TRANSFER(1000, 1),
    ;

    private var cooldown: ForgeConfigSpec.IntValue? = null
    private var cost: ForgeConfigSpec.IntValue? = null

    override val initialCooldown: Int
        get() = cooldown!!.get()

    override fun getCooldown(context: Any?): Int {
        return cooldown!!.get()
    }

    override fun getCost(context: Any?): Int {
        return cost!!.get()
    }

    override fun computerDescription(): Map<String, Any> {
        val data: MutableMap<String, Any> = HashMap()
        data["name"] = settingsName()
        data["type"] = javaClass.name
        data["baseCooldown"] = cooldown!!.get()
        data["baseCost"] = cost!!.get()
        return data
    }

    override fun addToConfig(builder: ForgeConfigSpec.Builder) {
        cooldown = builder.defineInRange(
            settingsName() + "Cooldown",
            defaultCooldown,
            1000,
            Int.MAX_VALUE,
        )
        cost = builder.defineInRange(
            settingsName() + "Cost",
            defaultCost,
            0,
            Int.MAX_VALUE,
        )
    }
}
