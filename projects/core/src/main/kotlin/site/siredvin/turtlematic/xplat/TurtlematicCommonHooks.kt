package site.siredvin.turtlematic.xplat

import dan200.computercraft.api.upgrades.UpgradeData
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.world.item.CreativeModeTab
import site.siredvin.broccolium.modules.platform.PlatformToolkit
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry
import site.siredvin.turtlematic.common.setup.EntityTypes
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.common.setup.TurtleUpgradeSerializers
import site.siredvin.turtlematic.util.ChunkManager
import site.siredvin.tweakium.modules.platform.ComputerPlatformRegistries
import site.siredvin.tweakium.modules.platform.ComputerPlatformToolkit

object TurtlematicCommonHooks {

    fun onRegister() {
        Items.doSomething()
        EntityTypes.doSomething()
        TurtleUpgradeSerializers.doSomething()
        ModPlatform.registerCreativeTab(
            ResourceLocation.fromNamespaceAndPath(TurtlematicCore.MOD_ID, "tab"),
            TurtlematicCore.configureCreativeTab(PlatformToolkit.get().createTabBuilder()).build(),
        )
    }

    fun commonSetup() {
        SoulHarvestRecipeRegistry.injectAutomataCoreRecipes()
        SoulHarvestRecipeRegistry.injectForgedAutomataCoreRecipes()
    }

    fun registerTurtlesInCreativeTab(output: CreativeModeTab.Output) {
//        ModPlatform.holder.turtleUpgrades.forEach {
//            val key = ComputerPlatformRegistries.TURTLE_UPGRADES.getResourceKey(ComputerPlatformRegistries.TURTLE_UPGRADES.get(it.id))
//            if (key.isPresent) {
//                val upgrade = ComputerPlatformRegistries.TURTLE_UPGRADES.get(key.get())
//                if (upgrade.isPresent) {
//                    ComputerPlatformToolkit.get().createTurtlesWithUpgrade(UpgradeData.ofDefault(upgrade.get()))
//                        .forEach(output::accept)
//                }
//            }
//        }
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
