package site.siredvin.turtlematic.computercraft.peripheral.forged

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.block.state.properties.Half
import net.minecraft.world.level.block.state.properties.Property
import net.minecraft.world.phys.BlockHitResult
import site.siredvin.peripheralium.api.datatypes.TransformInteractionMode
import site.siredvin.peripheralium.api.datatypes.VerticalDirection
import site.siredvin.peripheralium.api.peripheral.IPeripheralOperation
import site.siredvin.peripheralium.storages.ContainerUtils
import site.siredvin.peripheralium.storages.FakeItemContainer
import site.siredvin.peripheralium.storages.LimitedInventory
import site.siredvin.peripheralium.util.Pair
import site.siredvin.peripheralium.util.representation.LuaInterpretation
import site.siredvin.peripheralium.util.representation.stateProperties
import site.siredvin.peripheralium.xplat.PeripheraliumPlatform
import site.siredvin.peripheralium.xplat.XplatRegistries
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.operations.CountOperation
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.computercraft.plugins.AutomataLookPlugin
import java.util.function.Predicate

class MasonAutomataCorePeripheral(turtle: ITurtleAccess, side: TurtleSide, tier: IAutomataCoreTier) :
    ExperienceAutomataCorePeripheral(TYPE, turtle, side, tier) {

    interface MasonRecipeHandler {
        fun getAlternatives(level: Level, fakeContainer: Container): List<ItemStack>
        fun getRecipe(level: Level, fakeContainer: Container, targetItem: Item): Recipe<Container>?
        fun produce(level: Level, fakeContainer: Container, targetItem: Item, recipe: Recipe<Container>, limit: Int): ItemStack

        val handlerID: String
        val workWith: List<Class<*>>
    }

    interface MasonShapeChangeStrategy {
        fun changeShape(level: Level, pos: BlockPos, oldState: BlockState, newState: BlockState): MethodResult
    }

    class StonecutterRecipeHandler : MasonRecipeHandler {
        override fun getAlternatives(level: Level, fakeContainer: Container): List<ItemStack> {
            return level.recipeManager.getRecipesFor(RecipeType.STONECUTTING, fakeContainer, level).map { it.getResultItem(RegistryAccess.EMPTY) }
        }

        override fun getRecipe(level: Level, fakeContainer: Container, targetItem: Item): Recipe<Container>? {
            return level.recipeManager.getRecipesFor(RecipeType.STONECUTTING, fakeContainer, level).find {
                it.getResultItem(
                    RegistryAccess.EMPTY,
                ).`is`(targetItem)
            }
        }

        override fun produce(
            level: Level,
            fakeContainer: Container,
            targetItem: Item,
            recipe: Recipe<Container>,
            limit: Int,
        ): ItemStack {
            if (recipe !is StonecutterRecipe) {
                return ItemStack.EMPTY
            }
            var consumedAmount = 0
            val output = recipe.getResultItem(RegistryAccess.EMPTY).copy()
            output.count = 0
            while (recipe.matches(fakeContainer, level) && limit > consumedAmount) {
                fakeContainer.getItem(0).shrink(1)
                consumedAmount++
                output.grow(recipe.getResultItem(RegistryAccess.EMPTY).count)
            }
            return output
        }

        override val handlerID: String
            get() = "stonecutter"

        override val workWith: List<Class<*>>
            get() = listOf(StonecutterRecipe::class.java)
    }

    companion object : PeripheralConfiguration {
        override val TYPE = "masonAutomata"

        private val HANDLERS = mutableMapOf<String, MasonRecipeHandler>()
        private val RECIPE_TO_ID = mutableMapOf<Class<*>, String>()
        private val SHAPE_STRATEGY = mutableListOf<kotlin.Pair<Predicate<Block>, MasonShapeChangeStrategy>>()

        fun addRecipeHandler(handler: MasonRecipeHandler) {
            HANDLERS[handler.handlerID] = handler
            handler.workWith.forEach { RECIPE_TO_ID[it] = handler.handlerID }
        }

        fun addShapeStrategy(predicate: Predicate<Block>, handler: MasonShapeChangeStrategy) {
            SHAPE_STRATEGY.add(kotlin.Pair(predicate, handler))
        }

        init {
            addRecipeHandler(StonecutterRecipeHandler())
        }

        fun getAlternatives(level: Level, fakeContainer: Container): List<ItemStack> {
            val alternatives = mutableListOf<ItemStack>()
            HANDLERS.values.forEach { alternatives.addAll(it.getAlternatives(level, fakeContainer)) }
            return alternatives
        }

        fun getRecipe(level: Level, fakeContainer: Container, targetItem: Item): Recipe<Container>? {
            HANDLERS.values.forEach {
                val recipe = it.getRecipe(level, fakeContainer, targetItem)
                if (recipe != null) {
                    return recipe
                }
            }
            return null
        }

        fun changeShape(level: Level, pos: BlockPos, oldState: BlockState, newState: BlockState): MethodResult {
            SHAPE_STRATEGY.forEach {
                if (it.first.test(oldState.block)) {
                    return it.second.changeShape(level, pos, oldState, newState)
                }
            }
            level.setBlockAndUpdate(pos, newState)
            return MethodResult.of(true)
        }

        fun produce(level: Level, fakeContainer: Container, targetItem: Item, recipe: Recipe<Container>, limit: Int): ItemStack {
            val handlerID = RECIPE_TO_ID[recipe::class.java] ?: return ItemStack.EMPTY
            val handler = HANDLERS[handlerID] ?: return ItemStack.EMPTY
            return handler.produce(level, fakeContainer, targetItem, recipe, limit)
        }
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableMasonAutomataCore

    init {
        addPlugin(AutomataLookPlugin(this, blockStateEnriches = listOf(stateProperties)))
    }

    override fun possibleOperations(): MutableList<IPeripheralOperation<*>> {
        val base = super.possibleOperations()
        base.add(CountOperation.CHISEL)
        base.add(SingleOperation.TRANSFORM_BLOCK)
        return base
    }

    private fun isEditable(pos: BlockPos): Boolean {
        return peripheralOwner.withPlayer({
            PeripheraliumPlatform.isBlockProtected(pos, it.fakePlayer.level().getBlockState(pos), it.fakePlayer)
        })
    }

    private fun findBlock(overwrittenDirection: VerticalDirection?): Pair<Pair<BlockHitResult, BlockState>?, MethodResult?> {
        val hit = peripheralOwner.withPlayer({
            val hit = it.findHit(skipEntity = true, skipBlock = false)
            if (hit !is BlockHitResult) {
                return@withPlayer null
            }
            return@withPlayer hit
        }, overwrittenDirection = overwrittenDirection?.minecraftDirection) ?: return Pair.onlyRight(MethodResult.of(null, "There is nothing to work with"))
        val blockState = level!!.getBlockState(hit.blockPos)
        if (blockState.isAir) {
            return Pair.onlyRight(MethodResult.of(null, "There is nothing to work with"))
        }
        if (!isEditable(hit.blockPos)) {
            return Pair.onlyRight(MethodResult.of(null, "This block is protected"))
        }
        return Pair.onlyLeft(Pair(hit, blockState))
    }

    private fun chiselItem(target: String, arguments: IArguments): MethodResult {
        val level = level!!
        val limit = arguments.optInt(2, Int.MAX_VALUE)
        val targetItem = XplatRegistries.ITEMS.get(ResourceLocation(target))
        if (targetItem == Items.AIR) {
            return MethodResult.of(null, "Cannot find item with id $target")
        }
        val turtleInventory = peripheralOwner.turtle.inventory
        val fakeContainer = LimitedInventory(turtleInventory, intArrayOf(peripheralOwner.turtle.selectedSlot))
        val recipe = getRecipe(level, fakeContainer, targetItem) ?: return MethodResult.of(
            null,
            "Cannot transform selected item into $target",
        )
        val output = produce(level, fakeContainer, targetItem, recipe, limit)
        if (output.isEmpty) {
            return MethodResult.of(null, "Strange internal error appear, cannot find useful recipe")
        }
        ContainerUtils.toInventoryOrToWorld(
            output,
            turtleInventory,
            peripheralOwner.turtle.selectedSlot,
            pos.relative(peripheralOwner.facing),
            level,
        )
        return MethodResult.of(true)
    }

    private fun chiselBlock(target: String, arguments: IArguments): MethodResult {
        val directionArgument = arguments.optString(2)
        val overwrittenDirection = if (directionArgument.isEmpty) {
            null
        } else {
            VerticalDirection.luaValueOf(
                directionArgument.get(),
            )
        }
        val level = level!!
        val targetItem = XplatRegistries.ITEMS.get(ResourceLocation(target))
        if (targetItem == Items.AIR) {
            return MethodResult.of(null, "Cannot find item with id $target")
        }
        val findBlockResult = findBlock(overwrittenDirection)
        if (findBlockResult.rightPresent()) {
            return findBlockResult.right!!
        }
        val hit = findBlockResult.left!!.left
        val blockState = findBlockResult.left!!.right
        val fakeContainer = FakeItemContainer(blockState.block.asItem().defaultInstance)
        val recipe = getRecipe(level, fakeContainer, targetItem) ?: return MethodResult.of(
            null,
            "Cannot transform selected item into $target",
        )
        val output = produce(level, fakeContainer, targetItem, recipe, 1)
        if (output.isEmpty) {
            return MethodResult.of(null, "Strange internal error appear, cannot find useful recipe")
        }
        if (output.item is BlockItem && output.count == 1) {
            val targetBlockState = (output.item as BlockItem).block.defaultBlockState()
            level.setBlockAndUpdate(hit.blockPos, targetBlockState)
        } else {
            level.setBlockAndUpdate(hit.blockPos, Blocks.AIR.defaultBlockState())
            ContainerUtils.toInventoryOrToWorld(
                output.copy(),
                peripheralOwner.turtle.inventory,
                peripheralOwner.turtle.selectedSlot,
                peripheralOwner.pos.relative(peripheralOwner.facing),
                level,
            )
        }
        return MethodResult.of(true)
    }

    @LuaFunction(mainThread = true)
    fun getAlternatives(arguments: IArguments): MethodResult {
        val mode = TransformInteractionMode.luaValueOf(arguments.getString(0))
        val directionArgument = arguments.optString(1)
        val overwrittenDirection = if (directionArgument.isEmpty) {
            null
        } else {
            VerticalDirection.luaValueOf(
                directionArgument.get(),
            )
        }
        val level = level!!
        val fakeContainer: Container? = if (mode == TransformInteractionMode.BLOCK) {
            val blockState = peripheralOwner.withPlayer({
                val hit = it.findHit(skipEntity = true, skipBlock = false)
                if (hit !is BlockHitResult) {
                    return@withPlayer null
                }
                return@withPlayer level.getBlockState(hit.blockPos)
            }, overwrittenDirection = overwrittenDirection?.minecraftDirection)
            if (blockState?.isAir == false) FakeItemContainer(blockState.block.asItem().defaultInstance) else null
        } else {
            LimitedInventory(peripheralOwner.turtle.inventory, intArrayOf(peripheralOwner.turtle.selectedSlot))
        }

        if (fakeContainer == null) {
            return MethodResult.of(null, "Cannot find target for alternative analysis")
        }

        val alternatives = getAlternatives(level, fakeContainer)
        return MethodResult.of(alternatives.map { XplatRegistries.ITEMS.getKey(it.item).toString() })
    }

    @LuaFunction(mainThread = true)
    fun chisel(arguments: IArguments): MethodResult {
        val mode = TransformInteractionMode.luaValueOf(arguments.getString(0))
        val target = arguments.getString(1)
        return when (mode) {
            TransformInteractionMode.BLOCK -> withOperation(
                CountOperation.CHISEL,
                1,
                { chiselBlock(target, arguments) },
            )
            TransformInteractionMode.INVENTORY -> withOperation(
                CountOperation.CHISEL,
                peripheralOwner.toolInMainHand.count,
                { chiselItem(target, arguments) },
            )
        }
    }

    @LuaFunction(mainThread = true)
    fun rotate(arguments: IArguments): MethodResult {
        val rotation: Rotation = LuaInterpretation.asRotation(arguments.getString(0))
        val directionArgument = arguments.optString(1)
        val overwrittenDirection = if (directionArgument.isEmpty) {
            null
        } else {
            VerticalDirection.luaValueOf(
                directionArgument.get(),
            )
        }
        val level = level!!
        return withOperation(SingleOperation.TRANSFORM_BLOCK) {
            val findBlockResult = findBlock(overwrittenDirection)
            if (findBlockResult.rightPresent()) {
                return@withOperation findBlockResult.right!!
            }
            val hit = findBlockResult.left!!.left
            val blockState = findBlockResult.left!!.right
            @Suppress("DEPRECATION", "KotlinRedundantDiagnosticSuppress")
            level.setBlockAndUpdate(hit.blockPos, blockState.rotate(rotation))
            return@withOperation MethodResult.of(true)
        }
    }

    @LuaFunction(mainThread = true)
    fun turnOver(arguments: IArguments): MethodResult {
        val directionArgument = arguments.optString(0)
        val overwrittenDirection = if (directionArgument.isEmpty) {
            null
        } else {
            VerticalDirection.luaValueOf(
                directionArgument.get(),
            )
        }
        val level = level!!
        return withOperation(SingleOperation.TRANSFORM_BLOCK) {
            val findBlockResult = findBlock(overwrittenDirection)
            if (findBlockResult.rightPresent()) {
                return@withOperation findBlockResult.right!!
            }
            val hit = findBlockResult.left!!.left
            val blockState = findBlockResult.left!!.right
            val propertyCandidate = blockState.values.keys.stream().filter { it is EnumProperty<*> && it.allValues.anyMatch { pr -> pr.value() is Half } }.findAny()
            if (propertyCandidate.isEmpty) {
                return@withOperation MethodResult.of(null, "Cannot turn over block")
            }
            @Suppress("UNCHECKED_CAST") val property = propertyCandidate.get() as EnumProperty<Half>
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
        val overwrittenDirection = if (directionArgument.isEmpty) {
            null
        } else {
            VerticalDirection.luaValueOf(
                directionArgument.get(),
            )
        }
        val findBlockResult = findBlock(overwrittenDirection)
        if (findBlockResult.rightPresent()) {
            return findBlockResult.right!!
        }
        val blockState = findBlockResult.left!!.right
        val propertyCandidate = blockState.values.keys.stream().filter { it is EnumProperty<*> && it.getName() == "shape" }.findAny()
        if (propertyCandidate.isEmpty) {
            return MethodResult.of(null, "This block cannot change it shape")
        }
        val property = propertyCandidate.get() as EnumProperty<*>
        val currentShape = blockState.getValue(property)
        return MethodResult.of(property.possibleValues.filter { it != currentShape }.map { it.name.lowercase() })
    }

    @LuaFunction(mainThread = true, value = ["changeShape"])
    fun <V : Comparable<V>> changeShapeLua(arguments: IArguments): MethodResult {
        val targetShape: String = arguments.getString(0)
        val directionArgument = arguments.optString(1)
        val overwrittenDirection = if (directionArgument.isEmpty) {
            null
        } else {
            VerticalDirection.luaValueOf(
                directionArgument.get(),
            )
        }
        val level = level!!
        return withOperation(SingleOperation.TRANSFORM_BLOCK) {
            val findBlockResult = findBlock(overwrittenDirection)
            if (findBlockResult.rightPresent()) {
                return@withOperation findBlockResult.right!!
            }
            val hit = findBlockResult.left!!.left
            val blockState = findBlockResult.left!!.right
            val propertyCandidate = blockState.values.keys.stream().filter { it is EnumProperty<*> && it.getName() == "shape" }.findAny()
            if (propertyCandidate.isEmpty) {
                return@withOperation MethodResult.of(null, "This block cannot change it shape")
            }
            val property = propertyCandidate.get() as EnumProperty<*>
            val newValue = property.possibleValues.find { it.name.lowercase() == targetShape }
                ?: return@withOperation MethodResult.of(null, "This block cannot change shape to $targetShape")
            @Suppress("UNCHECKED_CAST") val fixedProperty = propertyCandidate.get() as Property<V>
            @Suppress("UNCHECKED_CAST")
            return@withOperation changeShape(level, hit.blockPos, blockState, blockState.setValue(fixedProperty, newValue as V))
        }
    }
}
