package site.siredvin.turtlematic.computercraft.peripheral.forged

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.UpgradeRecipe
import net.minecraft.world.level.Level
import site.siredvin.lib.api.peripheral.IPeripheralOperation
import site.siredvin.lib.computercraft.peripheral.ability.PeripheralOwnerAbility
import site.siredvin.lib.util.LimitedInventory
import site.siredvin.lib.util.isCorrectSlot
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.operations.CountOperation
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import java.util.*
import kotlin.math.min

class SmithingAutomataCorePeripheral(turtle: ITurtleAccess, side: TurtleSide, tier: IAutomataCoreTier):
    ExperienceAutomataCorePeripheral(TYPE, turtle, side, tier) {

    companion object {
        const val TYPE = "smithingAutomataCore"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableSmithingAutomataCore

    override fun possibleOperations(): MutableList<IPeripheralOperation<*>> {
        val base = possibleOperations()
        base.add(SingleOperation.SMITH)
        base.add(CountOperation.SMELT)
        return base
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun smith(secondSlot: Int, targetSlot: Int): MethodResult {
        isCorrectSlot(secondSlot, "second")
        isCorrectSlot(targetSlot)
        val realTargetSlot = targetSlot - 1
        val realSecondSlot = secondSlot - 1
        return withOperation(SingleOperation.SMITH) {
            val turtleInventory: Container = peripheralOwner.turtle.inventory
            if (!turtleInventory.getItem(realTargetSlot).isEmpty) return@withOperation MethodResult.of(
                null,
                "Target slot should be empty!"
            )
            addRotationCycle()
            val limitedInventory =
                LimitedInventory(turtleInventory, intArrayOf(peripheralOwner.turtle.selectedSlot, realSecondSlot))
            val level: Level = level!!
            val optRecipe: Optional<UpgradeRecipe> =
                level.recipeManager.getRecipeFor(RecipeType.SMITHING, limitedInventory, level)
            if (!optRecipe.isPresent) return@withOperation MethodResult.of(null, "Cannot find smithing recipe")
            val recipe: UpgradeRecipe = optRecipe.get()
            val result: ItemStack = recipe.assemble(limitedInventory)
            limitedInventory.reduceCount(0)
            limitedInventory.reduceCount(1)
            turtleInventory.setItem(realTargetSlot, result)
            MethodResult.of(true)
        }
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun smelt(arguments: IArguments): MethodResult {
        val turtleInventory: Container = peripheralOwner.turtle.inventory
        val limitedInventory = LimitedInventory(turtleInventory, intArrayOf(peripheralOwner.turtle.selectedSlot))
        val targetSlot = arguments.getInt(0)
        isCorrectSlot(targetSlot)
        val realTargetSlot = targetSlot - 1
        if (!turtleInventory.getItem(realTargetSlot).isEmpty) return MethodResult.of(
            null,
            "Target slot should be empty"
        )
        val limit = arguments.optInt(1, Int.MAX_VALUE)
        val smeltCount = min(limit, limitedInventory.getItem(0).count)
        val level: Level = level!!
        val optRecipe: Optional<SmeltingRecipe> =
            level.recipeManager.getRecipeFor(RecipeType.SMELTING, limitedInventory, level)
        return if (!optRecipe.isPresent) MethodResult.of(
            null,
            "Cannot find smelting recipe"
        ) else withOperation(CountOperation.SMELT, smeltCount, {
            addRotationCycle(smeltCount / 2)
            val recipe: SmeltingRecipe = optRecipe.get()
            val result: ItemStack = recipe.assemble(limitedInventory)
            result.count = smeltCount
            turtleInventory.setItem(realTargetSlot, result)
            limitedInventory.reduceCount(0, smeltCount)
            peripheralOwner.getAbility(PeripheralOwnerAbility.EXPERIENCE)
                ?.adjustStoredXP((smeltCount * recipe.experience).toDouble())
            MethodResult.of(true)
        }, null)
    }
}