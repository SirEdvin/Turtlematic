package site.siredvin.turtlematic.computercraft.peripheral.forged

import dan200.computercraft.api.ComputerCraftTags
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.tags.EnchantmentTags
import net.minecraft.util.RandomSource
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.ItemEnchantments
import site.siredvin.broccolium.modules.base.util.ValueContainer
import site.siredvin.broccolium.modules.base.util.XPUtil
import site.siredvin.broccolium.modules.base.util.world.ScanUtils
import site.siredvin.broccolium.modules.platform.PlatformToolkit
import site.siredvin.broccolium.modules.storage.item.AgnosticItemStorageLookup
import site.siredvin.turtlematic.api.AutomataCoreTraits
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.tags.BlockTags
import site.siredvin.tweakium.modules.peripheral.api.IPeripheralOperation
import site.siredvin.tweakium.modules.peripheral.boon.PeripheralOwnerBoonKey
import site.siredvin.tweakium.modules.peripheral.representation.LuaRepresentation
import site.siredvin.tweakium.modules.peripheral.util.assertBetween
import site.siredvin.tweakium.modules.peripheral.util.isCorrectSlot
import java.util.*
import java.util.stream.Stream
import kotlin.math.max

open class EnchantingAutomataCorePeripheral(turtle: ITurtleAccess, side: TurtleSide, tier: IAutomataCoreTier) : ExperienceAutomataCorePeripheral(type, turtle, side, tier) {
    companion object : PeripheralConfiguration {
        override val type = "enchantingAutomata"

        private const val MAX_ENCHANTMENT_LEVEL = 30
    }

    var storedEnchantmentSeed: Long = -1

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableEnchantingAutomataCore

    open val allowTreasureEnchants: Boolean
        get() = tier.traits.contains(AutomataCoreTraits.SKILLED)

