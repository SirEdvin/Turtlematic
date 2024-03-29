package site.siredvin.turtlematic.computercraft.peripheral.forged

import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.Util
import net.minecraft.core.*
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.monster.ZombieVillager
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ThrownPotion
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.Level
import site.siredvin.peripheralium.api.datatypes.InteractionMode
import site.siredvin.peripheralium.api.peripheral.IPeripheralOperation
import site.siredvin.peripheralium.api.peripheral.IPeripheralOwner
import site.siredvin.peripheralium.computercraft.peripheral.ability.PeripheralOwnerAbility
import site.siredvin.peripheralium.computercraft.peripheral.ability.ScanningAbility
import site.siredvin.peripheralium.util.representation.effectsData
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.operations.PowerOperation
import site.siredvin.turtlematic.computercraft.operations.PowerOperationContext
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.computercraft.operations.SphereOperation
import site.siredvin.turtlematic.computercraft.plugins.*
import site.siredvin.turtlematic.util.TurtleDispenseBehavior
import java.util.function.Predicate

class BrewingAutomataCorePeripheral(turtle: ITurtleAccess, side: TurtleSide, tier: IAutomataCoreTier) :
    ExperienceAutomataCorePeripheral(TYPE, turtle, side, tier) {

    companion object : PeripheralConfiguration {
        override val TYPE = "brewingAutomata"
        private val suitableEntity: Predicate<Entity> = Predicate<Entity> { entity: Entity -> entity is ZombieVillager }
    }

    init {
        addPlugin(AutomataLookPlugin(this, entityEnriches = listOf(effectsData)))
        addPlugin(
            AutomataInteractionPlugin(
                this,
                allowedMods = InteractionMode.values().toSet(),
                suitableEntity = suitableEntity,
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
                { it1, it2 -> effectsData.accept(it1, it2) },
            ),
        )
    }

    internal class TurtlePotionDispenseBehavior(owner: IPeripheralOwner) : TurtleDispenseBehavior(owner) {
        override fun getProjectile(level: Level, targetPosition: Position, stack: ItemStack): Projectile {
            stack.shrink(1)
            return Util.make(
                ThrownPotion(level, targetPosition.x(), targetPosition.y(), targetPosition.z()),
            ) { p_218413_1_ -> p_218413_1_.item = stack }
        }
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableBrewingAutomataCore

    private val dispenseBehavior: TurtlePotionDispenseBehavior by lazy {
        TurtlePotionDispenseBehavior(peripheralOwner)
    }

    override fun possibleOperations(): MutableList<IPeripheralOperation<*>> {
        val operations = super.possibleOperations()
        operations.add(SingleOperation.BREW)
        operations.add(PowerOperation.THROW_POTION)
        return operations
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun brew(): MethodResult {
        return withOperation(SingleOperation.BREW) {
            val turtleInventory: Container = peripheralOwner.turtle.inventory
            val selectedSlot: Int = peripheralOwner.turtle.selectedSlot
            val component: ItemStack = turtleInventory.getItem(selectedSlot)
            if (!PotionBrewing.isIngredient(component)) {
                return@withOperation MethodResult.of(
                    null,
                    "Selected component is not an ingredient for brewing!",
                )
            }
            var usedForBrewing = false
            for (slot in 0 until turtleInventory.containerSize) {
                if (slot == selectedSlot) continue
                val slotStack: ItemStack = turtleInventory.getItem(slot)
                if (slotStack.isEmpty) continue
                if (PotionBrewing.hasMix(slotStack, component)) {
                    turtleInventory.setItem(slot, PotionBrewing.mix(component, slotStack))
                    usedForBrewing = true
                    peripheralOwner.getAbility(PeripheralOwnerAbility.EXPERIENCE)
                        ?.adjustStoredXP(TurtlematicConfig.brewingXPReward)
                }
            }
            if (usedForBrewing) if (component.count == 1) {
                turtleInventory.setItem(selectedSlot, ItemStack.EMPTY)
            } else {
                component.shrink(1)
            }
            MethodResult.of(usedForBrewing)
        }
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun throwPotion(power: Double, angle: Double): MethodResult {
        if (power <= 0.0) throw LuaException("Power cannot be 0")
        val limitedPower = power.coerceAtLeast(TurtlematicConfig.brewingPowerLimit)
        return withOperation(PowerOperation.THROW_POTION, PowerOperationContext(limitedPower), {
            val selectedSlot: Int = peripheralOwner.turtle.selectedSlot
            val turtleInventory: Container = peripheralOwner.turtle.inventory
            val selectedStack: ItemStack = turtleInventory.getItem(selectedSlot)
            val selectedItem: Item = selectedStack.item
            if (selectedItem !== Items.SPLASH_POTION && selectedItem !== Items.LINGERING_POTION) {
                return@withOperation MethodResult.of(
                    null,
                    "Selected item should be splash or lingering potion",
                )
            }
            val potion: Potion = PotionUtils.getPotion(selectedStack)
            if (potion === Potions.EMPTY) return@withOperation MethodResult.of(null, "Selected item is not potion")
            turtleInventory.setItem(
                selectedSlot,
                dispenseBehavior.dispense(BlockSourceImpl(level as ServerLevel, pos), selectedStack, limitedPower, angle),
            )
            MethodResult.of(true)
        })
    }
}
