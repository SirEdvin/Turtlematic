package site.siredvin.turtlematic.computercraft.peripheral.forged

import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.npc.Villager
import net.minecraft.world.entity.npc.WanderingTrader
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.trading.Merchant
import site.siredvin.peripheralium.computercraft.peripheral.ability.PeripheralOwnerAbility
import site.siredvin.peripheralium.computercraft.peripheral.ability.ScanningAbility
import site.siredvin.peripheralium.util.representation.merchantData
import site.siredvin.peripheralium.util.representation.villagerData
import site.siredvin.turtlematic.api.AutomataCoreTraits
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.operations.SphereOperation
import site.siredvin.turtlematic.computercraft.plugins.AutomataLookPlugin
import site.siredvin.turtlematic.computercraft.plugins.AutomataRestockPlugin
import site.siredvin.turtlematic.computercraft.plugins.AutomataTradePlugin
import java.util.function.Predicate

class MercantileAutomataCorePeripheral(
    turtle: ITurtleAccess,
    side: TurtleSide,
    tier: IAutomataCoreTier,
) : ExperienceAutomataCorePeripheral(
    TYPE,
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
        peripheralOwner.attachAbility(
            PeripheralOwnerAbility.SCANNING,
            ScanningAbility(
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
                    if (!merchant.canRestock()) {
                        return@AutomataRestockPlugin MethodResult.of(null, "Merchant cannot provide restock for now")
                    }
                    if (merchant is Villager) {
                        merchant.restock()
                        return@AutomataRestockPlugin MethodResult.of(true)
                    }
                    if (merchant is WanderingTrader) {
                        merchant.canRestock()
                    }
                    return@AutomataRestockPlugin MethodResult.of(null, "Current merchant cannot be restock at all")
                }, suitableEntity),
            )
        }
    }

    companion object : PeripheralConfiguration {
        override val TYPE = "mercantileAutomata"

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
