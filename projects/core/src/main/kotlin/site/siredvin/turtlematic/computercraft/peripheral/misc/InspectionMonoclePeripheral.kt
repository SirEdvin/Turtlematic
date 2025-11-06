package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.shared.util.NBTUtil
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.tweakium.modules.peripheral.OwnedPeripheral
import site.siredvin.tweakium.modules.peripheral.api.VerticalDirection
import site.siredvin.tweakium.modules.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.tweakium.modules.peripheral.representation.LuaRepresentation

class InspectionMonoclePeripheral(turtle: ITurtleAccess, side: TurtleSide) : OwnedPeripheral<TurtlePeripheralOwner>(type, TurtlePeripheralOwner(turtle, side)) {
    companion object : PeripheralConfiguration {
        override val type = "inspection_monocle"
    }
    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableInspectionMonocle

    @LuaFunction(mainThread = true)
    fun inspect(arguments: IArguments): MethodResult {
        val directionArgument = arguments.optString(0)
        val direction = if (directionArgument.isEmpty) peripheralOwner.facing else VerticalDirection.luaValueOf(directionArgument.get()).minecraftDirection
        val blockPos = peripheralOwner.pos.relative(direction)
        val base = LuaRepresentation.forBlockV2(peripheralOwner.level!!, blockPos)
        val entity = peripheralOwner.level!!.getBlockEntity(blockPos)
        if (entity != null) {
            val tag = entity.saveWithoutMetadata()
            val luaTag = NBTUtil.toLua(tag)
            if (luaTag != null)
                base["nbt"] = luaTag
        }
        return MethodResult.of(base)
    }
}
