package site.siredvin.turtlematic.computercraft.plugins

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.AABB
import site.siredvin.broccolium.modules.base.util.world.ScanUtils
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.computercraft.peripheral.automatas.BaseAutomataCorePeripheral
import site.siredvin.tweakium.modules.peripheral.api.IPeripheralFunction
import site.siredvin.tweakium.modules.peripheral.api.IPeripheralOperation
import site.siredvin.tweakium.modules.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.tweakium.modules.plugins.PeripheralPluginUtils
import java.util.function.Predicate

class AutomataItemSuckPlugin(automataCore: BaseAutomataCorePeripheral) : AutomataCorePlugin(automataCore) {
    override val operations: List<IPeripheralOperation<*>>
        get() = listOf(SingleOperation.SUCK)

    protected fun getBox(pos: BlockPos): AABB {
        val interactionRadius = automataCore.interactionRadius
        return ScanUtils.getBox(pos, interactionRadius.toDouble())
    }

    protected val items: List<ItemEntity>
        get() {
            val owner: TurtlePeripheralOwner = automataCore.peripheralOwner
            return owner.level!!.getEntitiesOfClass(ItemEntity::class.java, getBox(owner.pos))
        }

    protected fun suckItem(entity: ItemEntity, requiredQuantity: Int): Int {
        val stack: ItemStack = entity.item.copy()
        val storeStack: ItemStack
        val leaveStack: ItemStack
        if (stack.count > requiredQuantity) {
            storeStack = stack.split(requiredQuantity)
            leaveStack = stack
        } else {
            storeStack = stack
            leaveStack = ItemStack.EMPTY
        }
        val remainder: ItemStack = automataCore.peripheralOwner.storeItem(storeStack)
        if (remainder != storeStack) {
            if (remainder.isEmpty && leaveStack.isEmpty) {
                entity.remove(Entity.RemovalReason.KILLED)
            } else if (remainder.isEmpty) {
                entity.item = leaveStack
            } else if (leaveStack.isEmpty) {
                entity.item = remainder
            } else {
                leaveStack.grow(remainder.count)
                entity.item = leaveStack
            }
        }
        return storeStack.count - remainder.count
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun suck(arguments: IArguments): MethodResult {
        val requiredQuantityArg: Int = arguments.optInt(0, Int.MAX_VALUE)
        val itemPredicate: Predicate<ItemStack> = PeripheralPluginUtils.itemQueryToPredicate(arguments.get(1))
        return automataCore.withOperation(
            SingleOperation.SUCK,
            IPeripheralFunction {
                if (requiredQuantityArg == 0) {
                    return@IPeripheralFunction MethodResult.of(true)
                }
                val items: List<ItemEntity> = items
                if (items.isEmpty()) {
                    MethodResult.of(null, "Nothing to take")
                }
                var requiredQuantity = requiredQuantityArg
                for (entity in items) {
                    if (itemPredicate.test(entity.item)) {
                        requiredQuantity -= suckItem(entity, requiredQuantity)
                    }
                    if (requiredQuantity <= 0) {
                        break
                    }
                }
                return@IPeripheralFunction MethodResult.of(true)
            },
        )
    }
}
