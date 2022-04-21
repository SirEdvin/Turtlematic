package site.siredvin.lib.util

import com.mojang.authlib.GameProfile
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.shared.util.InventoryUtil
import dan200.computercraft.shared.util.WorldUtil
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import java.util.*
import java.util.function.Function

object FakePlayerProviderTurtle {
    /*
    Highly inspired by https://github.com/SquidDev-CC/plethora/blob/minecraft-1.12/src/main/java/org/squiddev/plethora/integration/computercraft/FakePlayerProviderTurtle.java
    */
    private val registeredPlayers: WeakHashMap<ITurtleAccess, LibFakePlayer> =
        WeakHashMap<ITurtleAccess, LibFakePlayer>()

    private fun getPlayer(turtle: ITurtleAccess, profile: GameProfile?): LibFakePlayer {
        var fake: LibFakePlayer? = registeredPlayers[turtle]
        if (fake == null) {
            fake = LibFakePlayer(turtle.level as ServerLevel, null, profile)
            registeredPlayers[turtle] = fake
        }
        return fake
    }

    private fun load(player: LibFakePlayer, turtle: ITurtleAccess, overwrittenDirection: Direction? = null) {
        val direction = overwrittenDirection ?: turtle.direction
        player.setLevel(turtle.level as ServerLevel)
        val position = turtle.position
        // Player position
        val pitch: Float = if (direction == Direction.UP) -90f else if (direction == Direction.DOWN) 90f else 0f
        val yaw: Float =
            if (direction == Direction.SOUTH) 0f else if (direction == Direction.WEST) 90f else if (direction == Direction.NORTH) 180f else -90f
        val sideVec = direction.normal
        val a = direction.axis
        val ad = direction.axisDirection
        val x = if (a === Direction.Axis.X && ad == Direction.AxisDirection.NEGATIVE) -.5 else .5 + sideVec.x / 1.9
        val y = 0.5 + sideVec.y / 1.9
        val z = if (a === Direction.Axis.Z && ad == Direction.AxisDirection.NEGATIVE) -.5 else .5 + sideVec.z / 1.9
        player.moveTo(position.x + x, position.y + y, position.z + z, yaw, pitch)
        // Player inventory
        val playerInventory: Inventory = player.getInventory()
        playerInventory.selected = 0

        // Copy primary items into player inventory and empty the rest
        val turtleInventory = turtle.itemHandler
        val size = turtleInventory.size()
        val largerSize = playerInventory.containerSize
        playerInventory.selected = turtle.selectedSlot
        for (i in 0 until size) {
            playerInventory.setItem(i, turtleInventory.getStack(i))
        }
        for (i in size until largerSize) {
            playerInventory.setItem(i, ItemStack.EMPTY)
        }

        // Add properties
        val activeStack: ItemStack = player.getItemInHand(InteractionHand.MAIN_HAND)
        if (!activeStack.isEmpty) {
            player.attributes.addTransientAttributeModifiers(activeStack.getAttributeModifiers(EquipmentSlot.MAINHAND))
        }
    }

    private fun unload(player: LibFakePlayer, turtle: ITurtleAccess) {
        val playerInventory: Inventory = player.inventory
        playerInventory.selected = 0

        // Remove properties
        val activeStack: ItemStack = player.getItemInHand(InteractionHand.MAIN_HAND)
        if (!activeStack.isEmpty) {
            player.attributes.removeAttributeModifiers(activeStack.getAttributeModifiers(EquipmentSlot.MAINHAND))
        }

        // Copy primary items into turtle inventory and then insert/drop the rest
        val turtleInventory = turtle.itemHandler
        val size: Int = turtleInventory.size()
        val largerSize = playerInventory.containerSize
        playerInventory.selected = turtle.selectedSlot
        for (i in 0 until size) {
            turtleInventory.setStack(i, playerInventory.getItem(i))
            playerInventory.setItem(i, ItemStack.EMPTY)
        }
        for (i in size until largerSize) {
            var remaining = playerInventory.getItem(i)
            if (!remaining.isEmpty) {
                remaining = InventoryUtil.storeItems(remaining, turtleInventory)
                if (!remaining.isEmpty) {
                    val position = turtle.position
                    WorldUtil.dropItemStack(remaining, turtle.level, position, turtle.direction.opposite)
                }
            }
            playerInventory.setItem(i, ItemStack.EMPTY)
        }
    }

    fun <T> withPlayer(turtle: ITurtleAccess, function: Function<LibFakePlayer, T>,  overwrittenDirection: Direction? = null): T {
        val player: LibFakePlayer = getPlayer(turtle, turtle.owningPlayer)
        load(player, turtle, overwrittenDirection = overwrittenDirection)
        val result = function.apply(player)
        unload(player, turtle)
        return result
    }
}