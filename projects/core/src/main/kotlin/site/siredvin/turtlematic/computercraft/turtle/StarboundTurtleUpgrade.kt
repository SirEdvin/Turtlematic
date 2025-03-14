package site.siredvin.turtlematic.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.api.upgrades.UpgradeType
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import site.siredvin.turtlematic.api.AutomataPeripheralBuildFunction
import site.siredvin.turtlematic.api.AutomataTickerFunction
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.tweakium.modules.peripheral.api.IOwnedPeripheral

abstract class StarboundTurtleUpgrade<T : IOwnedPeripheral<*>> : ClockwiseTurtleUpgrade<T> {
    constructor(id: ResourceLocation, adjective: Component, item: ItemStack) : super(id, adjective, item)
    constructor(id: ResourceLocation, item: ItemStack) : super(id, item)

    companion object {

        fun <T : IOwnedPeripheral<*>> dynamic(id: ResourceLocation, item: BaseAutomataCore, upgradeType: UpgradeType<out ITurtleUpgrade>, constructor: AutomataPeripheralBuildFunction<T>): StarboundTurtleUpgrade<T> = Dynamic(id, item, upgradeType, constructor)

        fun <T : IOwnedPeripheral<*>> ticker(id: ResourceLocation, item: BaseAutomataCore, upgradeType: UpgradeType<out ITurtleUpgrade>, constructor: AutomataPeripheralBuildFunction<T>, ticker: AutomataTickerFunction): StarboundTurtleUpgrade<T> = Ticker(id, item, constructor, upgradeType, ticker)
    }

    private open class Dynamic<T : IOwnedPeripheral<*>>(
        id: ResourceLocation,
        protected val item: BaseAutomataCore,
        private val upgradeType: UpgradeType<out ITurtleUpgrade>,
        private val constructor: AutomataPeripheralBuildFunction<T>,
    ) : StarboundTurtleUpgrade<T>(id, item.defaultInstance) {
        override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): T = constructor.build(turtle, side, item.coreTier)
        override fun getType(): UpgradeType<out ITurtleUpgrade> = upgradeType
    }

    private class Ticker<T : IOwnedPeripheral<*>>(
        id: ResourceLocation,
        item: BaseAutomataCore,
        constructor: AutomataPeripheralBuildFunction<T>,
        upgradeType: UpgradeType<out ITurtleUpgrade>,
        private val ticker: AutomataTickerFunction,
    ) : Dynamic<T>(id, item, upgradeType, constructor) {
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
