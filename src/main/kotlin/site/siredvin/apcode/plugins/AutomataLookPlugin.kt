package site.siredvin.apcode.plugins

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import site.siredvin.lib.peripherals.BaseAutomataCorePeripheral
import site.siredvin.lib.util.LuaConverter

class AutomataLookPlugin(
    automataCore: BaseAutomataCorePeripheral,
    private val blockStateConverter: (BlockState) -> (MutableMap<String, Any>) = LuaConverter::blockStateToLua,
    private val entityConverter: (Entity) -> (MutableMap<String, Any>) = LuaConverter::entityToLua,
    private val allowedMods: Set<InteractionMode> = InteractionMode.values().toSet()
) : AutomataCorePlugin(automataCore) {

    private fun lookImpl(arguments: IArguments, overwrittenDirection: Direction? = null): MethodResult {
        val mode = InteractionMode.luaValueOf(arguments.optString(0, InteractionMode.BOTH.name))
        if (!allowedMods.contains(mode))
            return MethodResult.of(null, "Mode $mode are not allowed for this core")
        automataCore.addRotationCycle()
        val owner = automataCore.peripheralOwner
        val result = owner.withPlayer({
                APFakePlayer -> APFakePlayer.findHit(skipEntity = mode.skipEntry, skipBlock = mode.skipBlock)
        }, overwrittenDirection=overwrittenDirection)
        if (result.type == HitResult.Type.MISS)
            return MethodResult.of(null, "Nothing found")
        if (result is BlockHitResult)
            return MethodResult.of(blockStateConverter(owner.level!!.getBlockState(result.blockPos)))
        if (result is EntityHitResult)
            return MethodResult.of(entityConverter(result.entity))
        return MethodResult.of(null, "Nothing found")
    }

    @LuaFunction(mainThread = true)
    fun look(arguments: IArguments): MethodResult {
        return lookImpl(arguments)
    }

    @LuaFunction(mainThread = true)
    fun lookUp(arguments: IArguments): MethodResult {
        return lookImpl(arguments, Direction.UP)
    }

    @LuaFunction(mainThread = true)
    fun lookDown(arguments: IArguments): MethodResult {
        return lookImpl(arguments, Direction.DOWN)
    }
}