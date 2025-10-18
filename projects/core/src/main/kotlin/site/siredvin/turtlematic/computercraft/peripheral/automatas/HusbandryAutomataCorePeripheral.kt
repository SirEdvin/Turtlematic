package site.siredvin.turtlematic.computercraft.peripheral.automatas

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.SaplingBlock
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.phys.BlockHitResult
import site.siredvin.broccolium.modules.storage.item.ContainerUtils
import site.siredvin.turtlematic.api.AutomataCoreTraits
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.computercraft.operations.SphereOperation
import site.siredvin.turtlematic.computercraft.plugins.*
import site.siredvin.turtlematic.tags.BlockTags
import site.siredvin.turtlematic.tags.EntityTags
import site.siredvin.tweakium.modules.peripheral.api.IPeripheralOperation
import site.siredvin.tweakium.modules.peripheral.api.InteractionMode
import site.siredvin.tweakium.modules.peripheral.api.VerticalDirection
import site.siredvin.tweakium.modules.peripheral.boon.PeripheralOwnerBoonKey
import site.siredvin.tweakium.modules.peripheral.boon.ScanningBoon
import site.siredvin.tweakium.modules.peripheral.representation.animalData
import site.siredvin.tweakium.modules.peripheral.representation.beeNestAnalyze
import site.siredvin.tweakium.modules.peripheral.representation.cropAge
import site.siredvin.tweakium.modules.peripheral.representation.honeyLevel
import java.util.function.Predicate
import kotlin.jvm.optionals.getOrNull

