package site.siredvin.turtlematic.common.items.base
import net.minecraft.core.NonNullList
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import site.siredvin.lib.util.ItemUtil
import site.siredvin.turtlematic.Turtlematic
import java.util.function.Supplier


open class TurtleItem(p: Properties, var turtleID: ResourceLocation, var enableSup: Supplier<Boolean>): DescriptiveItem(p){

    constructor(turtleID: ResourceLocation, enableSup: Supplier<Boolean>): this(
        Properties().tab(Turtlematic.TAB), turtleID, enableSup
    )

    fun isEnabled(): Boolean {
        return enableSup.get()
    }

    override fun fillItemCategory(group: CreativeModeTab, items: NonNullList<ItemStack>) {
        super.fillItemCategory(group, items)
        if (!allowdedIn(group)) return
        items.add(ItemUtil.makeTurtle(turtleID.toString()))
        items.add(ItemUtil.makeAdvancedTurtle(turtleID.toString()))
    }
}