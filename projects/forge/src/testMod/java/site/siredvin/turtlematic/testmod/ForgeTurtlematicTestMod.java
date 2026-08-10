package site.siredvin.turtlematic.testmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.common.Mod;
import site.siredvin.testiarium.ForgeTestiarium;
import site.siredvin.testiarium.Testiarium;
import site.siredvin.testiarium.cct.CctComputers;

@Mod("turtlematic_testmod")
public final class ForgeTurtlematicTestMod {
    public ForgeTurtlematicTestMod() {
        CctComputers.INSTANCE.initialize();
        MinecraftForge.EVENT_BUS.addListener((ServerStartingEvent event) -> {
            CctComputers.INSTANCE.reset();
            TurtlematicComputerFixtures.INSTANCE.importFiles(event.getServer());
        });
        Testiarium.register(TurtlematicGameTests.class);
        Testiarium.register(MiscTurtleGameTests.class);
        Testiarium.register(BaseAutomataGameTests.class);
        Testiarium.register(ForgedAutomataGameTests.class);
        ForgeTestiarium.registerTests();
    }
}
