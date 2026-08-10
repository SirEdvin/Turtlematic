package site.siredvin.turtlematic.testmod

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import site.siredvin.testiarium.FabricTestiarium
import site.siredvin.testiarium.Testiarium
import site.siredvin.testiarium.cct.CctComputers

object FabricTurtlematicTestMod : ModInitializer {
    override fun onInitialize() {
        CctComputers.initialize()
        ServerLifecycleEvents.SERVER_STARTING.register {
            CctComputers.reset()
            TurtlematicComputerFixtures.importFiles(it)
        }
        Testiarium.register(TurtlematicGameTests::class.java)
        Testiarium.register(MiscTurtleGameTests::class.java)
        Testiarium.register(BaseAutomataGameTests::class.java)
        Testiarium.register(ForgedAutomataGameTests::class.java)
        FabricTestiarium.registerTests()
    }
}
