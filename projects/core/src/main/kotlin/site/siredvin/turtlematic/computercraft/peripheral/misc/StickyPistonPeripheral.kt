package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.piston.PistonStructureResolver
import site.siredvin.peripheralium.api.datatypes.VerticalDirection
import site.siredvin.peripheralium.computercraft.peripheral.OwnedPeripheral
import site.siredvin.peripheralium.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.peripheralium.util.world.PistonSimulation
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig

class StickyPistonPeripheral(turtle: ITurtleAccess, side: TurtleSide) :
    OwnedPeripheral<TurtlePeripheralOwner>(TYPE, TurtlePeripheralOwner(turtle, side)) {

    companion object : PeripheralConfiguration {
        override val TYPE = "sticky_piston"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableStickyPistonTurtle

    @LuaFunction(mainThread = true)
    fun push(arguments: IArguments): MethodResult {
        val directionArgument = arguments.optString(0)
        val direction = if (directionArgument.isEmpty) peripheralOwner.facing else VerticalDirection.luaValueOf(directionArgument.get()).minecraftDirection
        val level = level!!
        val resolver = PistonStructureResolver(level, pos, direction, true)
        return if (!resolver.resolve()) {
            MethodResult.of(null, "Cannot resolve piston structure")
        } else {
            PistonSimulation.move(level, resolver, direction, isExtending = true)
            level.playSound(peripheralOwner.owner, peripheralOwner.pos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 10f, 10f)
            MethodResult.of(true)
        }
    }

    @LuaFunction(mainThread = true)
    fun pull(arguments: IArguments): MethodResult {
        val directionArgument = arguments.optString(0)
        val direction = if (directionArgument.isEmpty) peripheralOwner.facing else VerticalDirection.luaValueOf(directionArgument.get()).minecraftDirection
        val level = level!!
        val resolver = PistonStructureResolver(level, pos.relative(direction), direction, false)
        return if (!resolver.resolve()) {
            MethodResult.of(null, "Cannot resolve piston structure")
        } else {
            PistonSimulation.move(level, resolver, direction, isExtending = false)
            level.playSound(peripheralOwner.owner, peripheralOwner.pos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 10f, 10f)
            MethodResult.of(true)
        }
    }
}
