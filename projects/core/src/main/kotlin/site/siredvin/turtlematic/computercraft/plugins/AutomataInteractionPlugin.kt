package site.siredvin.turtlematic.computercraft.plugins

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Pose
import net.minecraft.world.item.ItemStack
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.computercraft.peripheral.automatas.BaseAutomataCorePeripheral
import site.siredvin.turtlematic.util.getInteractionMode
import site.siredvin.turtlematic.util.optPose
import site.siredvin.turtlematic.util.optVerticalDirection
import site.siredvin.tweakium.modules.peripheral.api.IPeripheralOperation
import site.siredvin.tweakium.modules.peripheral.api.InteractionMode
import site.siredvin.tweakium.modules.peripheral.owner.TurtlePeripheralOwner
import java.util.function.Predicate

class AutomataInteractionPlugin(
    automataCore: BaseAutomataCorePeripheral,
    private val suitableEntity: Predicate<Entity> = Predicate { false },
    private val allowedMods: Set<InteractionMode> = setOf(InteractionMode.BLOCK),
) : AutomataCorePlugin(automataCore) {
    override val operations: List<IPeripheralOperation<*>>
        get() = listOf(SingleOperation.SWING, SingleOperation.USE)

    fun swingImplInner(arguments: IArguments): MethodResult {
        val mode = arguments.getInteractionMode(0, allowedMods)
        val overwrittenDirection = arguments.optVerticalDirection(1)
        val overwrittenPose = arguments.optPose(2)
        if (!allowedMods.contains(mode)) {
            return MethodResult.of(null, "Mode $mode are not allowed for this core")
        }
        automataCore.addRotationCycle()
        val owner = automataCore.peripheralOwner
        val result = owner.withPlayer({
            if (overwrittenPose != null) {
                it.fakePlayer.pose = overwrittenPose
            } else {
                it.fakePlayer.pose = Pose.STANDING
            }
            it.swing(skipEntity = mode.skipEntry, skipBlock = mode.skipBlock, entityFilter = suitableEntity)
        }, overwrittenDirection = overwrittenDirection?.minecraftDirection)
        if (!result.first) {
            return MethodResult.of(null, result.second)
        }
        return MethodResult.of(true)
    }

    fun swingImpl(arguments: IArguments): MethodResult = automataCore.withOperation(SingleOperation.SWING) {
        val owner: TurtlePeripheralOwner = automataCore.peripheralOwner
        val selectedTool: ItemStack = owner.toolInMainHand
        val previousDamageValue = selectedTool.damageValue
        val result = swingImplInner(arguments)
        if (automataCore.tier.needRestoreDurability() && selectedTool.isDamageableItem && selectedTool.damageValue != previousDamageValue) {
            selectedTool.damageValue = previousDamageValue
        }
        result
    }

    fun useImplInner(arguments: IArguments): MethodResult {
        val mode = arguments.getInteractionMode(0, allowedMods)
        val overwrittenDirection = arguments.optVerticalDirection(1)
        val overwrittenPose = arguments.optPose(2)
        if (!allowedMods.contains(mode)) {
            return MethodResult.of(null, "Mode $mode are not allowed for this core")
        }
        automataCore.addRotationCycle()
        val owner = automataCore.peripheralOwner
        return owner.withPlayer({
            if (overwrittenPose != null) {
                it.fakePlayer.pose = overwrittenPose
            } else {
                it.fakePlayer.pose = Pose.STANDING
            }
            MethodResult.of(true, it.use(skipEntity = mode.skipEntry, skipBlock = mode.skipBlock, entityFilter = suitableEntity).toString())
        }, overwrittenDirection = overwrittenDirection?.minecraftDirection)
    }

    fun useImpl(arguments: IArguments): MethodResult = automataCore.withOperation(SingleOperation.USE) {
        val owner: TurtlePeripheralOwner = automataCore.peripheralOwner
        val selectedTool: ItemStack = owner.toolInMainHand
        val previousDamageValue = selectedTool.damageValue
        val result = useImplInner(arguments)
        if (automataCore.tier.needRestoreDurability() && selectedTool.isDamageableItem && selectedTool.damageValue != previousDamageValue) {
            selectedTool.damageValue = previousDamageValue
        }
        result
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun swing(arguments: IArguments): MethodResult = swingImpl(arguments)

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun use(arguments: IArguments): MethodResult = useImpl(arguments)
}
