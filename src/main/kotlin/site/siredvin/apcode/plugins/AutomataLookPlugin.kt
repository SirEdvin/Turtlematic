package site.siredvin.apcode.plugins

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import site.siredvin.lib.peripherals.BaseAutomataCorePeripheral
import site.siredvin.lib.util.LuaConverter

class AutomataLookPlugin(automataCore: BaseAutomataCorePeripheral) : AutomataCorePlugin(automataCore) {
    @LuaFunction(mainThread = true)
    fun lookAtBlock(): MethodResult {
        automataCore!!.addRotationCycle()
        val owner = automataCore.peripheralOwner
        val result = owner.withPlayer { APFakePlayer -> APFakePlayer.findHit(skipEntity = true, skipBlock = false) }
        if (result.type == HitResult.Type.MISS) return MethodResult.of(null, "No block find")
        val blockHit = result as BlockHitResult
        val state = owner.level!!.getBlockState(blockHit.blockPos)
        val data: MutableMap<String, Any?> = HashMap()
        val blockName = state.block.builtInRegistryHolder().key().registry()
        if (blockName != null) data["name"] = blockName.toString()
        data["tags"] = LuaConverter.tagsToList(state.block.builtInRegistryHolder().tags())
        return MethodResult.of(null)
    }

    @LuaFunction(mainThread = true)
    fun lookAtEntity(): MethodResult {
        automataCore!!.addRotationCycle()
        val result = automataCore.peripheralOwner.withPlayer { APFakePlayer -> APFakePlayer.findHit(
            skipEntity = false,
            skipBlock = true
        ) }
        if (result.type == HitResult.Type.MISS) {
            return MethodResult.of(null, "No entity find")
        }
        val entityHit = result as EntityHitResult
        return MethodResult.of(LuaConverter.entityToLua(entityHit.getEntity()))
    }
}