package site.siredvin.turtlematic.util

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.ChunkPos

fun toNBT(pos: ChunkPos): CompoundTag {
    val data = CompoundTag()
    data.putInt("x", pos.x)
    data.putInt("z", pos.z)
    return data
}

fun chunkPosFromNBT(nbt: CompoundTag): ChunkPos {
    return ChunkPos(nbt.getInt("x"), nbt.getInt("z"))
}
