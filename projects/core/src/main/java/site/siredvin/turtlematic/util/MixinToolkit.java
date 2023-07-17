package site.siredvin.turtlematic.util;

import com.mojang.blaze3d.vertex.PoseStack;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import site.siredvin.peripheralium.api.turtle.TurtleUpgradeHolder;
import site.siredvin.peripheralium.util.Pair;
import site.siredvin.turtlematic.api.AutomataCoreTraits;
import site.siredvin.turtlematic.client.RenderTrickOpcode;
import site.siredvin.turtlematic.client.TurtleRenderTrick;
import site.siredvin.turtlematic.client.TurtleRenderTrickRegistry;
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class MixinToolkit {

    public static @Nullable Pair<TurtleRenderTrick, CompoundTag> searchRenderTrickWithData(@Nonnull ITurtleUpgrade upgrade, @Nonnull ITurtleAccess access, @Nonnull TurtleSide side) {
        var coreTrick = TurtleRenderTrickRegistry.INSTANCE.getTrick(upgrade);
        if (coreTrick != null) {
            return Pair.Companion.of(coreTrick, access.getUpgradeNBTData(side));
        }
        if (upgrade instanceof TurtleUpgradeHolder upgradeHolder) {
            for (var internalUpgrade: upgradeHolder.getInternalUpgrades(access, side)) {
                var internalTrick = TurtleRenderTrickRegistry.INSTANCE.getTrick(internalUpgrade.upgrade());
                if (internalTrick != null)
                    return Pair.Companion.of(internalTrick, internalUpgrade.data());
            }
        }
        return null;
    }

    public static void render(
            @Nonnull TurtleBlockEntity turtle, float partialTicks, @Nonnull PoseStack transform,
            @Nonnull MultiBufferSource buffers, int lightmapCoord, int overlayLight, CallbackInfo info) {
        var leftUpgrade = turtle.getUpgrade(TurtleSide.LEFT);
        var rightUpgrade = turtle.getUpgrade(TurtleSide.RIGHT);
        var access = turtle.getAccess();
        var cancelTurtleRender = false;

        if (leftUpgrade != null) {
            var leftRenderTrick = searchRenderTrickWithData(leftUpgrade, access, TurtleSide.LEFT);
            if (leftRenderTrick != null) {
                var opcode = leftRenderTrick.getLeft().render(turtle, access, TurtleSide.LEFT, leftRenderTrick.getRight(), partialTicks, transform, buffers, lightmapCoord, overlayLight);
                if (opcode == RenderTrickOpcode.CANCEL_RENDER)
                    cancelTurtleRender = true;
            }
        }

        if (rightUpgrade != null) {
            var rightRenderTrick = searchRenderTrickWithData(rightUpgrade, access, TurtleSide.RIGHT);
            if (rightRenderTrick != null) {
                var opcode = rightRenderTrick.getLeft().render(turtle, access, TurtleSide.RIGHT, rightRenderTrick.getRight(), partialTicks, transform, buffers, lightmapCoord, overlayLight);
                if (opcode == RenderTrickOpcode.CANCEL_RENDER)
                    cancelTurtleRender = true;
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
