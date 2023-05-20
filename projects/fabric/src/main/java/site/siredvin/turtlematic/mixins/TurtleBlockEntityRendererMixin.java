package site.siredvin.turtlematic.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.client.render.TurtleBlockEntityRenderer;
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import site.siredvin.turtlematic.computercraft.peripheral.misc.TurtleChatterPeripheral;
import site.siredvin.turtlematic.util.DataStorageObjects;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Mixin(TurtleBlockEntityRenderer.class)
public class TurtleBlockEntityRendererMixin{

    // This strange constants are mostly for nice rendering limitations
    private static final int MAX_WIDTH = 160;
    private static final int MAX_LINES = 6;
    private static final float TEXT_SCALING = 0.025F;
    private static final float BASE_HEIGHT = 1.6f;

    @Inject(at = @At("HEAD"), method="render(Ldan200/computercraft/shared/turtle/blocks/TurtleBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V")
    public void render(
            @Nonnull TurtleBlockEntity turtle, float partialTicks, @Nonnull PoseStack transform,
            @Nonnull MultiBufferSource buffers, int lightmapCoord, int overlayLight, CallbackInfo info) {
        var side = TurtleSide.LEFT;
        var leftUpdate = turtle.getUpgrade(side);
        if (leftUpdate == null || !leftUpdate.getUpgradeID().equals(TurtleChatterPeripheral.Companion.getUPGRADE_ID())) {
            side = TurtleSide.RIGHT;
            var rightUpdate =  turtle.getUpgrade(side);
            if (rightUpdate == null || !rightUpdate.getUpgradeID().equals(TurtleChatterPeripheral.Companion.getUPGRADE_ID()))
                return;
        }

        String text = DataStorageObjects.TurtleChat.INSTANCE.getMessage(turtle.getAccess(), side);

        if (text == null || text.isBlank())
            return;

        Font font = Minecraft.getInstance().font;
        List<FormattedCharSequence> textLines = new ArrayList<>();
        for (String textPart: text.split("\n")) {
            textLines.addAll(font.split(FormattedText.of(textPart), MAX_WIDTH));
        }
        transform.pushPose();

        float lineHeight = font.lineHeight * TEXT_SCALING;
        float height = BASE_HEIGHT + lineHeight * Math.min(MAX_LINES, textLines.size());
        var translation = turtle.getRenderOffset(partialTicks);
        transform.translate(translation.x, translation.y + height, translation.z);
        transform.translate(0.5f, 0, 0.5f);
        transform.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());

        transform.scale(-TEXT_SCALING, -TEXT_SCALING, TEXT_SCALING);

        float firstLineOffset = 0;
        for (int i = 0; i < Math.min(textLines.size(), MAX_LINES); i++) {
            var textLine = textLines.get(i);
            if (i == 0) {
                firstLineOffset = -font.width(textLine) / 2.0f;
            }
            font.draw(transform, textLine, firstLineOffset, font.lineHeight*(i + 1), 0xffffff);
        }
        transform.popPose();
    }
}
