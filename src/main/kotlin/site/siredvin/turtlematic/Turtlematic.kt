package site.siredvin.turtlematic
import dan200.computercraft.api.ComputerCraftAPI
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraftforge.api.ModLoadingContext
import net.minecraftforge.api.fml.event.config.ModConfigEvent
import net.minecraftforge.fml.config.ModConfig
import site.siredvin.turtlematic.common.configuration.ConfigHandler
import site.siredvin.turtlematic.common.configuration.ConfigHolder
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.integrations.computercraft.turtle.Automata
import site.siredvin.turtlematic.integrations.computercraft.turtle.EndAutomata
import site.siredvin.turtlematic.integrations.computercraft.turtle.EnormousAutomata
import site.siredvin.turtlematic.integrations.computercraft.turtle.HusbandryAutomata
import org.apache.logging.log4j.LogManager;


@Suppress("UNUSED")
object Turtlematic: ModInitializer {
    const val MOD_ID = "turtlematic"

    var LOGGER = LogManager.getLogger(MOD_ID)

    @Suppress("MoveLambdaOutsideParentheses")
    val TAB: CreativeModeTab = FabricItemGroupBuilder.build(
        ResourceLocation(MOD_ID, "main"),
        { ItemStack(Items.AUTOMATA_CORE) }
    )

    override fun onInitialize() {
        ModConfigEvent.LOADING.register(ConfigHandler::onLoad)
        ModConfigEvent.RELOADING.register(ConfigHandler::onLoad)
        ModLoadingContext.registerConfig(MOD_ID, ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC)
        Items.register()
        ComputerCraftAPI.registerTurtleUpgrade(Automata())
        ComputerCraftAPI.registerTurtleUpgrade(EndAutomata())
        ComputerCraftAPI.registerTurtleUpgrade(HusbandryAutomata())
        ComputerCraftAPI.registerTurtleUpgrade(EnormousAutomata())
    }
}