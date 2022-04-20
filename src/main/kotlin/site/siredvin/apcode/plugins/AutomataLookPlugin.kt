package site.siredvin.apcode.plugins

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import site.siredvin.lib.peripherals.BaseAutomataCorePeripheral

class AutomataLookPlugin(automataCore: BaseAutomataCorePeripheral) : AutomataCorePlugin(automataCore) {
    @LuaFunction(mainThread = true)
    fun lookAtBlock(): MethodResult {
        // TODO: fix
//        automataCore!!.addRotationCycle()
//        val owner: TurtlePeripheralOwner = automataCore.peripheralOwner
//        val result: HitResult = owner.withPlayer { APFakePlayer -> APFakePlayer.findHit(true, false) }
//        if (result.getType() == HitResult.Type.MISS) return MethodResult.of(null, "No block find")
//        val blockHit: BlockHitResult = result as BlockHitResult
//        val state: BlockState = owner.getLevel().getBlockState(blockHit.getBlockPos())
//        val data: MutableMap<String, Any> = HashMap()
//        val blockName: ResourceLocation = state.getBlock().getRegistryName()
//        if (blockName != null) data["name"] = blockName.toString()
//        data["tags"] = LuaConverter.tagsToList(state.getBlock().getTags())
        return MethodResult.of(null)
    }

    @LuaFunction(mainThread = true)
    fun lookAtEntity(): MethodResult {
        // TODO: fix
        automataCore!!.addRotationCycle()
//        val result: HitResult =
//            automataCore.peripheralOwner.withPlayer { APFakePlayer -> APFakePlayer.findHit(false, true) }
//        if (result.getType() == HitResult.Type.MISS) {
//            return MethodResult.of(null, "No entity find")
//        }
//        val entityHit: EntityHitResult = result as EntityHitResult
//        return MethodResult.of(LuaConverter.entityToLua(entityHit.getEntity()))
        return MethodResult.of(null)
    }
}