    var enchantmentSeed: Long
        get() {
            if (storedEnchantmentSeed == -1L) {
                storedEnchantmentSeed = peripheralOwner.level!!.random.nextLong()
            }
            return storedEnchantmentSeed
        }
        set(value) {
            storedEnchantmentSeed = value
        }

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
            val level = peripheralOwner.level!!
            ScanUtils.traverseBlocks(level, peripheralOwner.pos, 2, { blockState, blockPos ->
                if (blockState.`is`(BlockTags.ENCHANTMENT_POWER_PROVIDER)) {
                    enchantmentPower.value += 1
                } else if (blockState.`is`(ComputerCraftTags.Blocks.TURTLE)) {
                    val itemStorage = AgnosticItemStorageLookup.extractFromBlock(level, blockPos, level.getBlockEntity(blockPos), null)
                    itemStorage?.getContent()?.forEach {
                        if (it.`is`(Items.ENCHANTED_BOOK)) {
                            enchantmentPower.value += 1
                        }
                    }
                }
            })
            return max(enchantmentPower.value * 2, MAX_ENCHANTMENT_LEVEL)
        }

    private fun getComponentType(stack: ItemStack): DataComponentType<ItemEnchantments> = if (stack.`is`(Items.ENCHANTED_BOOK)) DataComponents.STORED_ENCHANTMENTS else DataComponents.ENCHANTMENTS

    private fun extractEnchantments(stack: ItemStack): ItemEnchantments = stack.getOrDefault(getComponentType(stack), ItemEnchantments.EMPTY)

    private fun buildEnchantments(): Stream<Holder<Enchantment>> {
        val enchantmentRegistry = PlatformToolkit.get().registries!!.lookupOrThrow(Registries.ENCHANTMENT)
        var enchantmentStream: Stream<Holder<Enchantment>> = Stream.of()
        enchantmentRegistry.get(EnchantmentTags.IN_ENCHANTING_TABLE).ifPresent { enchantments ->
            enchantmentStream = Stream.concat(enchantmentStream, enchantments.stream())
        }
        if (allowTreasureEnchants) {
            enchantmentRegistry.get(EnchantmentTags.TREASURE).ifPresent { enchantments ->
                enchantmentStream = Stream.concat(enchantmentStream, enchantments.stream())
            }
        }
        return enchantmentStream
    }

    @LuaFunction(mainThread = true, value = ["getEnchantmentPower"])
    fun getEnchantmentPowerLua(): Int = enchantmentPower

    @LuaFunction(mainThread = true)
    fun refreshEnchantments() {
        enchantmentSeed += peripheralOwner.level!!.random.nextLong()
    }

    @LuaFunction(mainThread = true)
    fun getPossibleEnchantments(): MethodResult {
        val selectedSlot: Int = peripheralOwner.turtle.selectedSlot
        val turtleInventory: Container = peripheralOwner.turtle.inventory
        val targetItem: ItemStack = turtleInventory.getItem(selectedSlot)
        if (!targetItem.isEnchantable) return MethodResult.of(null, "Item is not enchantable")
        if (targetItem.isEnchanted) return MethodResult.of(null, "Item already enchanted!")
        val possibleEnchantments = mutableListOf<Map<String, Any>>()
        val experienceAbility = peripheralOwner.getBoon(PeripheralOwnerBoonKey.EXPERIENCE)
            ?: return MethodResult.of(null, "Internal error ...?")
        intArrayOf(0, 1, 2).forEach {
            val cost = EnchantmentHelper.getEnchantmentCost(RandomSource.create(enchantmentSeed + it), it, enchantmentPower, targetItem)
            val enchantments = EnchantmentHelper.selectEnchantment(RandomSource.create(enchantmentSeed + it), targetItem, cost, buildEnchantments())
            if (enchantments.isNotEmpty()) {
                val enchantment = enchantments.first()
                val baseInformation = LuaRepresentation.forEnchantment(enchantment.enchantment.value(), enchantment.level)
                val requiredCost = XPUtil.levelsToXP(cost)
                baseInformation["requiredXP"] = requiredCost
                baseInformation["cost"] = XPUtil.levelReductionToXp(
                    experienceAbility.getStoredXP().coerceAtLeast(requiredCost),
                    it + 1,
                )
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
            val experienceAbility = peripheralOwner.getBoon(PeripheralOwnerBoonKey.EXPERIENCE)
                ?: return@withOperation MethodResult.of(null, "Internal error ...?")
            addRotationCycle()
            val selectedSlot: Int = peripheralOwner.turtle.selectedSlot
            val turtleInventory: Container = peripheralOwner.turtle.inventory
            val targetItem: ItemStack = turtleInventory.getItem(selectedSlot)
            if (!targetItem.isEnchantable) return@withOperation MethodResult.of(null, "Item is not enchantable")
            if (targetItem.isEnchanted) return@withOperation MethodResult.of(null, "Item already enchanted!")
            val requiredXP: Double = XPUtil.levelReductionToXp(experienceAbility.getStoredXP(), luaSlot)
            if (requiredXP > experienceAbility.getStoredXP()) {
                return@withOperation MethodResult.of(
                    null,
                    String.format("Not enough XP, %d required", requiredXP),
                )
            }
            val enchantedItem: ItemStack =
                EnchantmentHelper.enchantItem(
                    RandomSource.create(enchantmentSeed + slot),
                    targetItem,
                    enchantmentPower,
                    buildEnchantments(),
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
            if (!selectedItem.isEnchanted) {
                return@withOperation MethodResult.of(null, "Selected item is not enchanted")
            }
            if (!targetItem.item.equals(Items.BOOK)) {
                return@withOperation MethodResult.of(null, "Target item is not book")
            }
            if (targetItem.count != 1) {
                return@withOperation MethodResult.of(null, "Target book should be 1 in stack")
            }
            val enchants = ItemEnchantments.Mutable(extractEnchantments(selectedItem))
            if (!tier.traits.contains(AutomataCoreTraits.SKILLED)) {
                if (peripheralOwner.level!!.random.nextInt(100) < TurtlematicConfig.enchantmentWipeChance * 100) {
                    enchants.keySet().stream().findAny().ifPresent { enchants.set(it, 0) }
                }
            }
            val enchantedBook = ItemStack(Items.ENCHANTED_BOOK)
            EnchantmentHelper.setEnchantments(enchantedBook, enchants.toImmutable())
            EnchantmentHelper.setEnchantments(selectedItem, ItemEnchantments.EMPTY)
            turtleInventory.setItem(realSlot, enchantedBook)
            return@withOperation MethodResult.of(true)
        }
    }
}
