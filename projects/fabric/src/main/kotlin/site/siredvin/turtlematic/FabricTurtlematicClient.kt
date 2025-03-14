package site.siredvin.turtlematic

import dan200.computercraft.api.client.FabricComputerCraftAPIClient
import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.upgrades.UpgradeType
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.resources.ResourceLocation

object FabricTurtlematicClient : ClientModInitializer {
    override fun onInitializeClient() {
        TurtlematicCoreClient.onInit()
        ModelLoadingPlugin.register {
            it.addModels(TurtlematicCoreClient.EXTRA_MODELS.map { id -> ResourceLocation.fromNamespaceAndPath(TurtlematicCore.MOD_ID, id) })
        }
        TurtlematicCoreClient.EXTRA_ENTITY_RENDERERS.forEach {
            EntityRendererRegistry.register(it.get(), TurtlematicCoreClient.getEntityRendererProvider(it.get()))
        }
        TurtlematicCoreClient.onModelRegister { serializer, modeller ->
            @Suppress("UNCHECKED_CAST")
            FabricComputerCraftAPIClient.registerTurtleUpgradeModeller(serializer as UpgradeType<ITurtleUpgrade>, modeller)
        }
    }
}
