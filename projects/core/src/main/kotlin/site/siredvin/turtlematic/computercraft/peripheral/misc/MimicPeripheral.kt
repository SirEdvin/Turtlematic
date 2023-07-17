package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.nbt.TagParser
import site.siredvin.peripheralium.computercraft.peripheral.OwnedPeripheral
import site.siredvin.peripheralium.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.peripheralium.ext.getBlockState
import site.siredvin.peripheralium.extra.dsl.rml1.RMLParsingException
import site.siredvin.peripheralium.util.representation.LuaRepresentation
import site.siredvin.peripheralium.xplat.PeripheraliumPlatform
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.client.RenderUtil
import site.siredvin.turtlematic.tags.BlockTags
import site.siredvin.turtlematic.util.DataStorageObjects

class MimicPeripheral(turtle: ITurtleAccess, side: TurtleSide) :
    OwnedPeripheral<TurtlePeripheralOwner>(TYPE, TurtlePeripheralOwner(turtle, side)) {
    companion object : PeripheralConfiguration {
        override val TYPE = "mimic"
    }

    override val isEnabled: Boolean
        // TODO: fix this
        get() = true

    @LuaFunction(mainThread = true)
    fun setTransformation(rml: String): MethodResult {
        try {
            RenderUtil.parseRML(rml)
        } catch (exception: RMLParsingException) {
            throw LuaException("Unable to parse rml: ${exception.message}")
        }
        DataStorageObjects.RMLInstructions[peripheralOwner] = rml
        return MethodResult.of(true)
    }

    @LuaFunction(mainThread = true)
    fun getTransformation(): String? {
        return DataStorageObjects.RMLInstructions[peripheralOwner]
    }

    @LuaFunction(mainThread = true)
    fun setMimic(arguments: IArguments): MethodResult {
        val mimic = arguments.getBlockState(0)
        if (mimic.`is`(BlockTags.MIMIC_BLOCKLIST)) {
            throw LuaException("You cannot mimic this block, he is in blocklist")
        }
        DataStorageObjects.Mimic[peripheralOwner] = mimic
        val extraData = arguments.optString(1)
        if (extraData.isPresent) {
            DataStorageObjects.MimicExtraData[peripheralOwner] = TagParser.parseTag(extraData.get())
        }
        return MethodResult.of(true)
    }

    @LuaFunction(mainThread = true)
    fun getMimic(): MethodResult {
        val blockState = DataStorageObjects.Mimic[peripheralOwner]
        val blockStateData = blockState?.let { LuaRepresentation.forBlockState(it) }
        val nbtData = DataStorageObjects.MimicExtraData[peripheralOwner]
        val nbtDataRepresentation = nbtData?.let { PeripheraliumPlatform.nbtToLua(it) }
        return MethodResult.of(blockStateData, nbtDataRepresentation)
    }

    @LuaFunction(mainThread = true)
    fun reset(): MethodResult {
        DataStorageObjects.Mimic[peripheralOwner] = null
        DataStorageObjects.MimicExtraData[peripheralOwner] = null
        DataStorageObjects.RMLInstructions[peripheralOwner] = null
        return MethodResult.of(true)
    }
}
