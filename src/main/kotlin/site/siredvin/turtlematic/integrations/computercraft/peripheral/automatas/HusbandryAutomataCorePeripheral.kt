package site.siredvin.turtlematic.integrations.computercraft.peripheral.automatas

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.player.Player
import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.lib.util.LuaConverter
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.integrations.computercraft.plugins.*
import site.siredvin.turtlematic.tags.EntityTags
import java.util.function.Predicate

class HusbandryAutomataCorePeripheral(
    turtle: ITurtleAccess,
    side: TurtleSide,
): BaseAutomataCorePeripheral(
    TYPE, turtle, side, AutomataCoreTier.TIER2
){
    init {
        addPlugin(AutomataLookPlugin(this, entityConverter = LuaConverter::completeEntityToLua))
        addPlugin(
            AutomataInteractionPlugin(
            this, allowedMods = InteractionMode.values().toSet(),
            suitableEntity = isAnimal
        )
        )
        addPlugin(
            AutomataScanPlugin(
            this, suitableEntity = suitableEntity,
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
}