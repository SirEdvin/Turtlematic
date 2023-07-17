package site.siredvin.turtlematic.client

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.Tesselator
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.FormattedText
import net.minecraft.util.FormattedCharSequence
import net.minecraft.world.phys.Vec3
import site.siredvin.turtlematic.util.DataStorageObjects

object ChattingTurtleRenderTrick : TurtleRenderTrick {
    // This strange constants are mostly for nice rendering limitations
    private const val MAX_WIDTH = 160
    private const val MAX_LINES = 6
    private const val TEXT_SCALING = 0.025f
    private const val BASE_HEIGHT = 1.6f
    override fun render(
        turtle: TurtleBlockEntity,
        access: ITurtleAccess,
        side: TurtleSide,
        upgradeData: CompoundTag,
        partialTicks: Float,
        transform: PoseStack,
        buffers: MultiBufferSource,
        lightmapCoord: Int,
        overlayLight: Int,
    ): RenderTrickOpcode {
        val text = DataStorageObjects.TurtleChat[upgradeData]

        if (text.isNullOrBlank()) return RenderTrickOpcode.NOOP

        val font = Minecraft.getInstance().font
        val textLines: MutableList<FormattedCharSequence> = ArrayList()
        for (textPart in text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            textLines.addAll(font.split(FormattedText.of(textPart), MAX_WIDTH))
        }
        transform.pushPose()

        val lineHeight = font.lineHeight * TEXT_SCALING
        val height = BASE_HEIGHT + lineHeight * MAX_LINES.coerceAtMost(textLines.size)
        val translation: Vec3 = turtle.getRenderOffset(partialTicks)
        transform.translate(translation.x, translation.y + height, translation.z)
        transform.translate(0.5f, 0f, 0.5f)
        transform.mulPose(Minecraft.getInstance().entityRenderDispatcher.cameraOrientation())

        transform.scale(-TEXT_SCALING, -TEXT_SCALING, TEXT_SCALING)

        var firstLineOffset = 0f
        for (i in 0 until textLines.size.coerceAtMost(MAX_LINES)) {
            val textLine = textLines[i]
            if (i == 0) {
                firstLineOffset = -font.width(textLine) / 2.0f
            }
            val bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().builder)
            font.drawInBatch(
                textLine,
                firstLineOffset,
                (font.lineHeight * (i + 1)).toFloat(),
                0xffffff,
                false,
                transform.last().pose(),
                bufferSource,
                Font.DisplayMode.NORMAL,
                0,
                15728880,
            )
            bufferSource.endBatch()
        }
        transform.popPose()
        return RenderTrickOpcode.NOOP
    }
}
