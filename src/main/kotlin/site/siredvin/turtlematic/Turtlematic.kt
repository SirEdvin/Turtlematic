package site.siredvin.turtlematic
import dan200.computercraft.api.ComputerCraftAPI
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import site.siredvin.turtlematic.common.configuration.ConfigHandler
import site.siredvin.turtlematic.common.configuration.ConfigHolder
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.integrations.computercraft.turtle.Automata


@Suppress("UNUSED")
object Turtlematic: ModInitializer {
    const val MOD_ID = "turtlematic"

    @Suppress("MoveLambdaOutsideParentheses")
    val TAB: CreativeModeTab = FabricItemGroupBuilder.build(
        ResourceLocation(MOD_ID, "main"),
        { ItemStack(Items.AUTOMATA_CORE) }
    )

    override fun onInitialize() {
        println("Example mod has been initialized.")
        ConfigHandler.register()
        Items.register()
        ComputerCraftAPI.registerTurtleUpgrade(Automata())
    }
}