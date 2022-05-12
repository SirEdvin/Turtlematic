package site.siredvin.turtlematic.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import site.siredvin.turtlematic.util.GlobalFlags;

@Mixin(BaseRailBlock.class)
public class RailStateMixin {
    @Inject(at = @At("HEAD"), method="updateDir", cancellable = true)
    protected void updateDir(Level level, BlockPos blockPos, BlockState blockState, boolean bl, CallbackInfoReturnable<BlockState> cir) {
        if (!level.isClientSide)
            if (GlobalFlags.INSTANCE.getRAIL_SHAPE_CORRECTION_SUPPRESSION().get() > 0) {
                GlobalFlags.INSTANCE.getRAIL_SHAPE_CORRECTION_SUPPRESSION().decrementAndGet();
                cir.setReturnValue(blockState);
            }
    }
}
