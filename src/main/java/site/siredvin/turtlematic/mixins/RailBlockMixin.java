package site.siredvin.turtlematic.mixins;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import site.siredvin.turtlematic.common.blocks.BlockStateProperties;

@Mixin(RailBlock.class)
public class RailBlockMixin {

    @ModifyArg(method="<init>", at=@At(value="INVOKE", target="Lnet/minecraft/world/level/block/RailBlock;registerDefaultState(Lnet/minecraft/world/level/block/state/BlockState;)V"), index = 0)
    private BlockState trickDefaultBlockState(BlockState par1) {
        return par1.setValue(BlockStateProperties.INSTANCE.getBONKED(), false);
    }

    @Inject(at = @At("TAIL"), method = "createBlockStateDefinition")
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(BlockStateProperties.INSTANCE.getBONKED());
    }
}
