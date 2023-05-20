package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.Util
import net.minecraft.core.BlockSource
import net.minecraft.core.BlockSourceImpl
import net.minecraft.core.Position
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.entity.projectile.ThrownPotion
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import site.siredvin.peripheralium.api.peripheral.IPeripheralOwner
import site.siredvin.peripheralium.api.storage.StorageUtils
import site.siredvin.peripheralium.computercraft.peripheral.OwnedPeripheral
import site.siredvin.peripheralium.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.peripheralium.util.DataStorageUtil
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.common.entities.ShootedItemProjectile
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.util.DataStorageObjects
import site.siredvin.turtlematic.util.TurtleDispenseBehavior
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class BowPeripheral(turtle: ITurtleAccess, side: TurtleSide) :
OwnedPeripheral<TurtlePeripheralOwner>(TYPE, TurtlePeripheralOwner(turtle, side)) {

    companion object: PeripheralConfiguration {
        override val TYPE = "bow"
    }

    internal class RandomItemDispenseBehavior(owner: IPeripheralOwner) : TurtleDispenseBehavior(owner) {
        override fun getProjectile(level: Level, targetPosition: Position, stack: ItemStack): ShootedItemProjectile {
            val newProjectile = ShootedItemProjectile(level, targetPosition.x(), targetPosition.y(), targetPosition.z())
            newProjectile.stack = stack
            return newProjectile
        }
    }

    private val dispenseBehavior: RandomItemDispenseBehavior by lazy {
        RandomItemDispenseBehavior(peripheralOwner)
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableBowTurtle

    @LuaFunction(mainThread = true)
    fun setAngle(angle: Double) {
        DataStorageObjects.Angle[peripheralOwner] = angle
    }

    @LuaFunction(mainThread = true)
    fun getAngle(): Double {
        return DataStorageObjects.Angle[peripheralOwner]
    }

    @LuaFunction(mainThread = true)
    fun shootItem(power: Double, limit: Optional<Int>): MethodResult {
        // TODO: add operations
        if (power <= 0.0) throw LuaException("Power cannot be equal or less then 0")
        val currentAngle = Math.toRadians(DataStorageObjects.Angle[peripheralOwner])
        val selectedSlot: Int = peripheralOwner.turtle.selectedSlot
        val turtleInventory: Container = peripheralOwner.turtle.inventory
        val selectedStack: ItemStack = turtleInventory.getItem(selectedSlot)
        val realLimit = limit.orElse(selectedStack.count)
        val stackToDispense = selectedStack.split(realLimit)
        val dispensedResult = dispenseBehavior.dispense(BlockSourceImpl(level as ServerLevel, pos), stackToDispense, power, currentAngle)
        if (!dispensedResult.isEmpty)
            StorageUtils.inplaceMerge(selectedStack, dispensedResult)
        turtleInventory.setItem(selectedSlot, selectedStack)
        return MethodResult.of(true)
    }
}