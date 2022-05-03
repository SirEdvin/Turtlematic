package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.piston.PistonStructureResolver
import site.siredvin.lib.computercraft.peripheral.BasePeripheral
import site.siredvin.lib.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.lib.util.world.PistonSimulation
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig

class StickyPistonPeripheral(peripheralOwner: TurtlePeripheralOwner) :
    BasePeripheral<TurtlePeripheralOwner>(TYPE, peripheralOwner) {

    companion object {
        const val TYPE = "sticky_piston"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableStickyPistonTurtle

    @LuaFunction(mainThread = true)
    fun push(): MethodResult {
        val direction = peripheralOwner.facing
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
    fun pull(): MethodResult {
        val direction = peripheralOwner.facing
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