package site.siredvin.turtlematic.computercraft.peripheral.forged

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.world.Container
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmithingRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import site.siredvin.broccolium.modules.platform.PlatformToolkit
import site.siredvin.broccolium.modules.storage.item.ContainerUtils
import site.siredvin.broccolium.modules.storage.item.LimitedInventory
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.operations.CountOperation
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.tweakium.modules.peripheral.api.IPeripheralOperation
import site.siredvin.tweakium.modules.peripheral.api.TransformInteractionMode
import site.siredvin.tweakium.modules.peripheral.api.VerticalDirection
import site.siredvin.tweakium.modules.peripheral.boon.PeripheralOwnerBoonKey
import java.util.*
import kotlin.math.min

class SmithingAutomataCorePeripheral(turtle: ITurtleAccess, side: TurtleSide, tier: IAutomataCoreTier) : ExperienceAutomataCorePeripheral(type, turtle, side, tier) {

    companion object : PeripheralConfiguration {
        override val type = "smithingAutomata"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableSmithingAutomataCore

    override fun possibleOperations(): MutableList<IPeripheralOperation<*>> {
        val base = super.possibleOperations()
        base.add(SingleOperation.SMITH)
        base.add(CountOperation.SMELT)
        return base
    }

    private fun isEditable(pos: BlockPos): Boolean = !peripheralOwner.withPlayer({
        PlatformToolkit.get().isBlockProtected(pos, it.fakePlayer.level().getBlockState(pos), it.fakePlayer)
    })

    private fun findBlock(overwrittenDirection: VerticalDirection?): Pair<Pair<BlockHitResult, BlockState>?, MethodResult?> {
        val hit = peripheralOwner.withPlayer({
            val hit = it.findHit(skipEntity = true, skipBlock = false)
            if (hit !is BlockHitResult) {
                return@withPlayer null
            }
            return@withPlayer hit
        }, overwrittenDirection = overwrittenDirection?.minecraftDirection) ?: return Pair(null, MethodResult.of(null, "There is nothing to work with"))
        val blockState = peripheralOwner.level!!.getBlockState(hit.blockPos)
        if (blockState.isAir) {
            return Pair(null, MethodResult.of(null, "There is nothing to work with"))
        }
        if (!isEditable(hit.blockPos)) {
            return Pair(null, MethodResult.of(null, "This block is protected"))
        }
        return Pair(Pair(hit, blockState), null)
    }

    private fun smeltItem(arguments: IArguments): MethodResult {
        val turtleInventory: Container = peripheralOwner.turtle.inventory
        val limitedInventory = SingleRecipeInput(peripheralOwner.toolInMainHand)
        val limit = arguments.optInt(1, Int.MAX_VALUE)
        val smeltCount = min(limit, limitedInventory.getItem(0).count)
        val level: Level = peripheralOwner.level!!
        val optRecipe: Optional<RecipeHolder<SmeltingRecipe>> =
            level.recipeManager.getRecipeFor(RecipeType.SMELTING, limitedInventory, level)
        return if (!optRecipe.isPresent) {
            MethodResult.of(
                null,
                "Cannot find smelting recipe",
            )
        } else {
            withOperation(CountOperation.SMELT, smeltCount, {
                addRotationCycle(smeltCount / 2)
                val recipe: SmeltingRecipe = optRecipe.get().value
                val result: ItemStack = recipe.assemble(limitedInventory, RegistryAccess.EMPTY)
                result.count *= smeltCount
                peripheralOwner.toolInMainHand.shrink(smeltCount)
                ContainerUtils.toInventoryOrToWorld(
                    result,
                    turtleInventory,
                    peripheralOwner.turtle.selectedSlot,
                    peripheralOwner.pos.relative(peripheralOwner.facing),
                    level,
                )
                peripheralOwner.getBoon(PeripheralOwnerBoonKey.EXPERIENCE)
                    ?.adjustStoredXP((smeltCount * recipe.experience).toDouble())
                MethodResult.of(true)
            }, null)
        }
    }

    private fun smeltBlock(arguments: IArguments): MethodResult {
        val directionArgument = arguments.optString(1)
        val overwrittenDirection = if (directionArgument.isEmpty) {
            null
        } else {
            VerticalDirection.luaValueOf(
                directionArgument.get(),
            )
        }
        val blockSearchResult = findBlock(overwrittenDirection)
        if (blockSearchResult.second != null) {
            return blockSearchResult.second!!
        }
        val blockState = blockSearchResult.first!!.second
        val hit = blockSearchResult.first!!.first
        val level = peripheralOwner.level!!
        val fakeContainer = SingleRecipeInput(blockState.block.asItem().defaultInstance)
        val optRecipe = level.recipeManager.getRecipeFor(RecipeType.SMELTING, fakeContainer, level)
        if (optRecipe.isEmpty) {
            return MethodResult.of(null, "Cannot perform in-place smelting for this block")
        }
        return withOperation(CountOperation.SMELT, 1, {
            val recipe = optRecipe.get()
            val recipeResult = recipe.value.getResultItem(RegistryAccess.EMPTY)
            if (recipeResult.item is BlockItem && recipeResult.count == 1) {
                val targetBlockState = (recipeResult.item as BlockItem).block.defaultBlockState()
                level.setBlockAndUpdate(hit.blockPos, targetBlockState)
            } else {
                level.setBlockAndUpdate(hit.blockPos, Blocks.AIR.defaultBlockState())
                ContainerUtils.toInventoryOrToWorld(
                    recipeResult.copy(),
                    peripheralOwner.turtle.inventory,
                    peripheralOwner.turtle.selectedSlot,
                    peripheralOwner.pos.relative(peripheralOwner.facing),
                    level,
                )
            }
            peripheralOwner.getBoon(PeripheralOwnerBoonKey.EXPERIENCE)?.adjustStoredXP(recipe.value.experience.toDouble())
            return@withOperation MethodResult.of(true)
        })
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun smith(): MethodResult {
        return withOperation(SingleOperation.SMITH) {
            val turtleInventory: Container = peripheralOwner.turtle.inventory
            val selectedSlot = peripheralOwner.turtle.selectedSlot
            if (selectedSlot + 2 >= turtleInventory.containerSize) {
                return@withOperation MethodResult.of(null, "Cannot use last and pre-last slot as first for smith operation")
            }
            val limitedInventory =
                LimitedInventory(turtleInventory, intArrayOf(selectedSlot, selectedSlot + 1, selectedSlot + 2))
            val recipeInput = SmithingRecipeInput(limitedInventory.getItem(0), limitedInventory.getItem(1), limitedInventory.getItem(2))
            val level: Level = peripheralOwner.level!!
            val optRecipe =
                level.recipeManager.getRecipeFor(RecipeType.SMITHING, recipeInput, level)
            if (!optRecipe.isPresent) return@withOperation MethodResult.of(null, "Cannot find smithing recipe")
            val recipe = optRecipe.get()
            val result: ItemStack = recipe.value.assemble(recipeInput, RegistryAccess.EMPTY)
            limitedInventory.reduceCount(0)
            limitedInventory.reduceCount(1)
            limitedInventory.reduceCount(2)
            ContainerUtils.toInventoryOrToWorld(
                result,
                turtleInventory,
                peripheralOwner.turtle.selectedSlot,
                peripheralOwner.pos.relative(peripheralOwner.facing),
                level,
            )
            MethodResult.of(true)
        }
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun smelt(arguments: IArguments): MethodResult = when (TransformInteractionMode.luaValueOf(arguments.getString(0))) {
        TransformInteractionMode.BLOCK -> smeltBlock(arguments)
        TransformInteractionMode.INVENTORY -> smeltItem(arguments)
    }
}
