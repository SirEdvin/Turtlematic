package site.siredvin.turtlematic.computercraft.peripheral.forged

import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.util.RandomSource
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import site.siredvin.peripheralium.api.peripheral.IPeripheralOperation
import site.siredvin.peripheralium.computercraft.peripheral.ability.PeripheralOwnerAbility
import site.siredvin.peripheralium.util.*
import site.siredvin.peripheralium.util.representation.LuaRepresentation
import site.siredvin.peripheralium.util.world.ScanUtils
import site.siredvin.turtlematic.api.AutomataCoreTraits
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.tags.BlockTags
import java.util.Random
import kotlin.math.max

open class EnchantingAutomataCorePeripheral(turtle: ITurtleAccess, side: TurtleSide, tier: IAutomataCoreTier):
    ExperienceAutomataCorePeripheral(TYPE, turtle, side, tier) {
    companion object {
        const val TYPE = "enchantingAutomataCore"
        private const val MAX_ENCHANTMENT_LEVEL = 30
    }

    var storedEnchantmentSeed: Long = -1

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableEnchantingAutomataCore

    open val allowTreasureEnchants: Boolean
        get() = tier.traits.contains(AutomataCoreTraits.SKILLED)

    var enchantmentSeed: Long
        get() {
            if (storedEnchantmentSeed == -1L)
                storedEnchantmentSeed = peripheralOwner.level!!.random.nextLong()
            return storedEnchantmentSeed
        }
        set(value) { storedEnchantmentSeed = value }

    override val peripheralConfiguration: MutableMap<String, Any>
        get() {
            val data: MutableMap<String, Any> = super.peripheralConfiguration
            data["treasureEnchantmentsAllowed"] = allowTreasureEnchants
            data["enchantmentWipeChance"] = TurtlematicConfig.enchantmentWipeChance
            return data
        }

    override fun possibleOperations(): MutableList<IPeripheralOperation<*>> {
        val base = super.possibleOperations()
        base.add(SingleOperation.ENCHANTMENT)
        return base
    }

    private val enchantmentPower: Int
        get() {
            val enchantmentPower = ValueContainer(0)
            val level = level!!
            ScanUtils.traverseBlocks(level, pos, 2, { blockState, blockPos ->
                if (blockState.`is`(BlockTags.ENCHANTMENT_POWER_PROVIDER)) {
                    enchantmentPower.value = enchantmentPower.value + 1
                } else if (blockState.`is`(BlockUtil.TURTLE_ADVANCED) || blockState.`is`(BlockUtil.TURTLE_NORMAL)) {
                    val itemStorage = ItemStorage.SIDED.find(level, blockPos, null)
                    if (itemStorage != null) {
                        Transaction.openOuter().use {
                            itemStorage.iterator().forEach { view ->
                                if (view.resource.toStack().`is`(Items.ENCHANTED_BOOK))
                                    enchantmentPower.value = enchantmentPower.value + 1
                            }
                        }
                    }
                }
            })
            return max(enchantmentPower.value * 2, MAX_ENCHANTMENT_LEVEL)
        }

    @LuaFunction(mainThread = true, value = ["getEnchantmentPower"])
    fun getEnchantmentPowerLua(): Int {
        return enchantmentPower
    }

    @LuaFunction(mainThread = true)
    fun refreshEnchantments() {
        enchantmentSeed += level!!.random.nextLong()
    }

    @LuaFunction(mainThread = true)
    fun getPossibleEnchantments(): MethodResult {
        val selectedSlot: Int = peripheralOwner.turtle.selectedSlot
        val turtleInventory: Container = peripheralOwner.turtle.inventory
        val targetItem: ItemStack = turtleInventory.getItem(selectedSlot)
        if (!targetItem.isEnchantable) return MethodResult.of(null, "Item is not enchantable")
        if (targetItem.isEnchanted) return MethodResult.of(null, "Item already enchanted!")
        val possibleEnchantments = mutableListOf<Map<String, Any>>()
        val experienceAbility = peripheralOwner.getAbility(PeripheralOwnerAbility.EXPERIENCE)
            ?: return MethodResult.of(null, "Internal error ...?")
        intArrayOf(0, 1, 2).forEach {
            val cost = EnchantmentHelper.getEnchantmentCost(RandomSource.create(enchantmentSeed + it), it, enchantmentPower, targetItem)
            val enchantments = EnchantmentHelper.selectEnchantment(RandomSource.create(enchantmentSeed + it), targetItem, cost, allowTreasureEnchants)
            if (enchantments != null && enchantments.isNotEmpty()) {
                val enchantment = enchantments.first()
                val baseInformation = LuaRepresentation.forEnchantment(enchantment.enchantment, enchantment.level)
                val requiredCost = XPUtil.levelsToXP(cost)
                baseInformation["requiredXP"] = requiredCost
                baseInformation["cost"] = XPUtil.levelReductionToXp(
                    experienceAbility.getStoredXP().coerceAtLeast(requiredCost), it + 1)
                possibleEnchantments.add(baseInformation)
            }
        }
        return MethodResult.of(possibleEnchantments)
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun enchant(luaSlot: Int): MethodResult {
        assertBetween(luaSlot, 1, 3, "selected")
        val slot = luaSlot - 1
        return withOperation(SingleOperation.ENCHANTMENT) {
            val experienceAbility = peripheralOwner.getAbility(PeripheralOwnerAbility.EXPERIENCE)
                ?: return@withOperation MethodResult.of(null, "Internal error ...?")
            addRotationCycle()
            val selectedSlot: Int = peripheralOwner.turtle.selectedSlot
            val turtleInventory: Container = peripheralOwner.turtle.inventory
            val targetItem: ItemStack = turtleInventory.getItem(selectedSlot)
            if (!targetItem.isEnchantable) return@withOperation MethodResult.of(null, "Item is not enchantable")
            if (targetItem.isEnchanted) return@withOperation MethodResult.of(null, "Item already enchanted!")
            val requiredXP: Double = XPUtil.levelReductionToXp(experienceAbility.getStoredXP(), luaSlot)
            if (requiredXP > experienceAbility.getStoredXP()) return@withOperation MethodResult.of(
                null,
                String.format("Not enough XP, %d required", requiredXP)
            )
            val enchantedItem: ItemStack =
                EnchantmentHelper.enchantItem(
                    RandomSource.create(enchantmentSeed + slot),
                    targetItem,
                    enchantmentPower,
                    allowTreasureEnchants
                )
            experienceAbility.adjustStoredXP(-requiredXP)
            turtleInventory.setItem(selectedSlot, enchantedItem)
            refreshEnchantments()
            MethodResult.of(true)
        }
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun extractEnchantment(target: Int): MethodResult {
        isCorrectSlot(target)
        val realSlot = target - 1
        return withOperation(SingleOperation.ENCHANTMENT) {
            val turtleInventory: Container = peripheralOwner.turtle.inventory
            val selectedSlot: Int = peripheralOwner.turtle.selectedSlot
            val selectedItem: ItemStack = turtleInventory.getItem(selectedSlot)
            val targetItem: ItemStack = turtleInventory.getItem(realSlot)
            if (!selectedItem.isEnchanted)
                return@withOperation MethodResult.of(null, "Selected item is not enchanted")
            if (!targetItem.item.equals(Items.BOOK))
                return@withOperation MethodResult.of(null, "Target item is not book")
            if (targetItem.count != 1)
                return@withOperation MethodResult.of(null, "Target book should be 1 in stack")
            val enchants: MutableMap<Enchantment, Int> = EnchantmentHelper.getEnchantments(selectedItem)
            if (!tier.traits.contains(AutomataCoreTraits.SKILLED))
                if (level!!.random.nextInt(100) < TurtlematicConfig.enchantmentWipeChance * 100) {
                    enchants.keys.stream().findAny().ifPresent(enchants::remove)
                }
            val enchantedBook = ItemStack(Items.ENCHANTED_BOOK)
            EnchantmentHelper.setEnchantments(enchants, enchantedBook)
            EnchantmentHelper.setEnchantments(emptyMap(), selectedItem)
            turtleInventory.setItem(realSlot, enchantedBook)
            return@withOperation MethodResult.of(true)
        }
    }
}