package site.siredvin.turtlematic.testmod;

import net.neoforged.fml.common.Mod;
import site.siredvin.testiarium.Testiarium;

@Mod("turtlematic_testmod")
public final class NeoForgeTurtlematicTestMod {
    public NeoForgeTurtlematicTestMod() {
        Testiarium.register(TurtlematicGameTests.class);
    }
}
