package site.siredvin.turtlematic.computercraft.operations

import net.neoforged.neoforge.common.ModConfigSpec
import site.siredvin.turtlematic.api.IForgeConfigHandler
import site.siredvin.tweakium.modules.peripheral.api.IPeripheralOperation

enum class UnconditionalOperation(
    private val defaultCooldown: Int,
    private val defaultCost: Int,
) : IPeripheralOperation<Any?>,
    IForgeConfigHandler {
    XP_TRANSFER(1000, 1),
    ;

    private var cooldown: ModConfigSpec.IntValue? = null
    private var cost: ModConfigSpec.IntValue? = null

    override fun getCooldown(context: Any?): Int = cooldown!!.get()

    override fun getCost(context: Any?): Int = cost!!.get()

    override fun computerDescription(): Map<String, Any> {
        val data: MutableMap<String, Any> = HashMap()
        data["name"] = settingsName()
        data["type"] = javaClass.name
        data["baseCooldown"] = cooldown!!.get()
        data["baseCost"] = cost!!.get()
        return data
    }

    override fun addToConfig(builder: ModConfigSpec.Builder) {
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
