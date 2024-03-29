package site.siredvin.turtlematic.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import site.siredvin.peripheralium.api.peripheral.IOwnedPeripheral
import site.siredvin.turtlematic.api.AutomataPeripheralBuildFunction
import site.siredvin.turtlematic.api.AutomataTickerFunction
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore

abstract class StarboundTurtleUpgrade<T : IOwnedPeripheral<*>> : ClockwiseTurtleUpgrade<T> {
    constructor(id: ResourceLocation, adjective: String, item: ItemStack) : super(id, adjective, item)
    constructor(id: ResourceLocation, item: ItemStack) : super(id, item)

    companion object {
        fun <T : IOwnedPeripheral<*>> dynamic(id: ResourceLocation, item: BaseAutomataCore, constructor: AutomataPeripheralBuildFunction<T>): StarboundTurtleUpgrade<T> {
            return Dynamic(id, item, constructor)
        }

        fun <T : IOwnedPeripheral<*>> ticker(id: ResourceLocation, item: BaseAutomataCore, constructor: AutomataPeripheralBuildFunction<T>, ticker: AutomataTickerFunction): StarboundTurtleUpgrade<T> {
            return Ticker(id, item, constructor, ticker)
        }
    }

    private open class Dynamic<T : IOwnedPeripheral<*>>(
        id: ResourceLocation,
        protected val item: BaseAutomataCore,
        private val constructor: AutomataPeripheralBuildFunction<T>,
    ) : StarboundTurtleUpgrade<T>(id, item.defaultInstance) {
        override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): T {
            return constructor.build(turtle, side, item.coreTier)
        }
    }

    private class Ticker<T : IOwnedPeripheral<*>>(
        id: ResourceLocation,
        item: BaseAutomataCore,
        constructor: AutomataPeripheralBuildFunction<T>,
        private val ticker: AutomataTickerFunction,
    ) : Dynamic<T>(id, item, constructor) {
        override fun update(turtle: ITurtleAccess, side: TurtleSide) {
            super.update(turtle, side)
            if (!turtle.level.isClientSide) {
                ticker.tick(turtle, side, item.coreTier, tickCounter)
            }
        }
    }

    override fun update(turtle: ITurtleAccess, side: TurtleSide) {
        super.update(turtle, side)
        if (!turtle.level.isClientSide) {
            if (turtle.level.random.nextDouble() <= TurtlematicConfig.starboundAutomataFuelGenerationChance) {
                turtle.addFuel(TurtlematicConfig.starboundAutomataFuelGenerationAmount)
            }
        }
    }
}
