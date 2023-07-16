package site.siredvin.turtlematic.mixins;

import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import site.siredvin.turtlematic.util.MixinToolkit;

import java.util.EnumMap;
import java.util.Map;

@Mixin(TurtleBrain.class)
public class TurtleBrainMixin {
    @Final
    @Shadow(remap = false)
    private final Map<TurtleSide, ITurtleUpgrade> upgrades = new EnumMap<>( TurtleSide.class );

    @Inject(at = @At("RETURN"), method = "isFuelNeeded()Z", cancellable = true, remap = false)
    public void isFuelNeeded(CallbackInfoReturnable<Boolean> cir) {
        MixinToolkit.isFuelNeeded(upgrades, cir);
    }
}
