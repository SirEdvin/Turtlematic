package site.siredvin.turtlematic.computercraft.operations

import net.neoforged.neoforge.common.ModConfigSpec
import site.siredvin.turtlematic.api.IForgeConfigHandler
import site.siredvin.tweakium.modules.peripheral.api.IPeripheralOperation

enum class SimpleFreeOperation(private val defaultCooldown: Int) :
    IPeripheralOperation<Any>,
    IForgeConfigHandler {
    CHAT_MESSAGE(100),
    ;

    private var cooldown: ModConfigSpec.IntValue? = null
    override fun addToConfig(builder: ModConfigSpec.Builder) {
        cooldown = builder.defineInRange(
            settingsName() + "Cooldown",
            defaultCooldown,
            1000,
            Int.MAX_VALUE,
        )
    }

    override fun getCooldown(context: Any): Int = cooldown!!.get()

    override fun getCost(context: Any): Int = 0

    override fun computerDescription(): Map<String, Any> {
        val data: MutableMap<String, Any> = HashMap()
        data["name"] = settingsName()
        data["type"] = javaClass.simpleName
        data["cooldown"] = cooldown!!.get()
        return data
    }
}
