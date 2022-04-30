package site.siredvin.turtlematic.computercraft.peripheral.forged

import dan200.computercraft.ComputerCraft
import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.shared.TurtlePermissions
import net.minecraft.core.BlockPos
import net.minecraft.world.Container
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.UpgradeRecipe
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import site.siredvin.lib.api.peripheral.IPeripheralOperation
import site.siredvin.lib.computercraft.peripheral.ability.PeripheralOwnerAbility
import site.siredvin.lib.util.*
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.datatypes.TransformInteractionMode
import site.siredvin.turtlematic.computercraft.datatypes.VerticalDirection
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
        val base = super.possibleOperations()
        base.add(SingleOperation.SMITH)
        base.add(CountOperation.SMELT)
        return base
    }

    private fun isEditable(pos: BlockPos): Boolean {
        return peripheralOwner.withPlayer({
            ComputerCraft.turtlesObeyBlockProtection && TurtlePermissions.isBlockEditable(level, pos, it)
        })
    }

    private fun findBlock(overwrittenDirection: VerticalDirection?): Pair<Pair<BlockHitResult, BlockState>?, MethodResult?> {
        val hit = peripheralOwner.withPlayer({
            val hit = it.findHit(skipEntity = true, skipBlock = false)
            if (hit !is BlockHitResult)
                return@withPlayer null
            return@withPlayer hit
        }, overwrittenDirection = overwrittenDirection?.minecraftDirection) ?: return Pair.onlyRight(MethodResult.of(null, "There is nothing to work with"))
        val blockState = level!!.getBlockState(hit.blockPos)
        if (blockState.isAir)
            return Pair.onlyRight(MethodResult.of(null, "There is nothing to work with"))
        if (!isEditable(hit.blockPos))
            return Pair.onlyRight(MethodResult.of(null, "This block is protected"))
        return Pair.onlyLeft(Pair(hit, blockState))
    }

    private fun smeltItem(arguments: IArguments): MethodResult {
        val turtleInventory: Container = peripheralOwner.turtle.inventory
        val limitedInventory = LimitedInventory(turtleInventory, intArrayOf(peripheralOwner.turtle.selectedSlot))
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
            result.count = result.count * smeltCount
            limitedInventory.reduceCount(0, smeltCount)
            InsertionHelpers.toInventoryOrToWorld(
                result, turtleInventory, peripheralOwner.turtle.selectedSlot,
                pos.relative(peripheralOwner.facing), level
            )
            peripheralOwner.getAbility(PeripheralOwnerAbility.EXPERIENCE)
                ?.adjustStoredXP((smeltCount * recipe.experience).toDouble())
            MethodResult.of(true)
        }, null)
    }

    private fun smeltBlock(arguments: IArguments): MethodResult {
        val directionArgument = arguments.optString(1)
        val overwrittenDirection = if (directionArgument.isEmpty) null else VerticalDirection.luaValueOf(
            directionArgument.get()
        )
        val blockSearchResult = findBlock(overwrittenDirection)
        if (blockSearchResult.rightPresent())
            return blockSearchResult.right!!
        val blockState = blockSearchResult.left!!.right
        val hit = blockSearchResult.left!!.left
        val level = level!!
        val fakeContainer = FakeItemContainer(blockState.block.asItem().defaultInstance)
        val optRecipe = level.recipeManager.getRecipeFor(RecipeType.SMELTING, fakeContainer, level)
        if (optRecipe.isEmpty)
            return MethodResult.of(null, "Cannot perform in-place smelting for this block")
        val recipe = optRecipe.get()
        if (recipe.resultItem.item !is BlockItem || recipe.resultItem.count != 1)
            return MethodResult.of(
                null,
                "Cannot perform in-place transformation to $target, choose another target or transform it in inventory"
            )
        val targetBlockState = (recipe.resultItem.item as BlockItem).block.defaultBlockState()
        level.setBlockAndUpdate(hit.blockPos, targetBlockState)
        return MethodResult.of(true)
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun smith(): MethodResult {
        return withOperation(SingleOperation.SMITH) {
            val turtleInventory: Container = peripheralOwner.turtle.inventory
            addRotationCycle()
            val selectedSlot = peripheralOwner.turtle.selectedSlot
            if (selectedSlot + 1 == turtleInventory.containerSize)
                return@withOperation MethodResult.of(null, "Cannot use last slot as first for smith operation")
            val limitedInventory =
                LimitedInventory(turtleInventory, intArrayOf(selectedSlot, selectedSlot + 1))
            val level: Level = level!!
            val optRecipe: Optional<UpgradeRecipe> =
                level.recipeManager.getRecipeFor(RecipeType.SMITHING, limitedInventory, level)
            if (!optRecipe.isPresent) return@withOperation MethodResult.of(null, "Cannot find smithing recipe")
            val recipe: UpgradeRecipe = optRecipe.get()
            val result: ItemStack = recipe.assemble(limitedInventory)
            limitedInventory.reduceCount(0)
            limitedInventory.reduceCount(1)
            InsertionHelpers.toInventoryOrToWorld(
                result, turtleInventory, peripheralOwner.turtle.selectedSlot,
                pos.relative(peripheralOwner.facing), level
            )
            MethodResult.of(true)
        }
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun smelt(arguments: IArguments): MethodResult {
        return when (TransformInteractionMode.luaValueOf(arguments.getString(0))) {
            TransformInteractionMode.BLOCK -> smeltBlock(arguments)
            TransformInteractionMode.INVENTORY -> smeltItem(arguments)
        }
    }
}