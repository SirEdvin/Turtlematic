package site.siredvin.lib.common.blocks

import net.minecraft.world.level.block.Block
import kotlin.jvm.JvmOverloads
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.material.Material

class BaseBlock @JvmOverloads constructor(
    properties: Properties = Properties.of(Material.METAL).strength(1f, 5f).sound(SoundType.METAL).noOcclusion()
) : Block(properties) {
    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.MODEL
    }
}