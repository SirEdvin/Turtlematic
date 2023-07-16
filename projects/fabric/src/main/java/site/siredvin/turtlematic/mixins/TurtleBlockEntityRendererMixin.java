package site.siredvin.turtlematic.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import dan200.computercraft.client.render.TurtleBlockEntityRenderer;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import site.siredvin.turtlematic.util.MixinToolkit;

import javax.annotation.Nonnull;

@Mixin(TurtleBlockEntityRenderer.class)
public class TurtleBlockEntityRendererMixin{

    @Inject(at = @At("HEAD"), method="render(Ldan200/computercraft/shared/turtle/blocks/TurtleBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", cancellable = true)
    public void render(
            @Nonnull TurtleBlockEntity turtle, float partialTicks, @Nonnull PoseStack transform,
            @Nonnull MultiBufferSource buffers, int lightmapCoord, int overlayLight, CallbackInfo info) {
        MixinToolkit.render(turtle, partialTicks, transform, buffers, lightmapCoord, overlayLight, info);
    }
}
