package site.siredvin.turtlematic.integrations.computercraft.peripheral

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import site.siredvin.apcode.plugins.*
import site.siredvin.lib.operations.AutomataCoreTier
import site.siredvin.lib.peripherals.BaseAutomataCorePeripheral
import site.siredvin.lib.util.LuaConverter
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import java.util.function.Predicate

class HusbandryAutomataCorePeripheral(
    turtle: ITurtleAccess,
    side: TurtleSide,
): BaseAutomataCorePeripheral(
    TYPE, turtle, side, AutomataCoreTier.TIER2
){
    init {
        addPlugin(AutomataLookPlugin(this, entityConverter = LuaConverter::completeEntityToLua))
        addPlugin(AutomataHandPlugin(
            this, allowedMods = InteractionMode.values().toSet(),
            suitableEntity = isAnimal
        ))
        addPlugin(AutomataScanPlugin(
            this, suitableEntity = suitableEntity,
            allowedMods = setOf(AreaInteractionMode.ITEM, AreaInteractionMode.ENTITY))
        )
        addPlugin(AutomataEntityTransferPlugin(this, suitableEntity))
    }

    companion object {
        const val TYPE = "husbandryAutomata"
        private val isAnimal =
            Predicate { entity1: Entity ->
                entity1.type.category.isFriendly
            }
        private val isLivingEntity =
            Predicate { entity1: Entity? -> entity1 is LivingEntity }
        private val isNotPlayer =
            Predicate { entity1: Entity? -> entity1 !is Player }
        private val suitableEntity = isAnimal.and(isLivingEntity).and(isNotPlayer)
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableHusbandryAutomataCore
}