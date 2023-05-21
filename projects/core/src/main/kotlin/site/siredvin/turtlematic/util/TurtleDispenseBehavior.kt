package site.siredvin.turtlematic.util

import net.minecraft.core.BlockSource
import net.minecraft.core.Direction
import net.minecraft.core.Position
import net.minecraft.core.PositionImpl
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import site.siredvin.peripheralium.api.peripheral.IPeripheralOwner
import kotlin.math.cos
import kotlin.math.sin

abstract class TurtleDispenseBehavior constructor(protected val owner: IPeripheralOwner) {
    fun getDispensePosition(direction: Direction, turtleBlockSource: BlockSource): Position {
        val x: Double = turtleBlockSource.x() + 0.7 * direction.stepX
        val y: Double = turtleBlockSource.y() + 0.7 * direction.stepY
        val z: Double = turtleBlockSource.z() + 0.7 * direction.stepZ
        return PositionImpl(x, y, z)
    }

    fun dispense(turtleBlockSource: BlockSource, stack: ItemStack, power: Double, angle: Double): ItemStack {
        val resultStack = execute(turtleBlockSource, stack, power, angle)
        playSound(turtleBlockSource)
        return resultStack
    }

    open fun execute(turtleBlockSource: BlockSource, stack: ItemStack, power: Double, angle: Double): ItemStack {
        val level: Level = turtleBlockSource.level
        val currentDirection = owner.facing
        val targetPosition  = getDispensePosition(currentDirection, turtleBlockSource)
        val entity: Projectile = getProjectile(level, targetPosition, stack)
        val directionalMovement = power * cos(angle)
        val verticalMovement = power * sin(angle)
        entity.advancedShoot(Vec3(
            currentDirection.stepX.toDouble() * directionalMovement,
            currentDirection.stepY.toDouble() + verticalMovement,
            currentDirection.stepZ.toDouble() * directionalMovement
        ))
        level.addFreshEntity(entity)
        return stack
    }

    protected fun playSound(turtleBlockSource: BlockSource) {
        turtleBlockSource.level.levelEvent(1002, turtleBlockSource.pos, 0)
    }

    abstract fun getProjectile(level: Level, targetPosition: Position, stack: ItemStack): Projectile
}