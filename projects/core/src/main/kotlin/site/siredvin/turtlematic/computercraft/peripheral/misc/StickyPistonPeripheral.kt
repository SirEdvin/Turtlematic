package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.piston.PistonStructureResolver
import site.siredvin.broccolium.modules.base.util.world.PistonSimulation
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.util.DataStorageObjects
import site.siredvin.tweakium.modules.peripheral.OwnedPeripheral
import site.siredvin.tweakium.modules.peripheral.api.VerticalDirection
import site.siredvin.tweakium.modules.peripheral.owner.TurtlePeripheralOwner

class StickyPistonPeripheral(turtle: ITurtleAccess, side: TurtleSide) : OwnedPeripheral<TurtlePeripheralOwner>(type, TurtlePeripheralOwner(turtle, side)) {

    companion object : PeripheralConfiguration {
        override val type = "sticky_piston"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableStickyPistonTurtle

    @LuaFunction(mainThread = true)
    fun isSilent(): Boolean = DataStorageObjects.Silent[peripheralOwner]

    @LuaFunction(mainThread = true)
    fun setSilent(value: Boolean) {
        DataStorageObjects.Silent[peripheralOwner] = value
    }

    @LuaFunction(mainThread = true)
    fun push(arguments: IArguments): MethodResult {
        val directionArgument = arguments.optString(0)
        val direction = if (directionArgument.isEmpty) peripheralOwner.facing else VerticalDirection.luaValueOf(directionArgument.get()).minecraftDirection
        val level = peripheralOwner.level!!
        val resolver = PistonStructureResolver(level, peripheralOwner.pos, direction, true)
        return if (!resolver.resolve()) {
            MethodResult.of(null, "Cannot resolve piston structure")
        } else {
            PistonSimulation.move(level, resolver, direction, isExtending = true)
            if (!DataStorageObjects.Silent[peripheralOwner]) {
                level.playSound(
                    peripheralOwner.owner,
                    peripheralOwner.pos,
                    SoundEvents.PISTON_EXTEND,
                    SoundSource.BLOCKS,
                    TurtlematicConfig.pistonVolumeLevel.toFloat(),
                    TurtlematicConfig.pistonPitchLevel.toFloat(),
                )
            }
            MethodResult.of(true)
        }
    }

    @LuaFunction(mainThread = true)
    fun pull(arguments: IArguments): MethodResult {
        val directionArgument = arguments.optString(0)
        val direction = if (directionArgument.isEmpty) peripheralOwner.facing else VerticalDirection.luaValueOf(directionArgument.get()).minecraftDirection
        val level = peripheralOwner.level!!
        val resolver = PistonStructureResolver(level, peripheralOwner.pos, direction, false)
        return if (!resolver.resolve()) {
            MethodResult.of(null, "Cannot resolve piston structure")
        } else {
            PistonSimulation.move(level, resolver, direction, isExtending = false)
            if (!DataStorageObjects.Silent[peripheralOwner]) {
                level.playSound(
                    peripheralOwner.owner,
                    peripheralOwner.pos,
                    SoundEvents.PISTON_CONTRACT,
                    SoundSource.BLOCKS,
                    TurtlematicConfig.pistonVolumeLevel.toFloat(),
                    TurtlematicConfig.pistonPitchLevel.toFloat(),
                )
            }
            MethodResult.of(true)
        }
    }
}
