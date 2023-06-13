package site.siredvin.turtlematic.xplat

import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.world.item.CreativeModeTab
import site.siredvin.peripheralium.xplat.PeripheraliumPlatform
import site.siredvin.peripheralium.xplat.XplatRegistries
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry
import site.siredvin.turtlematic.common.setup.EntityTypes
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.common.setup.TurtleUpgradeSerializers
import site.siredvin.turtlematic.util.ChunkManager

object TurtlematicCommonHooks {

    fun onRegister() {
        Items.doSomething()
        EntityTypes.doSomething()
        TurtleUpgradeSerializers.doSomething()
        TurtlematicPlatform.registerCreativeTab(
            ResourceLocation(TurtlematicCore.MOD_ID, "tab"),
            TurtlematicCore.configureCreativeTab(PeripheraliumPlatform.createTabBuilder()).build()
        )
    }

    fun commonSetup() {
        SoulHarvestRecipeRegistry.injectAutomataCoreRecipes()
        SoulHarvestRecipeRegistry.injectForgedAutomataCoreRecipes()
    }

    fun registerTurtlesInCreativeTab(output: CreativeModeTab.Output) {
        TurtlematicPlatform.holder.turtleSerializers.forEach {
            val upgrade = PeripheraliumPlatform.getTurtleUpgrade(XplatRegistries.TURTLE_SERIALIZERS.getKey(it.get()).toString())
            if (upgrade != null) {
                PeripheraliumPlatform.createTurtlesWithUpgrade(upgrade).forEach(output::accept)
            }
        }
    }

    fun onServerStarted(server: MinecraftServer) {
        ChunkManager.get(server.overworld()).init(server)
    }

    fun onServerStopping(server: MinecraftServer) {
        ChunkManager.get(server.overworld()).stop(server)
    }

    fun onEndOfServerTick(server: MinecraftServer) {
        ChunkManager.get(server.overworld()).tick(server)
    }
}
