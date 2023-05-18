package site.siredvin.turtlematic.computercraft.plugins

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.world.Container
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.trading.Merchant
import net.minecraft.world.item.trading.MerchantOffer
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import site.siredvin.peripheralium.api.datatypes.VerticalDirection
import site.siredvin.peripheralium.api.peripheral.IPeripheralOperation
import site.siredvin.peripheralium.api.storage.ContainerUtils
import site.siredvin.peripheralium.util.world.FakePlayerProxy
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.computercraft.peripheral.forged.ExperienceAutomataCorePeripheral
import java.util.*
import java.util.function.Predicate

class AutomataTradePlugin(
    automataCore: ExperienceAutomataCorePeripheral,
    private val suitableEntity: Predicate<Entity> = Predicate { false }
) : AutomataCorePlugin(automataCore) {

    override val operations: List<IPeripheralOperation<*>>
        get() = listOf(SingleOperation.TRADE)

    private fun tradeImpl(merchant: Merchant, indexHint: Int?): MethodResult {
        if (merchant.offers.isEmpty()) return MethodResult.of(null, "There is no trade offers right now")
        if (merchant.tradingPlayer != null) return MethodResult.of(null, "This merchant is busy with another trade")
        val turtleInventory: Container = automataCore.peripheralOwner.turtle.inventory
        val firstItem = automataCore.peripheralOwner.toolInMainHand
        var secondItem = ItemStack.EMPTY
        // So, not the latest slot selected
        val selectedSlot = automataCore.peripheralOwner.turtle.selectedSlot
        if (selectedSlot != 15)
            secondItem = turtleInventory.getItem(selectedSlot + 1)
        val matchingOffers = mutableMapOf<Int, MerchantOffer>()
        merchant.offers.onEachIndexed { index, offer ->
            if (!offer.isOutOfStock && offer.costA.sameItem(firstItem) && (offer.costB.isEmpty || offer.costB.sameItem(secondItem)))
                matchingOffers[index] = offer
        }
        if (matchingOffers.isEmpty())
            return MethodResult.of(null, "No matching trades found")
        var matchingOffer: MerchantOffer? = null
        if (matchingOffers.size > 1)
            if (indexHint == null)
                return MethodResult.of(null, "Several overlapping offers found, please, provide index hint")
            if (matchingOffers[indexHint] == null)
                return MethodResult.of(null, "Incorrect index hint, there is no matching order for this index hint")
            matchingOffer = matchingOffers[indexHint]
        if (matchingOffers.size == 1)
            matchingOffer = matchingOffers.values.first()
        if (matchingOffer == null)
            return MethodResult.of(null, "Some random error in mod code, thats, well, should not happen")
        val costB: ItemStack = matchingOffer.costB
        val hasCostB = !costB.isEmpty
        if(firstItem.count < matchingOffer.costA.count || (hasCostB && secondItem.count < matchingOffer.costB.count))
            return MethodResult.of(null, "Not enough items to complete trade")

        return automataCore.withOperation(SingleOperation.TRADE) {
            turtleInventory.removeItem(selectedSlot, matchingOffer.costA.count)
            if (hasCostB) turtleInventory.removeItem(selectedSlot + 1, costB.count)
            ContainerUtils.toInventoryOrToWorld(
                matchingOffer.result,
                turtleInventory,
                selectedSlot,
                automataCore.peripheralOwner.pos.above(),
                automataCore.peripheralOwner.level!!
            )
            merchant.notifyTrade(matchingOffer)
            if (merchant is LivingEntity)
                merchant.playSound(merchant.notifyTradeSound, 1F, merchant.voicePitch)

            return@withOperation MethodResult.of(matchingOffer.result.count)
        }
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun trade(arguments: IArguments): MethodResult {
        val indexHintArgument = arguments.optInt(0)
        val indexHint = if (indexHintArgument.isEmpty) null else indexHintArgument.get() - 1
        val directionArgument = arguments.optString(1)
        val overwrittenDirection = if (directionArgument.isEmpty) null else VerticalDirection.luaValueOf(
            directionArgument.get()
        )
        val hit = automataCore.peripheralOwner.withPlayer({ FakePlayerProxy(it).findHit(
            skipEntity = false,
            skipBlock = true,
            entityFilter = suitableEntity
        ) }, overwrittenDirection = overwrittenDirection?.minecraftDirection)
        return when (hit.type) {
            HitResult.Type.MISS -> MethodResult.of(null, "No merchant found")
            HitResult.Type.BLOCK -> MethodResult.of(null, "No merchant found")
            HitResult.Type.ENTITY -> {
                val hitEntity: Entity = (hit as EntityHitResult).entity
                return if(hitEntity is Merchant) tradeImpl(hitEntity, indexHint) else MethodResult.of(null, "No merchant found")
            }
            null -> throw LuaException("This should never, never happen at all")
        }
    }
}