class HusbandryAutomataCorePeripheral(
    turtle: ITurtleAccess,
    side: TurtleSide,
    tier: IAutomataCoreTier,
) : BaseAutomataCorePeripheral(
    type,
    turtle,
    side,
    tier,
) {
    init {
        addPlugin(
            AutomataLookPlugin(
                this,
                entityEnriches = listOf(animalData),
                blockStateEnriches = listOf(cropAge, honeyLevel),
                blockEntityEnriches = listOf(beeNestAnalyze),
            ),
        )
        addPlugin(
            AutomataInteractionPlugin(
                this,
                allowedMods = InteractionMode.values().toSet(),
                suitableEntity = suitableEntity,
            ),
        )
        addPlugin(AutomataItemSuckPlugin(this))
        peripheralOwner.attachBoon(
            PeripheralOwnerBoonKey.SCANNING,
            ScanningBoon(peripheralOwner, tier.interactionRadius).attachItemScan(
                SphereOperation.SCAN_ITEMS,
            ).attachLivingEntityScan(SphereOperation.SCAN_ENTITIES, { suitableEntity.test(it) }, { it1, it2 -> animalData.accept(it1, it2) }),
        )
        addPlugin(AutomataCapturePlugin(this, allowedMods = setOf(InteractionMode.ENTITY), suitableEntity))
        if (tier.traits.contains(AutomataCoreTraits.APPRENTICE)) {
            addPlugin(AutomataAIPlugin(this, suitableEntity.and { !it.type.`is`(EntityTags.AI_CONTROL_BLOCKLIST) }))
        }
    }

    companion object : PeripheralConfiguration {
        override val type = "husbandryAutomata"
        val isAnimal =
            Predicate { entity1: Entity ->
                entity1.type.category.isFriendly || entity1.type.category == MobCategory.CREATURE || entity1.type.`is`(EntityTags.ANIMAL)
            }
        private val isLivingEntity =
            Predicate { entity1: Entity? -> entity1 is LivingEntity }
        private val isNotPlayer =
            Predicate { entity1: Entity? -> entity1 !is Player }
        private val suitableEntity = isAnimal.and(isLivingEntity).and(isNotPlayer)
        private const val HUSBANDRY_POINTS = "husbandryPoints"
        private const val BASE_MAX_HUSBANDRY_POINTS = 10000
        private const val HUSBANDRY_SIMULATE_COST = 100
        private const val MAGIC_AMOUNT_OF_TREE_LOG_OUTPUT = 12
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableHusbandryAutomataCore

    val maxHusbandryPoints: Int
        get() = BASE_MAX_HUSBANDRY_POINTS * tier.interactionRadius / 2

    override fun possibleOperations(): MutableList<IPeripheralOperation<*>> {
        val base = super.possibleOperations()
        base.add(SingleOperation.HARVEST)
        return base
    }

    override val peripheralConfiguration: MutableMap<String, Any>
        get() {
            val base = super.peripheralConfiguration
            base["maxHusbandryPoints"] = maxHusbandryPoints
            base["simulationGrowCost"] = HUSBANDRY_SIMULATE_COST
            return base
        }

    @LuaFunction(mainThread = true)
    fun getHusbandryPoints(): Int = peripheralOwner.dataStorage.getInt(HUSBANDRY_POINTS)

    @LuaFunction(mainThread = true)
    fun simulateGrow(): MethodResult {
        val selectedItem = peripheralOwner.toolInMainHand
        val points = peripheralOwner.dataStorage.getInt(HUSBANDRY_POINTS)
        if (points <= HUSBANDRY_SIMULATE_COST) {
            return MethodResult.of(false, "Not enough points")
        }
        if (selectedItem.item is BlockItem) {
            val block = (selectedItem.item as BlockItem).block
            if (block is SaplingBlock) {
                val treeFeatureKey = block.treeGrower.getConfiguredFeature(peripheralOwner.level!!.random, false) ?: return MethodResult.of(false, "Item is incorrect for simulation grow")
                val treeFeature = peripheralOwner.level!!.registryAccess().registryOrThrow(
                    Registries.CONFIGURED_FEATURE,
                ).getHolder(treeFeatureKey).getOrNull()?.value() ?: return MethodResult.of(false, "Item is incorrect for simulation grow")
                val configuration = treeFeature.config as? TreeConfiguration ?: return MethodResult.of(false, "Item is incorrect for simulation grow")
                val truckBlock = configuration.trunkProvider.getState(peripheralOwner.level!!.random, BlockPos(0, 0, 0))
                ContainerUtils.toInventoryOrToWorld(
                    truckBlock.block.asItem().defaultInstance.copyWithCount(MAGIC_AMOUNT_OF_TREE_LOG_OUTPUT),
                    peripheralOwner.turtle.inventory,
                    peripheralOwner.turtle.selectedSlot,
                    peripheralOwner.pos.above(),
                    peripheralOwner.level!!,
                )
                peripheralOwner.dataStorage.putInt(HUSBANDRY_POINTS, (peripheralOwner.dataStorage.getInt(HUSBANDRY_POINTS) - HUSBANDRY_SIMULATE_COST).coerceAtLeast(0))
                return MethodResult.of(true)
            }
        }
        return MethodResult.of(false, "Item is incorrect for simulation grow")
    }

    @LuaFunction(mainThread = true)
    fun harvest(arguments: IArguments): MethodResult {
        val directionArgument = arguments.optString(0)
        val overwrittenDirection = if (directionArgument.isEmpty) {
            null
        } else {
            VerticalDirection.luaValueOf(
                directionArgument.get(),
            )
        }
        val result = peripheralOwner.withPlayer({
            it.findHit(skipEntity = true, skipBlock = false)
        }, overwrittenDirection = overwrittenDirection?.minecraftDirection)
        if (result !is BlockHitResult) {
            return MethodResult.of(null, "Nothing to harvest from")
        }
        val level = peripheralOwner.level!!
        val blockState = level.getBlockState(result.blockPos)
        val ageProperty = blockState.properties.find { it.name == "age" } as IntegerProperty?
            ?: return MethodResult.of(null, "This block is not harvestable")
        if (blockState.block !is CropBlock && !blockState.`is`(BlockTags.HUSBANDRY_EXTRA_CROPS)) {
            return MethodResult.of(null, "This block is not harvestable")
        }
        val maxAge = ageProperty.possibleValues.maxOf { it }
        val currentAge = blockState.getValue(ageProperty)
        if (currentAge != maxAge) {
            return MethodResult.of(null, "Too early for harvesting")
        }
        return withOperation(SingleOperation.HARVEST) {
            val stacks = peripheralOwner.withPlayer({
                Block.getDrops(blockState, level as ServerLevel, result.blockPos, null, it.fakePlayer, it.fakePlayer.mainHandItem)
            }, overwrittenDirection = overwrittenDirection?.minecraftDirection)
            val targetItemToReduce = blockState.block.asItem()
            level.setBlockAndUpdate(result.blockPos, blockState.setValue(ageProperty, 0))
            stacks.forEach {
                if (it.`is`(targetItemToReduce)) {
                    it.shrink(1)
                }
                peripheralOwner.dataStorage.putInt(HUSBANDRY_POINTS, (peripheralOwner.dataStorage.getInt(HUSBANDRY_POINTS) + it.count * tier.interactionRadius / 2).coerceAtMost(maxHusbandryPoints))
                ContainerUtils.toInventoryOrToWorld(
                    it,
                    peripheralOwner.turtle.inventory,
                    peripheralOwner.turtle.selectedSlot,
                    peripheralOwner.pos.above(),
                    level,
                )
            }
            return@withOperation MethodResult.of(true)
        }
    }
}
