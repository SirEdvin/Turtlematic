package site.siredvin.turtlematic.util;

import com.mojang.blaze3d.vertex.PoseStack;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import site.siredvin.turtlematic.api.AutomataCoreTraits;
import site.siredvin.turtlematic.client.TurtleRenderTrickRegistry;
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore;

import javax.annotation.Nonnull;
import java.util.Map;

public class MixinToolkit {

    public static void render(
            @Nonnull TurtleBlockEntity turtle, float partialTicks, @Nonnull PoseStack transform,
            @Nonnull MultiBufferSource buffers, int lightmapCoord, int overlayLight, CallbackInfo info) {
        var leftUpgrade = turtle.getUpgrade(TurtleSide.LEFT);
        var rightUpgrade = turtle.getUpgrade(TurtleSide.RIGHT);
        var access = turtle.getAccess();
        var cancelTurtleRender = false;

        if (leftUpgrade != null) {
            var leftRenderTrick = TurtleRenderTrickRegistry.INSTANCE.getTrick(leftUpgrade);
            if (leftRenderTrick != null) {
                leftRenderTrick.render(turtle, access, TurtleSide.LEFT, partialTicks, transform, buffers, lightmapCoord, overlayLight);
                cancelTurtleRender = leftRenderTrick.getCancelTurtleRender();
            }
        }

        if (rightUpgrade != null) {
            var rightRenderTrick = TurtleRenderTrickRegistry.INSTANCE.getTrick(rightUpgrade);
            if (rightRenderTrick != null) {
                rightRenderTrick.render(turtle, access, TurtleSide.RIGHT, partialTicks, transform, buffers, lightmapCoord, overlayLight);
                cancelTurtleRender = cancelTurtleRender || rightRenderTrick.getCancelTurtleRender();
            }
        }
        if (cancelTurtleRender) info.cancel();
    }

    public static void isFuelNeeded(Map<TurtleSide, ITurtleUpgrade> upgrades, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            boolean isFuelConsumptionDisabled = upgrades.values().stream().anyMatch(it -> {
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
