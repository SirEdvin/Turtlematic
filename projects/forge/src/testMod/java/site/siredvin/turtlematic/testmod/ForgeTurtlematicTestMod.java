package site.siredvin.turtlematic.testmod;

import net.minecraftforge.fml.common.Mod;
import site.siredvin.testiarium.ForgeTestiarium;
import site.siredvin.testiarium.Testiarium;

@Mod("turtlematic_testmod")
public final class ForgeTurtlematicTestMod {
    public ForgeTurtlematicTestMod() {
        Testiarium.register(TurtlematicGameTests.class);
        ForgeTestiarium.registerTests();
    }
}
