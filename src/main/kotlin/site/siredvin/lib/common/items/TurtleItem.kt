package site.siredvin.lib.common.items
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import site.siredvin.lib.api.TurtleIDBuildFunction
import java.util.function.Supplier


open class TurtleItem(p: Properties, var enableSup: Supplier<Boolean>, private val turtleIDSup: TurtleIDBuildFunction = TurtleIDBuildFunction.IDENTIC): DescriptiveItem(p){

    constructor(tab: CreativeModeTab, enableSup: Supplier<Boolean>,
                turtleIDSup: TurtleIDBuildFunction = TurtleIDBuildFunction.IDENTIC): this(Properties().tab(tab), enableSup, turtleIDSup)

    open val turtleID: ResourceLocation
        get() = turtleIDSup.get(this)

    fun isEnabled(): Boolean {
        return enableSup.get()
    }
}