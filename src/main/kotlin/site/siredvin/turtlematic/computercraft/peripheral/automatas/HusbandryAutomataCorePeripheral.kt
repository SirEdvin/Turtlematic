package site.siredvin.turtlematic.computercraft.peripheral.automatas

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.NetherWartBlock
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import site.siredvin.lib.api.peripheral.IPeripheralOperation
import site.siredvin.lib.util.InsertionHelpers
import site.siredvin.lib.util.representation.animalData
import site.siredvin.lib.util.representation.beeNestAnalyze
import site.siredvin.lib.util.representation.cropAge
import site.siredvin.lib.util.representation.honeyLevel
import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.datatypes.AreaInteractionMode
import site.siredvin.turtlematic.computercraft.datatypes.InteractionMode
import site.siredvin.turtlematic.computercraft.datatypes.VerticalDirection
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.computercraft.plugins.*
import site.siredvin.turtlematic.tags.BlockTags
import site.siredvin.turtlematic.tags.EntityTags
import java.util.function.Predicate

class HusbandryAutomataCorePeripheral(
    turtle: ITurtleAccess,
    side: TurtleSide,
    tier: IAutomataCoreTier
): BaseAutomataCorePeripheral(
    TYPE, turtle, side, tier
){
    init {
        addPlugin(AutomataLookPlugin(
            this, entityEnriches = listOf(::animalData),
            blockStateEnriches = listOf(::cropAge, ::honeyLevel),
            blockEntityEnriches = listOf(::beeNestAnalyze)
        ))
        addPlugin(
            AutomataInteractionPlugin(
            this, allowedMods = InteractionMode.values().toSet(),
            suitableEntity = isAnimal
        )
        )
        addPlugin(
            AutomataScanPlugin(
            this, suitableEntity = suitableEntity, entityEnriches = listOf(::animalData),
            allowedMods = setOf(AreaInteractionMode.ITEM, AreaInteractionMode.ENTITY))
        )
        addPlugin(AutomataCapturePlugin(this, allowedMods = setOf(InteractionMode.ENTITY), suitableEntity))
    }

    companion object {
        const val TYPE = "husbandryAutomata"
        private val isAnimal =
            Predicate { entity1: Entity ->
                entity1.type.category.isFriendly || entity1.type.category == MobCategory.CREATURE || entity1.type.`is`(EntityTags.ANIMAL)
            }
        private val isLivingEntity =
            Predicate { entity1: Entity? -> entity1 is LivingEntity }
        private val isNotPlayer =
            Predicate { entity1: Entity? -> entity1 !is Player }
        private val suitableEntity = isAnimal.and(isLivingEntity).and(isNotPlayer)
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableHusbandryAutomataCore

    override fun possibleOperations(): MutableList<IPeripheralOperation<*>> {
        val base =  super.possibleOperations()
        base.add(SingleOperation.HARVEST)
        return base
    }

    @LuaFunction(mainThread = true)
    fun harvest(arguments: IArguments): MethodResult {
        val directionArgument = arguments.optString(0)
        val overwrittenDirection = if (directionArgument.isEmpty) null else VerticalDirection.luaValueOf(
            directionArgument.get()
        )
        val result = peripheralOwner.withPlayer({
                APFakePlayer -> APFakePlayer.findHit(skipEntity = true, skipBlock = false)
        }, overwrittenDirection=overwrittenDirection?.minecraftDirection)
        if (result !is BlockHitResult)
            return MethodResult.of(null, "Nothing to harvest from")
        val level = level!!
        val blockState = level.getBlockState(result.blockPos)
        val ageProperty = blockState.properties.find { it.name == "age" } as IntegerProperty?
            ?: return MethodResult.of(null, "This block is not harvestable")
        if (blockState.block !is CropBlock && !blockState.`is`(BlockTags.HUSBANDRY_EXTRA_CROPS))
            return MethodResult.of(null, "This block is not harvestable")
        val maxAge = ageProperty.possibleValues.maxOf { it }
        val currentAge = blockState.getValue(ageProperty)
        if (currentAge != maxAge)
            return MethodResult.of(null, "Too early for harvesting")
        return withOperation(SingleOperation.HARVEST) {
            val stacks = peripheralOwner.withPlayer({
                Block.getDrops(blockState, level as ServerLevel, result.blockPos, null, it, it.mainHandItem)
            }, overwrittenDirection = overwrittenDirection?.minecraftDirection)
            val targetItemToReduce = blockState.block.asItem()
            level.setBlockAndUpdate(result.blockPos, blockState.setValue(ageProperty, 0))
            stacks.forEach {
                if (it.`is`(targetItemToReduce))
                    it.shrink(1)
                InsertionHelpers.toInventoryOrToWorld(
                    it,
                    peripheralOwner.turtle.inventory,
                    peripheralOwner.turtle.selectedSlot,
                    peripheralOwner.pos.above(),
                    level
                )
            }
            return@withOperation MethodResult.of(true)
        }
    }
}