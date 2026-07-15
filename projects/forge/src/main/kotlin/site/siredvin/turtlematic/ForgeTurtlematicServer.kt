package site.siredvin.turtlematic

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.server.ServerStartedEvent
import net.neoforged.neoforge.event.server.ServerStoppingEvent
import net.neoforged.neoforge.event.tick.ServerTickEvent
import site.siredvin.turtlematic.xplat.TurtlematicCommonHooks

@EventBusSubscriber(modid = TurtlematicCore.MOD_ID)
object ForgeTurtlematicServer {
    @SubscribeEvent
    fun onServerStarted(event: ServerStartedEvent) {
        TurtlematicCore.logger.info("Server started")
        TurtlematicCommonHooks.onServerStarted(event.server)
    }

    @SubscribeEvent
    fun onServerStopping(event: ServerStoppingEvent) {
        TurtlematicCore.logger.info("Stopping server")
        TurtlematicCommonHooks.onServerStopping(event.server)
    }

    @SubscribeEvent
    fun onServerTickEnd(event: ServerTickEvent.Post) {
        TurtlematicCommonHooks.onEndOfServerTick(event.server)
    }
}
