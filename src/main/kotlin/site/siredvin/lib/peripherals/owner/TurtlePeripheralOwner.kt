package site.siredvin.lib.peripherals.owner

import dan200.computercraft.ComputerCraft
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.shared.TurtlePermissions
import dan200.computercraft.shared.util.InventoryUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import site.siredvin.lib.peripherals.ability.PeripheralOwnerAbility
import site.siredvin.lib.peripherals.ability.TurtleFuelAbility
import site.siredvin.lib.util.DataStorageUtil
import site.siredvin.lib.util.FakePlayerProviderTurtle
import site.siredvin.lib.util.LibFakePlayer

class TurtlePeripheralOwner(val turtle: ITurtleAccess, val side: TurtleSide) : BasePeripheralOwner() {

    override val level: Level?
        get() = turtle.level
    override val pos: BlockPos
        get() = turtle.position
    override val facing: Direction
        get() = turtle.direction
    override val owner: Player?
        get() {
            val owningPlayer = turtle.owningPlayer ?: return null
            return turtle.level.getPlayerByUUID(owningPlayer.id)
        }
    override val dataStorage: CompoundTag
        get() = DataStorageUtil.getDataStorage(turtle, side)

    override fun markDataStorageDirty() {
        turtle.updateUpgradeNBTData(side)
    }

    override fun <T> withPlayer(function: (LibFakePlayer) -> T, overwrittenDirection: Direction?): T {
        return FakePlayerProviderTurtle.withPlayer(turtle, function, overwrittenDirection = overwrittenDirection)
    }

    override val toolInMainHand: ItemStack
        get() = turtle.inventory.getItem(turtle.selectedSlot)

    override fun storeItem(stored: ItemStack): ItemStack {
        return InventoryUtil.storeItems(stored, turtle.itemHandler, turtle.selectedSlot)
    }

    override fun destroyUpgrade() {
        turtle.setUpgrade(side, null)
    }

    override fun isMovementPossible(level: Level, pos: BlockPos): Boolean {
        return FakePlayerProviderTurtle.withPlayer(turtle, {player ->
            if (level.isOutsideBuildHeight(pos))
                return@withPlayer false;
            if (!level.isInWorldBounds(pos))
                return@withPlayer false;
            if (ComputerCraft.turtlesObeyBlockProtection && !TurtlePermissions.isBlockEnterable(level, pos, player))
                return@withPlayer false;
            if (!level.isLoaded(pos))
                return@withPlayer false;
            return@withPlayer level.worldBorder.isWithinBounds(pos);
        });
    }

    override fun move(level: Level, pos: BlockPos): Boolean {
        return turtle.teleportTo(level, pos)
    }

    fun attachFuel(maxFuelConsumptionLevel: Int): TurtlePeripheralOwner {
        attachAbility(PeripheralOwnerAbility.FUEL, TurtleFuelAbility(this, maxFuelConsumptionLevel))
        return this
    }
}