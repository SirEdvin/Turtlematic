package site.siredvin.lib.computercraft.pocket

import site.siredvin.lib.api.peripheral.IBasePeripheral
import dan200.computercraft.api.pocket.AbstractPocketUpgrade
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike
import dan200.computercraft.api.pocket.IPocketAccess
import dan200.computercraft.api.peripheral.IPeripheral
import site.siredvin.lib.computercraft.peripheral.DisabledPeripheral
import site.siredvin.turtlematic.util.pocketAdjective
import java.util.function.Supplier

abstract class BasePocketUpgrade<T : IBasePeripheral<*>> : AbstractPocketUpgrade {
    protected var peripheral: T? = null

    protected constructor(id: ResourceLocation, adjective: String?, stack: Supplier<out ItemLike?>?) : super(
        id,
        adjective,
        stack
    ) {
    }

    protected constructor(id: ResourceLocation, stack: Supplier<out ItemLike?>?) : super(
        id,
        pocketAdjective(id.path),
        stack
    ) {
    }

    protected abstract fun getPeripheral(access: IPocketAccess?): T
    override fun createPeripheral(access: IPocketAccess): IPeripheral? {
        peripheral = getPeripheral(access)
        return if (!peripheral!!.isEnabled) DisabledPeripheral else peripheral
    }
}