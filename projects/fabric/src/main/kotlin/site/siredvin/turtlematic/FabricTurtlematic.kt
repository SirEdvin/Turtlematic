package site.siredvin.turtlematic

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraftforge.fml.config.ModConfig
import site.siredvin.peripheralium.FabricPeripheralium
import site.siredvin.turtlematic.common.configuration.ConfigHolder
import site.siredvin.turtlematic.fabric.FabricModInnerPlatform
import site.siredvin.turtlematic.fabric.FabricModRecipeIngredients
import site.siredvin.turtlematic.xplat.TurtlematicCommonHooks

@Suppress("UNUSED")
object FabricTurtlematic : ModInitializer {

    override fun onInitialize() {
        // Register configuration
        FabricPeripheralium.sayHi()
        TurtlematicCore.configure(FabricModInnerPlatform, FabricModRecipeIngredients)
        // Register items and blocks
        TurtlematicCommonHooks.onRegister()
        TurtlematicCommonHooks.commonSetup()
        // Load all integrations
        // Pretty important to setup configuration after integration loading!
        ForgeConfigRegistry.INSTANCE.register(TurtlematicCore.MOD_ID, ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC)
        registerHooks()
    }

    fun registerHooks() {
        // Chunk loader (mostly) hooks
        ServerLifecycleEvents.SERVER_STARTED.register(
            ServerLifecycleEvents.ServerStarted {
                TurtlematicCommonHooks.onServerStarted(it)
            },
        )
        ServerLifecycleEvents.SERVER_STOPPING.register(
            ServerLifecycleEvents.ServerStopping {
                TurtlematicCommonHooks.onServerStopping(it)
            },
        )
        ServerTickEvents.END_SERVER_TICK.register(
            ServerTickEvents.EndTick {
                TurtlematicCommonHooks.onEndOfServerTick(it)
            },
        )
    }
}
