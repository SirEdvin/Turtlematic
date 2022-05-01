package site.siredvin.turtlematic.computercraft.peripheral.forged

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.shared.turtle.blocks.BlockTurtle
import net.minecraft.Util
import net.minecraft.core.*
import net.minecraft.core.dispenser.DispenseItemBehavior
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
import site.siredvin.lib.api.peripheral.IPeripheralOperation
import site.siredvin.lib.computercraft.peripheral.ability.PeripheralOwnerAbility
import site.siredvin.lib.util.representation.effectsData
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.datatypes.AreaInteractionMode
import site.siredvin.turtlematic.computercraft.datatypes.InteractionMode
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.computercraft.plugins.*
import java.util.function.Predicate
import kotlin.math.min

class BrewingAutomataCorePeripheral(turtle: ITurtleAccess, side: TurtleSide, tier: IAutomataCoreTier):
    ExperienceAutomataCorePeripheral(TYPE, turtle, side, tier) {

    companion object {
        const val TYPE = "brewingAutomataCore"
        private val suitableEntity: Predicate<Entity> = Predicate<Entity> { entity: Entity -> entity is ZombieVillager }
    }

    init {
        addPlugin(AutomataLookPlugin(this, entityEnriches = listOf(::effectsData)))
        addPlugin(
            AutomataInteractionPlugin(
                this, allowedMods = InteractionMode.values().toSet(),
                suitableEntity = suitableEntity
            )
        )
        addPlugin(
            AutomataScanPlugin(
                this, suitableEntity = suitableEntity, entityEnriches = listOf(::effectsData),
                allowedMods = setOf(AreaInteractionMode.ITEM, AreaInteractionMode.ENTITY)
            )
        )
    }

    private class TurtlePotionDispenseBehavior constructor(
        private val uncertaintyMultiply: Double,
        private val powerMultiply: Double
    ) : DispenseItemBehavior {
        fun getDispensePosition(direction: Direction, turtleBlockSource: BlockSource): Position {
            val x: Double = turtleBlockSource.x() + 0.7 * direction.stepX
            val y: Double = turtleBlockSource.y() + 0.7 * direction.stepY
            val z: Double = turtleBlockSource.z() + 0.7 * direction.stepZ
            return PositionImpl(x, y, z)
        }

        override fun dispense(turtleBlockSource: BlockSource, stack: ItemStack): ItemStack {
            val lvt_3_1_ = execute(turtleBlockSource, stack)
            playSound(turtleBlockSource)
            playAnimation(turtleBlockSource, turtleBlockSource.blockState.getValue(BlockTurtle.FACING))
            return lvt_3_1_
        }

        fun execute(turtleBlockSource: BlockSource, stack: ItemStack): ItemStack {
            val level: Level = turtleBlockSource.level
            val currentDirection: Direction = turtleBlockSource.blockState.getValue(BlockTurtle.FACING)
            val targetPosition  = getDispensePosition(currentDirection, turtleBlockSource)
            val entity: Projectile = getProjectile(level, targetPosition, stack)
            entity.shoot(
                currentDirection.stepX.toDouble(), currentDirection.stepY.toDouble() + 0.1f,
                currentDirection.stepZ.toDouble(), power, uncertainty
            )
            level.addFreshEntity(entity)
            stack.shrink(1)
            return stack
        }

        protected fun playSound(turtleBlockSource: BlockSource) {
            turtleBlockSource.level.levelEvent(1002, turtleBlockSource.pos, 0)
        }

        protected fun playAnimation(turtleBlockSource: BlockSource, direction: Direction) {
            turtleBlockSource.level.levelEvent(2000, turtleBlockSource.pos, direction.get3DDataValue())
        }

        protected val uncertainty: Float
            get() = (6.0f * uncertaintyMultiply).toFloat()
        protected val power: Float
            get() = (1.1f * powerMultiply).toFloat()

        protected fun getProjectile(level: Level, targetPosition: Position, stack: ItemStack): Projectile {
            return Util.make(
                ThrownPotion(level, targetPosition.x(), targetPosition.y(), targetPosition.z())
            ) { p_218413_1_ -> p_218413_1_.item = stack }
        }
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableBrewingAutomataCore

    override fun possibleOperations(): MutableList<IPeripheralOperation<*>> {
        val operations = super.possibleOperations()
        operations.add(SingleOperation.BREW)
        operations.add(SingleOperation.THROW_POTION)
        return operations
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun brew(): MethodResult {
        return withOperation(SingleOperation.BREW) {
            val turtleInventory: Container = peripheralOwner.turtle.inventory
            val selectedSlot: Int = peripheralOwner.turtle.selectedSlot
            val component: ItemStack = turtleInventory.getItem(selectedSlot)
            if (!PotionBrewing.isIngredient(component)) return@withOperation MethodResult.of(
                null,
                "Selected component is not an ingredient for brewing!"
            )
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
    fun throwPotion(arguments: IArguments): MethodResult {
        val power = min(arguments.optFiniteDouble(0, 1.0), 16.04)
        val uncertainty = min(arguments.optFiniteDouble(1, 1.0), 16.0)
        if (power == 0.0) throw LuaException("Power multiplicator cannot be 0")
        if (uncertainty == 0.0) throw LuaException("Uncertainty multiplicator cannot be 0")
        return withOperation(SingleOperation.THROW_POTION) {
            val selectedSlot: Int = peripheralOwner.turtle.selectedSlot
            val turtleInventory: Container = peripheralOwner.turtle.inventory
            val selectedStack: ItemStack = turtleInventory.getItem(selectedSlot)
            val selectedItem: Item = selectedStack.item
            if (selectedItem !== Items.SPLASH_POTION && selectedItem !== Items.LINGERING_POTION) return@withOperation MethodResult.of(
                null,
                "Selected item should be splash or lingering potion"
            )
            val potion: Potion = PotionUtils.getPotion(selectedStack)
            if (potion === Potions.EMPTY) return@withOperation MethodResult.of(null, "Selected item is not potion")
            val behavior: DispenseItemBehavior = TurtlePotionDispenseBehavior(uncertainty, power)
            turtleInventory.setItem(
                selectedSlot,
                behavior.dispense(BlockSourceImpl(level as ServerLevel, pos), selectedStack)
            )
            MethodResult.of(true)
        }
    }
}