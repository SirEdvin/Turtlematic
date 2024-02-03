package site.siredvin.turtlematic

import dan200.computercraft.api.client.turtle.RegisterTurtleModellersEvent
import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers
import net.minecraftforge.client.event.ModelEvent.RegisterAdditional
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

@Mod.EventBusSubscriber(modid = TurtlematicCore.MOD_ID, value = [Dist.CLIENT], bus = Mod.EventBusSubscriber.Bus.MOD)
object ForgeTurtlematicClient {

    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun onClientSetup(event: FMLClientSetupEvent) {
        TurtlematicCoreClient.onInit()
    }

    @SubscribeEvent
    fun onRegisterRenderers(event: RegisterRenderers) {
        TurtlematicCoreClient.EXTRA_ENTITY_RENDERERS.forEach {
            event.registerEntityRenderer(it.get(), TurtlematicCoreClient.getEntityRendererProvider(it.get()))
        }
    }

    @SubscribeEvent
    fun registerModels(event: RegisterAdditional) {
        TurtlematicCoreClient.registerExtraModels { model: ResourceLocation ->
            event.register(model)
        }
    }

    @SubscribeEvent
    fun registerTurtleModels(event: RegisterTurtleModellersEvent) {
        TurtlematicCoreClient.onModelRegister { serializer, model ->
            @Suppress("UNCHECKED_CAST")
            event.register(serializer as TurtleUpgradeSerialiser<ITurtleUpgrade>, model)
        }
    }
}
