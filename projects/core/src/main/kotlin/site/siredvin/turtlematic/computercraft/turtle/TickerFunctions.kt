package site.siredvin.turtlematic.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.IntegerProperty
import site.siredvin.peripheralium.util.world.ScanUtils
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.tags.BlockTags

object TickerFunctions {

    fun isSuitableCrop(state: BlockState): Boolean {
        if (state.block is CropBlock || state.`is`(BlockTags.HUSBANDRY_EXTRA_CROPS)) {
            val ageProperty = state.properties.find { it.name == "age" } as IntegerProperty
            return state.getValue(ageProperty) != ageProperty.possibleValues.maxOf { it }
        }
        return false
    }

    fun causeRandomTickForCrop(turtle: ITurtleAccess, coreTier: IAutomataCoreTier) {
        val randomTickCandidates = mutableListOf<Pair<BlockState, BlockPos>>()
        ScanUtils.traverseBlocks(turtle.level, turtle.position, coreTier.interactionRadius, { state, pos ->
            if (isSuitableCrop(state)) {
                randomTickCandidates.add(Pair(state, pos))
            }
        })
        if (randomTickCandidates.isNotEmpty()) {
            val selected = randomTickCandidates.random()
            selected.first.randomTick(turtle.level as ServerLevel, selected.second, turtle.level.random)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun netheriteHusbandryTick(turtle: ITurtleAccess, side: TurtleSide, coreTier: IAutomataCoreTier, tickCounter: Long) {
        if (TurtlematicConfig.husbandryAutomataRandomTicksEnabled && tickCounter % TurtlematicConfig.netheriteHusbandryAutomataGrownPeriod == 0L) {
            causeRandomTickForCrop(turtle, coreTier)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun starboundHusbandryTick(turtle: ITurtleAccess, side: TurtleSide, coreTier: IAutomataCoreTier, tickCounter: Long) {
        if (TurtlematicConfig.husbandryAutomataRandomTicksEnabled && tickCounter % TurtlematicConfig.starboundeHusbandryAutomataGrownPeriod == 0L) {
            causeRandomTickForCrop(turtle, coreTier)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun creativeHusbandryTick(turtle: ITurtleAccess, side: TurtleSide, coreTier: IAutomataCoreTier, tickCounter: Long) {
        if (TurtlematicConfig.husbandryAutomataRandomTicksEnabled && tickCounter % TurtlematicConfig.creativeHusbandryAutomataGrownPeriod == 0L) {
            ScanUtils.traverseBlocks(turtle.level, turtle.position, coreTier.interactionRadius, { state, pos ->
                if (isSuitableCrop(state)) {
                    state.randomTick(turtle.level as ServerLevel, pos, turtle.level.random)
                }
            })
        }
    }
}
