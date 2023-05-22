package site.siredvin.turtlematic

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import site.siredvin.turtlematic.common.setup.EntityTypes
import java.util.function.Consumer

object FabricTurtlematicClient: ClientModInitializer {
    override fun onInitializeClient() {
        TurtlematicCoreClient.onInit()
        ModelLoadingRegistry.INSTANCE.registerModelProvider { _: ResourceManager, out: Consumer<ResourceLocation> ->
            TurtlematicCoreClient.registerExtraModels(out)
        }
        TurtlematicCoreClient.EXTRA_ENTITY_RENDERERS.forEach {
            EntityRendererRegistry.register(it.get(), TurtlematicCoreClient.getEntityRendererProvider(it.get()))
        }
    }
}