package site.siredvin.turtlematic.testmod

import net.fabricmc.api.ModInitializer
import site.siredvin.testiarium.FabricTestiarium
import site.siredvin.testiarium.Testiarium

object FabricTurtlematicTestMod : ModInitializer {
    override fun onInitialize() {
        Testiarium.register(TurtlematicGameTests::class.java)
        FabricTestiarium.registerTests()
    }
}
