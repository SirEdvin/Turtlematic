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
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.turtlematic.util.DataStorageObjects
import site.siredvin.tweakium.modules.peripheral.api.IOwnedPeripheral
import site.siredvin.tweakium.modules.turtle.StatefulPeripheralTurtleUpgrade

abstract class ClockwiseTurtleUpgrade<T : IOwnedPeripheral<*>> : StatefulPeripheralTurtleUpgrade<T> {
    constructor(id: ResourceLocation, adjective: Component, item: ItemStack) : super(id, adjective, item)
    constructor(id: ResourceLocation, item: ItemStack) : super(id, item)

    companion object {
        fun <T : IOwnedPeripheral<*>> dynamic(id: ResourceLocation, item: BaseAutomataCore, upgradeType: UpgradeType<out ITurtleUpgrade>, constructor: AutomataPeripheralBuildFunction<T>): ClockwiseTurtleUpgrade<T> = Dynamic(id, item, constructor, upgradeType)

        fun <T : IOwnedPeripheral<*>> ticker(id: ResourceLocation, item: BaseAutomataCore, upgradeType: UpgradeType<out ITurtleUpgrade>, constructor: AutomataPeripheralBuildFunction<T>, ticker: AutomataTickerFunction): ClockwiseTurtleUpgrade<T> = Ticker(id, item, constructor, upgradeType, ticker)
    }

    protected var tickCounterStorage = 0L

    protected var tickCounter: Long
        get() = tickCounterStorage
        set(value) {
            tickCounterStorage = value
        }

    private open class Dynamic<T : IOwnedPeripheral<*>>(
        id: ResourceLocation,
        protected val item: BaseAutomataCore,
        private val constructor: AutomataPeripheralBuildFunction<T>,
        private val upgradeType: UpgradeType<out ITurtleUpgrade>,
    ) : ClockwiseTurtleUpgrade<T>(id, item.defaultInstance) {
        override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): T = constructor.build(turtle, side, item.coreTier)
        override fun getType(): UpgradeType<out ITurtleUpgrade> = upgradeType
    }

    private class Ticker<T : IOwnedPeripheral<*>>(
        id: ResourceLocation,
        item: BaseAutomataCore,
        constructor: AutomataPeripheralBuildFunction<T>,
        upgradeType: UpgradeType<out ITurtleUpgrade>,
        private val ticker: AutomataTickerFunction,
    ) : Dynamic<T>(id, item, constructor, upgradeType) {
        override fun update(turtle: ITurtleAccess, side: TurtleSide) {
            super.update(turtle, side)
            if (!turtle.level.isClientSide) {
                ticker.tick(turtle, side, item.coreTier, tickCounter)
            }
        }
    }

    // Optional callbacks for addons
    fun chargeConsumingCallback() {}

    override fun update(turtle: ITurtleAccess, side: TurtleSide) {
        super.update(turtle, side)
        tickCounter++
        if (DataStorageObjects.RotationCharge.consume(turtle, side)) {
            chargeConsumingCallback()
        }
    }
}
