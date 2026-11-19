package site.siredvin.turtlematic.xplat

import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.upgrades.UpgradeData
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceKey
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

    // Must use the tab's own holders: the toolkit's key-only lookup reads server registries, which a remote client lacks.
    fun registerTurtlesInCreativeTab(holders: HolderLookup.Provider, output: CreativeModeTab.Output) {
        val registry = holders.lookupOrThrow(ITurtleUpgrade.REGISTRY)
        ModPlatform.holder.turtleUpgrades.forEach {
            val upgrade = registry.get(ResourceKey.create(ITurtleUpgrade.REGISTRY, it.id))
            if (upgrade.isPresent) {
                ComputerPlatformToolkit.get().createTurtlesWithUpgrade(UpgradeData.ofDefault(upgrade.get()))
                    .forEach(output::accept)
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
