package site.siredvin.lib.util

import com.mojang.brigadier.exceptions.CommandSyntaxException
import java.io.ByteArrayInputStream
import java.io.IOException
import net.minecraft.core.BlockPos
import net.minecraft.nbt.*
import net.minecraft.world.level.ChunkPos
import java.util.*

object NBTUtil {
    fun toDirectNBT(`object`: Any?): Tag? {
        // Mostly dan200.computercraft.shared.util toNBTTag method
        // put this map storing changes
        // instead of map serialization use direct map as CompoundNBT
        // assuming that map keys are strings
        return if (`object` == null) {
            null
        } else if (`object` is Boolean) {
            ByteTag.valueOf((if (`object`) 1 else 0).toByte())
        } else if (`object` is Int) {
            IntTag.valueOf((`object` as Int?)!!)
        } else if (`object` is Number) {
            DoubleTag.valueOf(`object`.toDouble())
        } else if (`object` is String) {
            StringTag.valueOf(`object`.toString())
        } else if (`object` is Map<*, *>) {
            val nbt = CompoundTag()
            for ((key, value1) in `object`) {
                val value = toDirectNBT(value1)
                if (key != null && value != null) {
                    nbt.put(key.toString(), value)
                }
            }
            nbt
        } else {
            null
        }
    }

    fun fromText(json: String?): CompoundTag? {
        return try {
            if (json == null) null else TagParser.parseTag(json)
        } catch (ex: CommandSyntaxException) {
//            AdvancedPeripherals.debug("Could not parse json data to NBT", Level.ERROR);
            ex.printStackTrace()
            null
        }
    }

    fun fromBinary(base64: String?): CompoundTag? {
        if (base64 == null) return null
        try {
            Base64.getDecoder().wrap(ByteArrayInputStream(base64.toByteArray()))
                .use { inputStream -> return NbtIo.readCompressed(inputStream) }
        } catch (ex: IOException) {
//            AdvancedPeripherals.debug("Could not parse binary data to NBT", Level.ERROR);
            ex.printStackTrace()
            return null
        }
    }

    fun toNBT(pos: BlockPos): CompoundTag {
        val data = CompoundTag()
        data.putInt("x", pos.x)
        data.putInt("y", pos.y)
        data.putInt("z", pos.z)
        return data
    }

    fun blockPosFromNBT(nbt: CompoundTag): BlockPos {
        return BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"))
    }

    fun toNBT(pos: ChunkPos): CompoundTag {
        val data = CompoundTag()
        data.putInt("x", pos.x)
        data.putInt("z", pos.z)
        return data
    }

    fun chunkPosFromNBT(nbt: CompoundTag): ChunkPos {
        return ChunkPos(nbt.getInt("x"), nbt.getInt("z"))
    }
}