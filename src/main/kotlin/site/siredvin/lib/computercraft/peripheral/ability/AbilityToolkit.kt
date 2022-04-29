package site.siredvin.lib.computercraft.peripheral.ability

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.shared.turtle.blocks.TileTurtle
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import site.siredvin.lib.api.peripheral.IBasePeripheral
import site.siredvin.lib.api.peripheral.IOwnerAbility
import site.siredvin.lib.util.Pair

object AbilityToolkit {

    private fun <T: IOwnerAbility> extractAbilityFromTurtle(turtle: ITurtleAccess, side: TurtleSide, ability: PeripheralOwnerAbility<T>): T? {
        val targetPeripheral = turtle.getPeripheral(side)
        if (targetPeripheral !is IBasePeripheral<*>)
            return null
        return targetPeripheral.peripheralOwner!!.getAbility(ability)
    }

    fun <T: IOwnerAbility> extractAbility(ability: PeripheralOwnerAbility<T>, level: Level, pos: BlockPos): Pair<T?, String?> {
        val entity: BlockEntity = level.getBlockEntity(pos)
            ?: return Pair.onlyRight("Target block doesn't posses required ability")
        if (entity is TileTurtle) {
            val turtle = entity.access
            val targetAbility = extractAbilityFromTurtle(turtle, TurtleSide.LEFT, ability) ?: extractAbilityFromTurtle(turtle, TurtleSide.RIGHT, ability)
            ?: return Pair.onlyRight("Turtle doesn't posses required ability")
            return Pair.onlyLeft(targetAbility)
        }
        return Pair.onlyRight("Target block doesn't posses required ability")
    }
}