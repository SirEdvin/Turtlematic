package site.siredvin.turtlematic.computercraft.turtle

import site.siredvin.peripheralium.api.peripheral.IOwnedPeripheral
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.turtlematic.util.DataStorageObjects
import site.siredvin.peripheralium.computercraft.turtle.PeripheralTurtleUpgrade
import site.siredvin.turtlematic.api.AutomataPeripheralBuildFunction
import site.siredvin.turtlematic.api.AutomataTickerFunction
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore

abstract class ClockwiseAnimatedTurtleUpgrade<T : IOwnedPeripheral<*>> : PeripheralTurtleUpgrade<T> {
    constructor(id: ResourceLocation, adjective: String, item: ItemStack) : super(id, adjective, item)
    constructor(id: ResourceLocation, item: ItemStack) : super(id, item)

    companion object {
        fun <T : IOwnedPeripheral<*>> dynamic(item: BaseAutomataCore, constructor: AutomataPeripheralBuildFunction<T>): ClockwiseAnimatedTurtleUpgrade<T> {
            return Dynamic(item.turtleID, item, constructor)
        }

        fun <T : IOwnedPeripheral<*>> ticker(item: BaseAutomataCore, constructor: AutomataPeripheralBuildFunction<T>, ticker: AutomataTickerFunction): ClockwiseAnimatedTurtleUpgrade<T> {
            return Ticker(item.turtleID, item, constructor, ticker)
        }
    }

    protected var tickCounterStorage = 0L

    protected var tickCounter: Long
        get() = tickCounterStorage
        set(value) {
            tickCounterStorage = value
        }

    private open class Dynamic<T : IOwnedPeripheral<*>>(
        id: ResourceLocation, protected val item: BaseAutomataCore, private val constructor: AutomataPeripheralBuildFunction<T>
        ): ClockwiseAnimatedTurtleUpgrade<T>(id, item.defaultInstance) {
        override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): T {
            return constructor.build(turtle, side, item.coreTier)
        }
    }

    private class Ticker<T : IOwnedPeripheral<*>>(
        id: ResourceLocation, item: BaseAutomataCore, constructor: AutomataPeripheralBuildFunction<T>, private val ticker: AutomataTickerFunction
    ): Dynamic<T>(id, item, constructor) {
        override fun update(turtle: ITurtleAccess, side: TurtleSide) {
            super.update(turtle, side)
            if (!turtle.level.isClientSide)
                ticker.tick(turtle, side, item.coreTier, tickCounter)
        }
    }

    // Optional callbacks for addons
    fun chargeConsumingCallback() {}

    override fun update(turtle: ITurtleAccess, side: TurtleSide) {
        super.update(turtle, side)
        tickCounter++
        if (DataStorageObjects.RotationCharge.consume(turtle, side))
            chargeConsumingCallback()
    }
}