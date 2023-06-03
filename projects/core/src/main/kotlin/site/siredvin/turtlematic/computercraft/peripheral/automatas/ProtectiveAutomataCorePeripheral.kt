package site.siredvin.turtlematic.computercraft.peripheral.automatas

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import site.siredvin.peripheralium.api.datatypes.InteractionMode
import site.siredvin.peripheralium.computercraft.peripheral.ability.PeripheralOwnerAbility
import site.siredvin.peripheralium.computercraft.peripheral.ability.ScanningAbility
import site.siredvin.turtlematic.api.AutomataCoreTraits
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.operations.SphereOperation
import site.siredvin.turtlematic.computercraft.plugins.*
import site.siredvin.turtlematic.tags.EntityTags
import java.util.function.Predicate

class ProtectiveAutomataCorePeripheral(
    turtle: ITurtleAccess,
    side: TurtleSide,
    tier: IAutomataCoreTier,
) : BaseAutomataCorePeripheral(
    TYPE,
    turtle,
    side,
    tier,
) {
    init {
        addPlugin(
            // TODO: consider adding exposing explosion protection, if this is not done yet
            AutomataLookPlugin(this),
        )
        addPlugin(
            AutomataInteractionPlugin(
                this,
                allowedMods = InteractionMode.values().toSet(),
                suitableEntity = suitableEntity,
            ),
        )
        peripheralOwner.attachAbility(
            PeripheralOwnerAbility.SCANNING,
            ScanningAbility(peripheralOwner, tier.interactionRadius).attachItemScan(
                SphereOperation.SCAN_ITEMS,
            ).attachLivingEntityScan(SphereOperation.SCAN_ENTITIES, { suitableEntity.test(it) }),
        )
        addPlugin(AutomataCapturePlugin(this, allowedMods = setOf(InteractionMode.ENTITY), suitableEntity))
        if (tier.traits.contains(AutomataCoreTraits.APPRENTICE)) {
            addPlugin(AutomataAIPlugin(this, suitableEntity.and { !it.type.`is`(EntityTags.AI_CONTROL_BLACKLIST) }))
        }
    }

    companion object : PeripheralConfiguration {
        override val TYPE = "protectiveAutomata"
        private val isLivingEntity =
            Predicate { entity1: Entity? -> entity1 is LivingEntity }
        private val isNotPlayer =
            Predicate { entity1: Entity? -> entity1 !is Player }
        private val suitableEntity = HusbandryAutomataCorePeripheral.isAnimal.negate().and(isLivingEntity).and(isNotPlayer)
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableProtectiveAutomataCore
}
