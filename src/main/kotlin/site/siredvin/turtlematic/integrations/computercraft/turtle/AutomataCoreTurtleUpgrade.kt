package site.siredvin.turtlematic.integrations.computercraft.turtle

import dan200.computercraft.api.client.TransformedModel
import dan200.computercraft.api.peripheral.IPeripheral
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.api.turtle.TurtleUpgradeType
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.AffineTransformation
import net.minecraft.util.math.Quaternion
import net.minecraft.util.math.Vec3f
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.integrations.computercraft.peripherals.AutomataCorePeripheral


class AutomataCoreTurtleUpgrade: ITurtleUpgrade {
    private val ID: Identifier = Identifier(Turtlematic.MOD_ID, "test_upgrade")
    private val CRAFTING_ITEM: ItemStack = ItemStack(Items.AUTOMATA_CORE)
    private val PERIPHERAL: IPeripheral = AutomataCorePeripheral()

    // Turtle upgrades can be tools, peripherals, or both. This simple upgrade is just a peripheral.
    override fun getType(): TurtleUpgradeType {
        return TurtleUpgradeType.PERIPHERAL
    }

    override fun createPeripheral(turtle: ITurtleAccess, side: TurtleSide): IPeripheral {
        return PERIPHERAL
    }

    // Re-use our block model with appropriate transformation to give our upgrade visuals. Could also create and load
    // a bespoke .json model.
    override fun getModel(iTurtleAccess: ITurtleAccess?, turtleSide: TurtleSide): TransformedModel {
        val offset = if (turtleSide == TurtleSide.RIGHT) 0.23f else -0.23f
        val affine = AffineTransformation(
            Vec3f(offset + 0.3125f, 0.3125f, 0.28125f),
            Quaternion.IDENTITY,
            Vec3f(0.375f, 0.375f, 0.375f),
            Quaternion.IDENTITY
        )
        return TransformedModel.of(CRAFTING_ITEM, affine)
    }

    override fun getUpgradeID(): Identifier {
        return ID
    }

    // This translation key is defined in the lang file. see resources/assets/cc_test/lang
    override fun getUnlocalisedAdjective(): String {
        return "turtle-adjective.testy"
    }

    // This method is used by Computer Craft to allow crafting the upgrade item with a turtle, and allow the turtle
    // equip command to function.
    override fun getCraftingItem(): ItemStack {
        return CRAFTING_ITEM
    }
}