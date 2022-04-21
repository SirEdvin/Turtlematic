package site.siredvin.lib.peripherals.owner

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import site.siredvin.lib.peripherals.IPeripheralOperation
import site.siredvin.lib.util.LibFakePlayer

interface IPeripheralOwner {
    val name: String?
@dan200.computercraft.api.lua.LuaFunction get() {
        return owner?.customName.toString()
    }
    val level: Level?
    val pos: BlockPos
    val facing: Direction
    val owner: Player?
    val dataStorage: CompoundTag
    fun markDataStorageDirty()

    fun <T> withPlayer(function: (LibFakePlayer) ->  T): T
    val toolInMainHand: ItemStack
    fun storeItem(stored: ItemStack): ItemStack
    fun destroyUpgrade()
    fun isMovementPossible(level: Level, pos: BlockPos): Boolean
    fun move(level: Level, pos: BlockPos): Boolean
    fun <T : IOwnerAbility> attachAbility(ability: PeripheralOwnerAbility<T>, abilityImplementation: T)
    fun <T : IOwnerAbility> getAbility(ability: PeripheralOwnerAbility<T>): T
    val abilities: Collection<IOwnerAbility>
    fun attachOperation(vararg operations: IPeripheralOperation<*>) {
        val operationAbility = OperationAbility(this)
        attachAbility(PeripheralOwnerAbility.OPERATION, operationAbility)
        for (operation in operations) operationAbility.registerOperation(operation)
    }

    fun attachOperation(operations: Collection<IPeripheralOperation<*>>) {
        val operationAbility = OperationAbility(this)
        attachAbility(PeripheralOwnerAbility.OPERATION, operationAbility)
        for (operation in operations) operationAbility.registerOperation(operation)
    }
}