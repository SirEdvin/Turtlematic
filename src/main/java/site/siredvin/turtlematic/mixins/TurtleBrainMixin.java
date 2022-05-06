package site.siredvin.turtlematic.mixins;

import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import site.siredvin.turtlematic.api.AutomataCoreTraits;
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore;

import java.util.EnumMap;
import java.util.Map;

@Mixin(TurtleBrain.class)
public class TurtleBrainMixin {
    @Final
    @Shadow(remap = false)
    private final Map<TurtleSide, ITurtleUpgrade> upgrades = new EnumMap<>( TurtleSide.class );

    @Inject(at = @At("RETURN"), method = "isFuelNeeded()Z", cancellable = true, remap = false)
    public void isFuelNeeded(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            boolean isFuelConsumptionDisabled = this.upgrades.values().stream().anyMatch(it -> {
                Item item = it.getCraftingItem().getItem();
                if (item instanceof BaseAutomataCore core) {
                    return core.getCoreTier().getTraits().contains(AutomataCoreTraits.INSTANCE.getFUEL_CONSUMPTION_DISABLED());
                }
                return false;
            });
            if (isFuelConsumptionDisabled)
                cir.setReturnValue(false);
        }
    }
}
