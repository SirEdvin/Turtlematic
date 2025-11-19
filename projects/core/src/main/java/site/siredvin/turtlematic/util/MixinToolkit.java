package site.siredvin.turtlematic.util;

import com.mojang.blaze3d.vertex.PoseStack;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.upgrades.UpgradeData;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import site.siredvin.turtlematic.api.AutomataCoreTraits;
import site.siredvin.turtlematic.client.RenderTrickOpcode;
import site.siredvin.turtlematic.client.TurtleRenderTrick;
import site.siredvin.turtlematic.client.TurtleRenderTrickRegistry;
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore;
import site.siredvin.tweakium.modules.peripheral.api.IDataStorage;
import site.siredvin.tweakium.modules.peripheral.util.CompoundTagDataStorage;
import site.siredvin.tweakium.modules.turtle.api.TurtleUpgradeHolder;

import java.util.Map;
import java.util.function.Function;

public class MixinToolkit {

    public static PairMonad<TurtleRenderTrick, IDataStorage> searchRenderTrickWithData(ITurtleUpgrade upgrade, ITurtleAccess access, TurtleSide side) {
        var coreTrick = TurtleRenderTrickRegistry.INSTANCE.getTrick(upgrade);
        if (coreTrick != null) {
            return new PairMonad<>(coreTrick, new CompoundTagDataStorage(access.getUpgradeNBTData(side), () -> null));
        }
        if (upgrade instanceof TurtleUpgradeHolder upgradeHolder) {
            for (var internalUpgrade: upgradeHolder.getInternalUpgrades(access, side)) {
                var internalTrick = TurtleRenderTrickRegistry.INSTANCE.getTrick(internalUpgrade.upgrade());
                if (internalTrick != null)
                    return new PairMonad<>(internalTrick, new CompoundTagDataStorage(internalUpgrade.data(), () -> null));
            }
        }
        return null;
    }

    public static <T> T traverseUpgrades(ITurtleAccess access, Function<UpgradeData<ITurtleUpgrade>, T> consumer) {
        var leftUpgrade = access.getUpgradeWithData(TurtleSide.LEFT);
        var rightUpgrade = access.getUpgradeWithData(TurtleSide.RIGHT);
        if (leftUpgrade != null) {
            var result = consumer.apply(leftUpgrade);
            if (result != null)
                return result;
            if (leftUpgrade.upgrade() instanceof TurtleUpgradeHolder upgradeHolder) {
                for (var subUpgrade: upgradeHolder.getInternalUpgrades(access, TurtleSide.LEFT)) {
                    var subResult = consumer.apply(subUpgrade);
                    if (subResult != null)
                        return subResult;
                }
            }
        }
        if (rightUpgrade != null) {
            var result = consumer.apply(rightUpgrade);
            if (result != null)
                return result;
            if (rightUpgrade.upgrade() instanceof TurtleUpgradeHolder upgradeHolder) {
                for (var subUpgrade: upgradeHolder.getInternalUpgrades(access, TurtleSide.RIGHT)) {
                    var subResult = consumer.apply(subUpgrade);
                    if (subResult != null)
                        return subResult;
                }
            }
        }
        return null;
    }

    public static Integer getColor(ITurtleAccess access) {
        Function<UpgradeData<ITurtleUpgrade>, Integer> func = (upgrade) -> DataStorageObjects.TurtleColor.INSTANCE.get(upgrade.data());
        return traverseUpgrades(access, func);
    }

    public static void render(
            TurtleBlockEntity turtle, float partialTicks, PoseStack transform,
            MultiBufferSource buffers, int lightmapCoord, int overlayLight, CallbackInfo info) {
        var leftUpgrade = turtle.getUpgrade(TurtleSide.LEFT);
        var rightUpgrade = turtle.getUpgrade(TurtleSide.RIGHT);
        var access = turtle.getAccess();
        var cancelTurtleRender = false;

        if (leftUpgrade != null) {
            var leftRenderTrick = searchRenderTrickWithData(leftUpgrade, access, TurtleSide.LEFT);
            if (leftRenderTrick != null) {
                var opcode = leftRenderTrick.first().render(turtle, access, TurtleSide.LEFT, leftRenderTrick.second(), partialTicks, transform, buffers, lightmapCoord, overlayLight);
                if (opcode == RenderTrickOpcode.CANCEL_RENDER)
                    cancelTurtleRender = true;
            }
        }

        if (rightUpgrade != null) {
            var rightRenderTrick = searchRenderTrickWithData(rightUpgrade, access, TurtleSide.RIGHT);
            if (rightRenderTrick != null) {
                var opcode = rightRenderTrick.first().render(turtle, access, TurtleSide.RIGHT, rightRenderTrick.second(), partialTicks, transform, buffers, lightmapCoord, overlayLight);
                if (opcode == RenderTrickOpcode.CANCEL_RENDER)
                    cancelTurtleRender = true;
            }
        }
        if (cancelTurtleRender) info.cancel();
    }

    public static void isFuelNeeded(ITurtleAccess access, Map<TurtleSide, ITurtleUpgrade> upgrades, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            boolean isFuelConsumptionDisabled = upgrades.entrySet().stream().anyMatch(it -> {
                if (it.getValue() instanceof TurtleUpgradeHolder) {
                    for (var internalUpgrade: ((TurtleUpgradeHolder) it.getValue()).getInternalUpgrades(access, it.getKey())) {
                        Item item = internalUpgrade.getUpgradeItem().getItem();
                        if (item instanceof BaseAutomataCore core) {
                            if (core.getCoreTier().getTraits().contains(AutomataCoreTraits.INSTANCE.getFUEL_CONSUMPTION_DISABLED()))
                                return true;
                        }
                    }
                }
                Item item = it.getValue().getCraftingItem().getItem();
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
