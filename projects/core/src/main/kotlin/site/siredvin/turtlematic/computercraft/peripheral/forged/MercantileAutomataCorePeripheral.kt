package site.siredvin.turtlematic.computercraft.peripheral.forged

import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.trading.Merchant
import site.siredvin.turtlematic.api.AutomataCoreTraits
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.operations.SphereOperation
import site.siredvin.turtlematic.computercraft.plugins.AutomataLookPlugin
import site.siredvin.turtlematic.computercraft.plugins.AutomataRestockPlugin
import site.siredvin.turtlematic.computercraft.plugins.AutomataTradePlugin
import site.siredvin.tweakium.modules.peripheral.boon.PeripheralOwnerBoonKey
import site.siredvin.tweakium.modules.peripheral.boon.ScanningBoon
import site.siredvin.tweakium.modules.peripheral.representation.merchantData
import site.siredvin.tweakium.modules.peripheral.representation.villagerData
import java.util.function.Predicate

class MercantileAutomataCorePeripheral(
    turtle: ITurtleAccess,
    side: TurtleSide,
    tier: IAutomataCoreTier,
) : ExperienceAutomataCorePeripheral(
    type,
    turtle,
    side,
    tier,
) {
    init {
        addPlugin(
            AutomataLookPlugin(
                this,
                entityEnriches = listOf(merchantData, villagerData),
            ),
        )
        peripheralOwner.attachBoon(
            PeripheralOwnerBoonKey.SCANNING,
            ScanningBoon(
                peripheralOwner,
                tier.interactionRadius,
            ).attachItemScan(
                SphereOperation.SCAN_ITEMS,
            ).attachLivingEntityScan(
                SphereOperation.SCAN_ENTITIES,
                { suitableEntity.test(it) },
                { it1, it2 -> merchantData.accept(it1, it2) },
                { it1, it2 -> villagerData.accept(it1, it2) },
            ),
        )
        addPlugin(AutomataTradePlugin(this, suitableEntity))
        if (tier.traits.contains(AutomataCoreTraits.SKILLED)) {
            addPlugin(
                AutomataRestockPlugin(this, {
                    if (it !is Merchant) {
                        return@AutomataRestockPlugin MethodResult.of(null, "Somehow targeted entity is not merchant")
                    }
                    val merchant: Merchant = it
                    // So, we ignore can Restock, because nobody uses it and it useless
                    // Reset probably too OP, but since villager reset already existed, it probably would not do too much harm
                    merchant.offers.forEach { it1 -> it1.resetUses() }
                    return@AutomataRestockPlugin MethodResult.of(true)
                }, suitableEntity),
            )
        }
    }

    companion object : PeripheralConfiguration {
        override val type = "mercantileAutomata"

        private val isMerchant =
            Predicate { entity1: Entity -> entity1 is Merchant }
        private val isLivingEntity =
            Predicate { entity1: Entity? -> entity1 is LivingEntity }
        private val isNotPlayer =
            Predicate { entity1: Entity? -> entity1 !is Player }
        private val suitableEntity = isMerchant.and(isLivingEntity).and(isNotPlayer)
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableMercantileAutomataCore
}
