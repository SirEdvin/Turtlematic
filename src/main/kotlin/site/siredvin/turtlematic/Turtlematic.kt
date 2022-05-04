package site.siredvin.turtlematic
import dan200.computercraft.api.ComputerCraftAPI
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraftforge.api.ModLoadingContext
import net.minecraftforge.fml.config.ModConfig
import site.siredvin.turtlematic.common.configuration.ConfigHolder
import site.siredvin.turtlematic.common.setup.Items
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry
import site.siredvin.turtlematic.computercraft.ComputerCraftProxy
import site.siredvin.turtlematic.computercraft.turtle.*
import site.siredvin.turtlematic.util.Platform
import site.siredvin.turtlematic.util.TooltipHandlerCollection
import java.util.function.Consumer


@Suppress("UNUSED")
object Turtlematic: ModInitializer {
    const val MOD_ID = "turtlematic"

    var LOGGER: Logger = LogManager.getLogger(MOD_ID)

    @Suppress("MoveLambdaOutsideParentheses")
    val TAB: CreativeModeTab = FabricItemGroupBuilder.create(ResourceLocation(MOD_ID, "main"))
        .appendItems(Consumer {
            Items.ITEMS.forEach {item -> it.add(item.defaultInstance)}
            ComputerCraftProxy.fillCreativeTab(it)
        })
        .icon({ ItemStack(Items.AUTOMATA_CORE) }).build()

    override fun onInitialize() {
        ModLoadingContext.registerConfig(MOD_ID, ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC)
        LOGGER.info(Items.AUTOMATA_CORE.descriptionId)

        SoulHarvestRecipeRegistry.injectAutomataCoreRecipes()
        SoulHarvestRecipeRegistry.injectForgedAutomataCoreRecipes()

        Platform.maybeLoadIntegration("chipped").ifPresent { (it as Runnable).run() }

        ComputerCraftProxy.initialize()

        TooltipHandlerCollection.registerDefaults()
    }
}