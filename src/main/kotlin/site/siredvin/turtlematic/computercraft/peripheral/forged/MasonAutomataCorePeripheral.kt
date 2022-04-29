package site.siredvin.turtlematic.computercraft.peripheral.forged

import dan200.computercraft.ComputerCraft
import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.shared.TurtlePermissions
import dan200.computercraft.shared.util.InventoryUtil
import dan200.computercraft.shared.util.ItemStorage
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.StringRepresentable
import net.minecraft.world.Container
import net.minecraft.world.Containers
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.block.state.properties.Half
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.phys.BlockHitResult
import site.siredvin.lib.api.peripheral.IPeripheralOperation
import site.siredvin.lib.util.FakeItemContainer
import site.siredvin.lib.util.LimitedInventory
import site.siredvin.lib.util.representation.LuaInterpretation
import site.siredvin.lib.util.Pair
import site.siredvin.lib.util.representation.stateProperties
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.datatypes.TransformInteractionMode
import site.siredvin.turtlematic.computercraft.datatypes.VerticalDirection
import site.siredvin.turtlematic.computercraft.operations.CountOperation
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.computercraft.plugins.AutomataLookPlugin

class MasonAutomataCorePeripheral(turtle: ITurtleAccess, side: TurtleSide, tier: IAutomataCoreTier):
    ExperienceAutomataCorePeripheral(TYPE, turtle, side, tier) {

    companion object {
        const val TYPE = "masonAutomataCore"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableMasonAutomataCore

    init {
        addPlugin(AutomataLookPlugin(this, blockStateEnriches = listOf(::stateProperties)))
    }

    override fun possibleOperations(): MutableList<IPeripheralOperation<*>> {
        val base = super.possibleOperations()
        base.add(CountOperation.CHISEL)
        base.add(SingleOperation.ROTATE)
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
            return Pair.onlyRight(MethodResult.of(null, "Nothing to chisel"))
        if (!isEditable(hit.blockPos))
            return Pair.onlyRight(MethodResult.of(null, "This block is protected"))
        return Pair.onlyLeft(Pair(hit, blockState))
    }

    private fun chiselItem(target: String): MethodResult {
        val level = level!!
        val targetItem = Registry.ITEM.get(ResourceLocation(target))
        if (targetItem == Items.AIR)
            return MethodResult.of(null, "Cannot find item with id $target")
        val turtleInventory = peripheralOwner.turtle.inventory
        val fakeContainer = LimitedInventory(turtleInventory, intArrayOf(peripheralOwner.turtle.selectedSlot))
        val candidates = level.recipeManager.getRecipesFor(RecipeType.STONECUTTING, fakeContainer, level)
        val recipe = candidates.find { it.resultItem.`is`(targetItem) } ?: return MethodResult.of(
            null,
            "Cannot transform selected item into $target"
        )
        val output = recipe.resultItem.copy()
        output.count = 0
        while (recipe.matches(fakeContainer, level)) {
            fakeContainer.getItem(0).shrink(1)
            output.grow(recipe.resultItem.count)
        }
        val rest =
            InventoryUtil.storeItems(output, ItemStorage.wrap(turtleInventory), peripheralOwner.turtle.selectedSlot)
        if (!rest.isEmpty) {
            val pos = pos.relative(peripheralOwner.facing)
            Containers.dropItemStack(level, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), rest)
        }
        return MethodResult.of(true)
    }

    private fun chiselBlock(target: String, overwrittenDirection: VerticalDirection?): MethodResult {
        val level = level!!
        val targetItem = Registry.ITEM.get(ResourceLocation(target))
        if (targetItem == Items.AIR)
            return MethodResult.of(null, "Cannot find item with id $target")
        val findBlockResult = findBlock(overwrittenDirection)
        if (findBlockResult.rightPresent())
            return findBlockResult.right!!
        val hit = findBlockResult.left!!.left
        val blockState = findBlockResult.left!!.right
        val fakeContainer = FakeItemContainer(blockState.block.asItem().defaultInstance)
        val candidates = level.recipeManager.getRecipesFor(RecipeType.STONECUTTING, fakeContainer, level)
        val recipe = candidates.find { it.resultItem.`is`(targetItem) } ?: return MethodResult.of(
            null,
            "Cannot transform selected item into $target"
        )
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
    fun getAlternatives(arguments: IArguments): MethodResult {
        val mode = TransformInteractionMode.luaValueOf(arguments.getString(0))
        val directionArgument = arguments.optString(1)
        val overwrittenDirection = if (directionArgument.isEmpty) null else VerticalDirection.luaValueOf(
            directionArgument.get()
        )
        val level = level!!
        val fakeContainer: Container? = if (mode == TransformInteractionMode.BLOCK) {
            val blockState = peripheralOwner.withPlayer({
                val hit = it.findHit(skipEntity = true, skipBlock = false)
                if (hit !is BlockHitResult)
                    return@withPlayer null
                return@withPlayer level.getBlockState(hit.blockPos)
            }, overwrittenDirection = overwrittenDirection?.minecraftDirection)
            if (blockState?.isAir == false) FakeItemContainer(blockState.block.asItem().defaultInstance) else null
        } else {
            LimitedInventory(peripheralOwner.turtle.inventory, intArrayOf(peripheralOwner.turtle.selectedSlot))
        }

        if (fakeContainer == null)
            return MethodResult.of(null, "Cannot find target for alternative analysis")

        var candidates = level.recipeManager.getRecipesFor(RecipeType.STONECUTTING, fakeContainer, level)
        if (mode == TransformInteractionMode.BLOCK)
            candidates = candidates.filter { it.resultItem.item is BlockItem && it.resultItem.count == 1 }
        return MethodResult.of(candidates.map { Registry.ITEM.getKey(it.resultItem.item).toString() })
    }

    @LuaFunction(mainThread = true)
    fun chisel(arguments: IArguments): MethodResult {
        val mode = TransformInteractionMode.luaValueOf(arguments.getString(0))
        val target = arguments.getString(1)
        val directionArgument = arguments.optString(2)
        val overwrittenDirection = if (directionArgument.isEmpty) null else VerticalDirection.luaValueOf(
            directionArgument.get()
        )
        return when (mode) {
            TransformInteractionMode.BLOCK -> withOperation(
                CountOperation.CHISEL,
                1,
                { chiselBlock(target, overwrittenDirection) })
            TransformInteractionMode.INVENTORY -> withOperation(
                CountOperation.CHISEL,
                peripheralOwner.toolInMainHand.count,
                { chiselItem(target) })
        }
    }

    @LuaFunction(mainThread = true)
    fun rotate(arguments: IArguments): MethodResult {
        val rotation: Rotation = LuaInterpretation.asRotation(arguments.getString(0))
        val directionArgument = arguments.optString(1)
        val overwrittenDirection = if (directionArgument.isEmpty) null else VerticalDirection.luaValueOf(
            directionArgument.get()
        )
        val level = level!!
        return withOperation(SingleOperation.ROTATE) {
            val findBlockResult = findBlock(overwrittenDirection)
            if (findBlockResult.rightPresent())
                return@withOperation findBlockResult.right!!
            val hit = findBlockResult.left!!.left
            val blockState = findBlockResult.left!!.right
            level.setBlockAndUpdate(hit.blockPos, blockState.rotate(rotation))
            return@withOperation MethodResult.of(true)
        }
    }

    @LuaFunction(mainThread = true)
    fun turnOver(arguments: IArguments): MethodResult {
        val directionArgument = arguments.optString(0)
        val overwrittenDirection = if (directionArgument.isEmpty) null else VerticalDirection.luaValueOf(
            directionArgument.get()
        )
        val level = level!!
        return withOperation(SingleOperation.ROTATE) {
            val findBlockResult = findBlock(overwrittenDirection)
            if (findBlockResult.rightPresent())
                return@withOperation findBlockResult.right!!
            val hit = findBlockResult.left!!.left
            val blockState = findBlockResult.left!!.right
            val propertyCandidate = blockState.values.keys.stream().filter { it is EnumProperty<*> && it.allValues.anyMatch { pr -> pr.value() is Half } }.findAny()
            if (propertyCandidate.isEmpty)
                return@withOperation MethodResult.of(null, "Cannot turn over block")
            val property = propertyCandidate.get() as EnumProperty<Half>
            val currentOrientation = blockState.getValue(property) as Half
            if (currentOrientation == Half.TOP) {
                level.setBlockAndUpdate(hit.blockPos, blockState.setValue(property, Half.BOTTOM))
            } else {
                level.setBlockAndUpdate(hit.blockPos, blockState.setValue(property, Half.TOP))
            }
            return@withOperation MethodResult.of(true)
        }
    }

    @LuaFunction(mainThread = true)
    fun getPossibleShapes(arguments: IArguments): MethodResult {
        val directionArgument = arguments.optString(0)
        val overwrittenDirection = if (directionArgument.isEmpty) null else VerticalDirection.luaValueOf(
            directionArgument.get()
        )
        val level = level!!
        val findBlockResult = findBlock(overwrittenDirection)
        if (findBlockResult.rightPresent())
            return findBlockResult.right!!
        val blockState = findBlockResult.left!!.right
        val propertyCandidate = blockState.values.keys.stream().filter { it is EnumProperty<*> && it.getName() == "shape" }.findAny()
        if (propertyCandidate.isEmpty)
            return MethodResult.of(null, "This block cannot change it shape")
        val property = propertyCandidate.get() as EnumProperty<*>
        val currentShape = blockState.getValue(property)
        return MethodResult.of(property.possibleValues.filter { it != currentShape }.map { it.name.lowercase() })
    }

    @LuaFunction(mainThread = true)
    fun <V: Comparable<V>> changeShape(arguments: IArguments): MethodResult {
        val targetShape: String = arguments.getString(0)
        val directionArgument = arguments.optString(1)
        val overwrittenDirection = if (directionArgument.isEmpty) null else VerticalDirection.luaValueOf(
            directionArgument.get()
        )
        val level = level!!
        return withOperation(SingleOperation.ROTATE) {
            val findBlockResult = findBlock(overwrittenDirection)
            if (findBlockResult.rightPresent())
                return@withOperation findBlockResult.right!!
            val hit = findBlockResult.left!!.left
            val blockState = findBlockResult.left!!.right
            val propertyCandidate = blockState.values.keys.stream().filter { it is EnumProperty<*> && it.getName() == "shape" }.findAny()
            if (propertyCandidate.isEmpty)
                return@withOperation MethodResult.of(null, "This block cannot change it shape")
            val property = propertyCandidate.get() as EnumProperty<*>
            val newValue = property.possibleValues.find { it.name.lowercase() == targetShape }
                ?: return@withOperation MethodResult.of(null, "This block cannot change shape to $targetShape")
            val fixedProperty = propertyCandidate.get() as Property<V>
            level.setBlockAndUpdate(hit.blockPos, blockState.setValue(fixedProperty, newValue as V))
            return@withOperation MethodResult.of(true)
        }
    }
}