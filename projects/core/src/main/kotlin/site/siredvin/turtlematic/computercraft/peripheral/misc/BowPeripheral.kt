package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.core.BlockSourceImpl
import net.minecraft.core.Position
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.entity.projectile.*
import net.minecraft.world.item.ArrowItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import site.siredvin.broccolium.modules.storage.item.ItemStorageUtils
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.common.entities.ShootedItemProjectile
import site.siredvin.turtlematic.computercraft.operations.PowerOperation
import site.siredvin.turtlematic.computercraft.operations.PowerOperationContext
import site.siredvin.turtlematic.util.DataStorageObjects
import site.siredvin.turtlematic.util.TurtleDispenseBehavior
import site.siredvin.tweakium.modules.peripheral.OwnedPeripheral
import site.siredvin.tweakium.modules.peripheral.api.IPeripheralOwner
import site.siredvin.tweakium.modules.peripheral.boon.PeripheralOwnerBoonKey
import site.siredvin.tweakium.modules.peripheral.owner.TurtlePeripheralOwner
import java.util.*

class BowPeripheral(turtle: ITurtleAccess, side: TurtleSide) : OwnedPeripheral<TurtlePeripheralOwner>(type, TurtlePeripheralOwner(turtle, side)) {

    companion object : PeripheralConfiguration {
        override val type = "bow"
    }

    init {
        peripheralOwner.attachOperations(reduceRate = 1.0, cooldownThreshold = TurtlematicConfig.cooldownTresholdLevel)
        peripheralOwner.attachFuel()
        peripheralOwner.getBoon(PeripheralOwnerBoonKey.OPERATION)?.registerOperation(PowerOperation.SHOOT)
    }

    internal class RandomItemDispenseBehavior(private val suppressExtraLogic: Boolean, owner: IPeripheralOwner) : TurtleDispenseBehavior(owner) {
        override fun getProjectile(level: Level, targetPosition: Position, stack: ItemStack): Projectile {
            if (!suppressExtraLogic) {
                if (stack.`is`(Items.SPECTRAL_ARROW)) {
                    val newProjectile: AbstractArrow =
                        SpectralArrow(level, targetPosition.x(), targetPosition.y(), targetPosition.z())
                    newProjectile.pickup = AbstractArrow.Pickup.ALLOWED
                    stack.shrink(1)
                    return newProjectile
                }
                if (stack.`is`(Items.TIPPED_ARROW)) {
                    val newProjectile = Arrow(level, targetPosition.x(), targetPosition.y(), targetPosition.z())
                    newProjectile.setEffectsFromItem(stack)
                    newProjectile.pickup = AbstractArrow.Pickup.ALLOWED
                    stack.shrink(1)
                    return newProjectile
                }
                if (stack.item is ArrowItem) {
                    val newProjectile = Arrow(level, targetPosition.x(), targetPosition.y(), targetPosition.z())
                    newProjectile.pickup = AbstractArrow.Pickup.ALLOWED
                    stack.shrink(1)
                    return newProjectile
                }
            }
            val newProjectile = ShootedItemProjectile(level, targetPosition.x(), targetPosition.y(), targetPosition.z())
            newProjectile.stack = stack
            stack.count = 0
            return newProjectile
        }
    }

    private val dispenseBehavior: RandomItemDispenseBehavior by lazy {
        RandomItemDispenseBehavior(false, peripheralOwner)
    }

    private val suppressedDispenseBehavior: RandomItemDispenseBehavior by lazy {
        RandomItemDispenseBehavior(true, peripheralOwner)
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableBowTurtle

    @LuaFunction(mainThread = true)
    fun setAngle(angle: Double) {
        DataStorageObjects.Angle[peripheralOwner] = angle
    }

    @LuaFunction(mainThread = true)
    fun getAngle(): Double = DataStorageObjects.Angle[peripheralOwner]

    @LuaFunction(mainThread = true)
    fun shoot(power: Double, limit: Optional<Int>, suppressExtraLogic: Optional<Boolean>): MethodResult {
        if (power <= 0.0) throw LuaException("Power cannot be equal or less then 0")
        val limitedPower = power.coerceAtMost(TurtlematicConfig.bowTurtlePowerLimit)
        val currentAngle = Math.toRadians(DataStorageObjects.Angle[peripheralOwner])
        val selectedSlot: Int = peripheralOwner.turtle.selectedSlot
        val turtleInventory: Container = peripheralOwner.turtle.inventory
        val selectedStack: ItemStack = turtleInventory.getItem(selectedSlot)
        if (selectedStack.isEmpty) {
            return MethodResult.of(null, "Nothing to shoot")
        }
        val realLimit = limit.orElse(selectedStack.count)
        if (realLimit <= 0) {
            throw LuaException("Limit should be positive integer")
        }
        return peripheralOwner.withOperation(PowerOperation.SHOOT, PowerOperationContext(limitedPower), {
            val stackToDispense = selectedStack.split(realLimit)
            val dispensedResult = if (suppressExtraLogic.orElse(false)) {
                suppressedDispenseBehavior.dispense(
                    BlockSourceImpl(peripheralOwner.level as ServerLevel, peripheralOwner.pos),
                    stackToDispense,
                    limitedPower,
                    currentAngle,
                )
            } else {
                dispenseBehavior.dispense(
                    BlockSourceImpl(peripheralOwner.level as ServerLevel, peripheralOwner.pos),
                    stackToDispense,
                    limitedPower,
                    currentAngle,
                )
            }
            if (!selectedStack.isEmpty) {
                if (!dispensedResult.isEmpty) {
                    ItemStorageUtils.inplaceMerge(selectedStack, dispensedResult)
                }
                turtleInventory.setItem(selectedSlot, selectedStack)
            } else {
                turtleInventory.setItem(selectedSlot, dispensedResult)
            }
            return@withOperation MethodResult.of(true)
        })
    }
}
