package site.siredvin.turtlematic.common.items.base
import site.siredvin.turtlematic.Turtlematic
import java.util.function.Supplier


open class TurtleItem(p: Settings): BaseItem(p){
//    private val turtleID: ResourceLocation? = null
    private val enabledSup: Supplier<Boolean>? = null

    constructor(): this(Settings().group(Turtlematic.TAB))


    override fun isEnabled(): Boolean {
        return true
    }

//    fun fillItemCategory(group: CreativeModeTab?, items: NonNullList<ItemStack?>) {
//        super.fillItemCategory(group, items)
//        if (!allowdedIn(group)) return
//        if (turtleID != null) {
//            items.add(ItemUtil.makeTurtle(ItemUtil.TURTLE_ADVANCED, turtleID.toString()))
//            items.add(ItemUtil.makeTurtle(ItemUtil.TURTLE_NORMAL, turtleID.toString()))
//        }
//        if (pocketID != null) {
//            items.add(ItemUtil.makePocket(ItemUtil.POCKET_ADVANCED, pocketID.toString()))
//            items.add(ItemUtil.makePocket(ItemUtil.POCKET_NORMAL, pocketID.toString()))
//        }
//    }
}