package site.siredvin.turtlematic.mixins;

import dan200.computercraft.shared.turtle.core.TurtleBrain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import site.siredvin.turtlematic.util.MixinToolkit;

@Mixin(TurtleBrain.class)
public class TurtleBrainMixin {
    @Inject(at = @At("RETURN"), method = "isFuelNeeded()Z", cancellable = true, remap = false)
    public void isFuelNeeded(CallbackInfoReturnable<Boolean> cir) {
        MixinToolkit.isFuelNeeded((TurtleBrain) (Object) this, cir);
    }

    @Inject(at = @At("RETURN"), method = "getColour", cancellable = true, remap = false)
    public void getColour(CallbackInfoReturnable<Integer> cir) {
        var color = MixinToolkit.getColor((TurtleBrain) (Object) this);
        if (color != null)
            cir.setReturnValue(color);
    }
}
