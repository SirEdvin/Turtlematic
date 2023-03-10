package site.siredvin.turtlematic.computercraft.plugins

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.world.Container
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.npc.Villager
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.trading.MerchantOffer
import net.minecraft.world.item.trading.MerchantOffers
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import site.siredvin.peripheralium.api.peripheral.IPeripheralOperation
import site.siredvin.peripheralium.util.ContainerHelpers
import site.siredvin.turtlematic.computercraft.datatypes.InteractionMode
import site.siredvin.turtlematic.computercraft.datatypes.VerticalDirection
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.computercraft.peripheral.automatas.BaseAutomataCorePeripheral
import java.util.*
import java.util.function.Predicate

class AutomataTradePlugin(
    automataCore: BaseAutomataCorePeripheral,
    private val allowedMods: Set<InteractionMode>,
    private val suitableEntity: Predicate<Entity> = Predicate { false }
) : AutomataCorePlugin(automataCore) {
    override val operations: Array<IPeripheralOperation<*>>
        get() = arrayOf(SingleOperation.TRADE)

    private fun tradeImpl(villager: Villager, firstCostSlot: Int, secondCostSlot: Optional<Int>): MethodResult {
        if(villager.villagerData.profession == VillagerProfession.NONE) return MethodResult.of(null, "no matching trades found")
        if(villager.isTrading) return MethodResult.of(null, "this villager is busy with another trade")
        val offers: MerchantOffers = villager.offers
        val turtleInventory: Container = automataCore.peripheralOwner.turtle.inventory
        val firstCostSlotItemStack: ItemStack = turtleInventory.getItem(firstCostSlot)
        val secondCostSlotItemStack: ItemStack = if(secondCostSlot.isEmpty) ItemStack.EMPTY else turtleInventory.getItem(secondCostSlot.get())
        var matchingOffer: MerchantOffer? = null
        for(offer: MerchantOffer in villager.offers)
            if(offer.costA.sameItem(firstCostSlotItemStack) && (offer.costB.isEmpty || offer.costB.sameItem(secondCostSlotItemStack))) {
                matchingOffer = offer
                break
        }
        if(matchingOffer==null) return MethodResult.of(null, "no matching trades found")
        val costB: ItemStack = matchingOffer.costB
        val hasCostB = !costB.isEmpty
        if(firstCostSlotItemStack.count < matchingOffer.costA.count || (hasCostB && secondCostSlotItemStack.count < matchingOffer.costB.count))
            return MethodResult.of(null, "not enough items to complete trade")

        turtleInventory.removeItem(firstCostSlot, matchingOffer.costA.count)
        if(hasCostB) turtleInventory.removeItem(secondCostSlot.get(), costB.count)
        ContainerHelpers.toInventoryOrToWorld(
            matchingOffer.result,
            turtleInventory,
            automataCore.peripheralOwner.turtle.selectedSlot,
            automataCore.peripheralOwner.pos.above(),
            automataCore.peripheralOwner.level!!
        )
        villager.notifyTrade(matchingOffer)
        villager.playSound(villager.notifyTradeSound, 1F, villager.voicePitch)

        return MethodResult.of(matchingOffer.result.count)
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun trade(arguments: IArguments): MethodResult {
        var directionArgument: Optional<String>
        try {
            directionArgument = arguments.optString(1)
        } catch(e: LuaException){
            directionArgument = Optional.empty()
        }
        val directionProvided = !directionArgument.isEmpty
        val overwrittenDirection = if (!directionProvided) null else VerticalDirection.luaValueOf(
            directionArgument.get()
        )
        val firstCostSlot = (if(directionProvided) arguments.getInt(1) else arguments.getInt(0)) - 1
        val secondCostSlot = run{
            val tempSecondCostSlot = if(directionProvided) arguments.optInt(2) else arguments.optInt(1)
            if(tempSecondCostSlot.isPresent) Optional.of(tempSecondCostSlot.get()-1) else tempSecondCostSlot
        }

        val hit = automataCore.peripheralOwner.withPlayer({ player -> player.findHit(
            skipEntity = false,
            skipBlock = false,
            entityFilter = suitableEntity
        ) }, overwrittenDirection = overwrittenDirection?.minecraftDirection)
        return when (hit.type) {
            HitResult.Type.MISS -> MethodResult.of(null, "no villager found")
            HitResult.Type.BLOCK -> MethodResult.of(null, "no villager found")
            HitResult.Type.ENTITY -> {
                val hitEntity: Entity = (hit as EntityHitResult).entity
                return if(hitEntity is Villager) tradeImpl(hitEntity, firstCostSlot, secondCostSlot) else MethodResult.of(null, "no villager found")
            }
            null -> throw LuaException("This should never, never happen at all")
        }
    }
}