package site.siredvin.turtlematic.computercraft.peripheral.forged

import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import site.siredvin.lib.api.peripheral.IPeripheralOperation
import site.siredvin.lib.computercraft.peripheral.ability.PeripheralOwnerAbility
import site.siredvin.lib.util.isCorrectSlot
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.operations.SingleOperation

open class EnchantingAutomataCorePeripheral(turtle: ITurtleAccess, side: TurtleSide, tier: IAutomataCoreTier):
    ExperienceAutomataCorePeripheral(TYPE, turtle, side, tier) {
    companion object {
        const val TYPE = "enchantingAutomataCore"
        private const val MINECRAFT_ENCHANTING_LEVEL_LIMIT = 30
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableEnchantingAutomataCore

    open val allowTreasureEnchants: Boolean
        get() = false

    override val peripheralConfiguration: MutableMap<String, Any>
        get() {
            val data: MutableMap<String, Any> = super.peripheralConfiguration
            data["enchantLevelCost"] = TurtlematicConfig.enchantLevelCost
            data["treasureEnchantmentsAllowed"] = allowTreasureEnchants
            data["enchantmentWipeChance"] = TurtlematicConfig.enchantmentWipeChance
            return data
        }

    override fun possibleOperations(): MutableList<IPeripheralOperation<*>> {
        val base = possibleOperations()
        base.add(SingleOperation.ENCHANTMENT)
        return base
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun enchant(levels: Int): MethodResult {
        return withOperation(SingleOperation.ENCHANTMENT) {
            if (levels > MINECRAFT_ENCHANTING_LEVEL_LIMIT) return@withOperation MethodResult.of(
                null,
                String.format(
                    "Enchanting levels cannot be bigger then %d",
                    MINECRAFT_ENCHANTING_LEVEL_LIMIT
                )
            )
            val experienceAbility = peripheralOwner.getAbility(PeripheralOwnerAbility.EXPERIENCE)
                ?: return@withOperation MethodResult.of(null, "Internal error ...?")
            addRotationCycle()
            val requiredXP: Int = levels * TurtlematicConfig.enchantLevelCost
            if (requiredXP > experienceAbility.getStoredXP()) return@withOperation MethodResult.of(
                null,
                String.format("Not enough XP, %d required", requiredXP)
            )
            val selectedSlot: Int = peripheralOwner.turtle.selectedSlot
            val turtleInventory: Container = peripheralOwner.turtle.inventory
            val targetItem: ItemStack = turtleInventory.getItem(selectedSlot)
            if (!targetItem.isEnchantable) return@withOperation MethodResult.of(null, "Item is not enchantable")
            if (targetItem.isEnchanted) return@withOperation MethodResult.of(null, "Item already enchanted!")
            val enchantedItem: ItemStack =
                EnchantmentHelper.enchantItem(
                    level!!.random,
                    peripheralOwner.toolInMainHand,
                    levels,
                    allowTreasureEnchants
                )
            experienceAbility.adjustStoredXP(-requiredXP.toDouble())
            turtleInventory.setItem(selectedSlot, enchantedItem)
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