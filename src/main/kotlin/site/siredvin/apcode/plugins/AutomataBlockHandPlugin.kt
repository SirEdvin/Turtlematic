package site.siredvin.apcode.plugins

import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.ItemStack
import site.siredvin.lib.operations.SingleOperation
import site.siredvin.lib.peripherals.BaseAutomataCorePeripheral
import site.siredvin.lib.peripherals.IPeripheralOperation
import site.siredvin.lib.peripherals.owner.TurtlePeripheralOwner
import site.siredvin.lib.util.LibFakePlayer
import site.siredvin.lib.util.Pair

class AutomataBlockHandPlugin(automataCore: BaseAutomataCorePeripheral) : AutomataCorePlugin(automataCore) {
    override val operations: Array<IPeripheralOperation<*>>
        get() = arrayOf(SingleOperation.DIG, SingleOperation.USE_ON_BLOCK)

//    @LuaFunction(mainThread = true)
//    @Throws(LuaException::class)
//    fun digBlock(): MethodResult {
//        return automataCore!!.withOperation(SingleOperation.DIG) {
//            val owner: TurtlePeripheralOwner = automataCore.peripheralOwner
//            val selectedTool: ItemStack = owner.toolInMainHand
//            val previousDamageValue = selectedTool.damageValue
//            val result: Pair<Boolean, String> =
//                owner.withPlayer { player -> player.digBlock(owner.facing.opposite) }
//            if (!result.left) {
//                return@withOperation MethodResult.of(null, result.right)
//            }
//            if (automataCore.hasAttribute(BaseAutomataCorePeripheral.ATTR_STORING_TOOL_DURABILITY)) selectedTool.damageValue =
//                previousDamageValue
//            MethodResult.of(true)
//        }
//    }
//
//    @LuaFunction(mainThread = true)
//    @Throws(LuaException::class)
//    fun useOnBlock(): MethodResult {
//        return automataCore!!.withOperation(SingleOperation.USE_ON_BLOCK) {
//            val owner: TurtlePeripheralOwner = automataCore.peripheralOwner
//            val selectedTool: ItemStack = owner.toolInMainHand
//            val previousDamageValue = selectedTool.damageValue
//            val result: InteractionResult = owner.withPlayer(LibFakePlayer::useOnBlock)
//            if (automataCore.hasAttribute(BaseAutomataCorePeripheral.ATTR_STORING_TOOL_DURABILITY)) selectedTool.damageValue =
//                previousDamageValue
//            MethodResult.of(true, result.toString())
//        }
//    }
}