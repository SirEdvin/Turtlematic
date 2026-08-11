package site.siredvin.turtlematic.testmod;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import site.siredvin.testiarium.Testiarium;
import site.siredvin.testiarium.cct.CctComputers;

@Mod("turtlematic_testmod")
public final class NeoForgeTurtlematicTestMod {
    public NeoForgeTurtlematicTestMod() {
        CctComputers.INSTANCE.initialize();
        NeoForge.EVENT_BUS.addListener((ServerStartingEvent event) -> {
            CctComputers.INSTANCE.reset();
            TurtlematicComputerFixtures.INSTANCE.importFiles(event.getServer());
        });
        Testiarium.register(TurtlematicGameTests.class);
        Testiarium.register(MiscTurtleGameTests.class);
        Testiarium.register(BaseAutomataGameTests.class);
        Testiarium.register(ForgedAutomataGameTests.class);
    }
}
