package site.siredvin.turtlematic.integrations.computercraft.peripheral

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.TurtleAnimation
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.EntityHitResult
import site.siredvin.lib.peripherals.BasePeripheral
import site.siredvin.lib.peripherals.owner.TurtlePeripheralOwner
import site.siredvin.turtlematic.api.ISoulFeedableItem

class SoulScrapperPeripheral(peripheralOwner: TurtlePeripheralOwner) :
    BasePeripheral<TurtlePeripheralOwner>(TYPE, peripheralOwner) {
    companion object {
        const val TYPE = "soul_scrapper"
    }

    override val isEnabled: Boolean
        get() = true

    @LuaFunction(mainThread = true)
    fun harvestSoul(): MethodResult {
        peripheralOwner.turtle.playAnimation(
            if (peripheralOwner.side == TurtleSide.LEFT) TurtleAnimation.SWING_LEFT_TOOL else TurtleAnimation.SWING_RIGHT_TOOL
        )
        val toolInMainHand = peripheralOwner.toolInMainHand
        if (toolInMainHand.item !is ISoulFeedableItem) {
            return MethodResult.of(null, "Well, you should feed correct items!")
        }
        val feedableItem = toolInMainHand.item as ISoulFeedableItem
        return peripheralOwner.withPlayer({player ->
            val hit = player.findHit(skipEntity = false, skipBlock = true) { entity -> entity !is Player && entity is LivingEntity }
            if (hit !is EntityHitResult)
                return@withPlayer MethodResult.of(null, "Nothing to consume")
            val result = feedableItem.consumeEntitySoul(toolInMainHand, player, hit.entity as LivingEntity)
            if (result.rightPresent())
                return@withPlayer MethodResult.of(null, result.right)
            player.setItemInHand(InteractionHand.MAIN_HAND, result.left!!)
            return@withPlayer MethodResult.of(true)
        })
    }

    @LuaFunction(mainThread = true)
    fun getLeftEntities(): MethodResult {
        val toolInMainHand = peripheralOwner.toolInMainHand
        if (toolInMainHand.item !is ISoulFeedableItem) {
            return MethodResult.of(null, "Item cannot be used for soul harvesting")
        }
        val feedableItem = toolInMainHand.item as ISoulFeedableItem
        val activeRecipe = feedableItem.getActiveRecipe(toolInMainHand)
            ?: return MethodResult.of(null, "Item have no selected recipes yet")
        val data = ArrayList<Map<String, Any>>()
        feedableItem.getEntityRepresentation(toolInMainHand, activeRecipe).filter { it.leftCount > 0 }.forEach {
            data.add(mapOf(
                "name" to it.name,
                "leftCount" to it.leftCount
            ))
        }
        return MethodResult.of(data)
    }
}