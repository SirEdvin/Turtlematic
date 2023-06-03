package site.siredvin.turtlematic

import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.ServerTickEvent
import net.minecraftforge.event.server.ServerStartedEvent
import net.minecraftforge.event.server.ServerStoppingEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import site.siredvin.turtlematic.xplat.TurtlematicCommonHooks

@Mod.EventBusSubscriber(modid = TurtlematicCore.MOD_ID)
object ForgeTurtlematicServer {
    @SubscribeEvent
    fun onServerStarted(event: ServerStartedEvent) {
        TurtlematicCore.LOGGER.info("Server started")
        TurtlematicCommonHooks.onServerStarted(event.server)
    }

    @SubscribeEvent
    fun onServerStopping(event: ServerStoppingEvent) {
        TurtlematicCore.LOGGER.info("Stopping server")
        TurtlematicCommonHooks.onServerStopping(event.server)
    }

    @SubscribeEvent
    fun onServerTickEnd(event: ServerTickEvent) {
//        TurtlematicCore.LOGGER.info("Server ticking!")
        if (event.phase == TickEvent.Phase.END) {
            TurtlematicCommonHooks.onEndOfServerTick(event.server)
        }
    }
}
