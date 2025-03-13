package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.nbt.TagParser
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.client.RenderUtil
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.tags.BlockTags
import site.siredvin.turtlematic.util.DataStorageObjects
import site.siredvin.tweakium.modules.peripheral.OwnedPeripheral
import site.siredvin.tweakium.modules.peripheral.ext.getBlockState
import site.siredvin.tweakium.modules.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.tweakium.modules.peripheral.representation.LuaRepresentation
import site.siredvin.tweakium.modules.platform.ComputerPlatformToolkit
import site.siredvin.tweakium.modules.rml1.RMLParsingException

class MimicPeripheral(turtle: ITurtleAccess, side: TurtleSide) : OwnedPeripheral<TurtlePeripheralOwner>(type, TurtlePeripheralOwner(turtle, side)) {
    companion object : PeripheralConfiguration {
        override val type = "mimic"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableMimicGadget

    @LuaFunction(mainThread = true)
    fun setTransformation(rml: String) {
        try {
            val instructions = RenderUtil.parseRML(rml)
            if (instructions.size > TurtlematicConfig.mimicGadgetRMLLimit) {
                throw LuaException("You can use up to ${TurtlematicConfig.mimicGadgetRMLLimit} instructions")
            }
        } catch (exception: RMLParsingException) {
            throw LuaException("Unable to parse rml: ${exception.message}")
        }
        DataStorageObjects.RMLInstructions[peripheralOwner] = rml
    }

    @LuaFunction(mainThread = true)
    fun getTransformation(): String? = DataStorageObjects.RMLInstructions[peripheralOwner]

    @LuaFunction(mainThread = true)
    fun setMimic(arguments: IArguments) {
        val mimic = arguments.getBlockState(0)
        if (mimic.`is`(BlockTags.MIMIC_BLOCKLIST)) {
            throw LuaException("You cannot mimic this block, he is in blocklist")
        }
        DataStorageObjects.Mimic[peripheralOwner] = mimic
        val extraData = arguments.optString(1)
        if (extraData.isPresent) {
            DataStorageObjects.MimicExtraData[peripheralOwner] = TagParser.parseTag(extraData.get())
        }
    }

    @LuaFunction(mainThread = true)
    fun getMimic(): MethodResult {
        val blockState = DataStorageObjects.Mimic[peripheralOwner]
        val blockStateData = blockState?.let { LuaRepresentation.forBlockState(it) }
        val nbtData = DataStorageObjects.MimicExtraData[peripheralOwner]
        val nbtDataRepresentation = nbtData?.let { ComputerPlatformToolkit.get().nbtToLua(it) }
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
