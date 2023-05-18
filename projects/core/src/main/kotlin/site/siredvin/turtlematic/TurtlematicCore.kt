package site.siredvin.turtlematic
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


@Suppress("UNUSED")
object TurtlematicCore {
    const val MOD_ID = "turtlematic"

    var LOGGER: Logger = LogManager.getLogger(MOD_ID)

//    override fun onInitialize() {
//        ModLoadingContext.registerConfig(MOD_ID, ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC)
//        LOGGER.info(Items.AUTOMATA_CORE.descriptionId)
//
//        SoulHarvestRecipeRegistry.injectAutomataCoreRecipes()
//        SoulHarvestRecipeRegistry.injectForgedAutomataCoreRecipes()
//
//        Platform.maybeLoadIntegration("chipped").ifPresent { (it as Runnable).run() }
//
//        ComputerCraftProxy.initialize()
//
//        TooltipHandlerCollection.registerDefaults()
//        ChunkManager.registerHooks()
//    }
}