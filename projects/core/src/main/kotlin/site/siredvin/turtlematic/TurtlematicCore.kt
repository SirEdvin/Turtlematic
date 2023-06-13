package site.siredvin.turtlematic
import net.minecraft.world.item.CreativeModeTab
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.data.ModText
import site.siredvin.turtlematic.xplat.ModRecipeIngredients
import site.siredvin.turtlematic.xplat.TurtlematicCommonHooks
import site.siredvin.turtlematic.xplat.TurtlematicPlatform

@Suppress("UNUSED")
object TurtlematicCore {
    const val MOD_ID = "turtlematic"

    var LOGGER: Logger = LogManager.getLogger(MOD_ID)

    fun configureCreativeTab(builder: CreativeModeTab.Builder): CreativeModeTab.Builder {
        return builder.icon { Items.AUTOMATA_CORE.get().defaultInstance }
            .title(ModText.CREATIVE_TAB.text)
            .displayItems { _, output ->
                TurtlematicPlatform.holder.items.forEach { output.accept(it.get()) }
                TurtlematicCommonHooks.registerTurtlesInCreativeTab(output)
            }
    }

    fun configure(platform: TurtlematicPlatform, ingredients: ModRecipeIngredients) {
        TurtlematicPlatform.configure(platform)
        ModRecipeIngredients.configure(ingredients)
    }
}
