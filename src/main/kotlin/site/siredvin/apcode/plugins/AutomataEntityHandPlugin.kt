package site.siredvin.apcode.plugins

import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import site.siredvin.lib.operations.SingleOperation
import site.siredvin.lib.operations.SingleOperationContext
import site.siredvin.lib.peripherals.BaseAutomataCorePeripheral
import site.siredvin.lib.peripherals.IPeripheralFunction
import site.siredvin.lib.peripherals.IPeripheralOperation
import site.siredvin.lib.peripherals.owner.TurtlePeripheralOwner
import site.siredvin.lib.util.LuaConverter
import java.util.ArrayList
import java.util.function.Predicate

class AutomataEntityHandPlugin(automataCore: BaseAutomataCorePeripheral, private val suitableEntity: Predicate<Entity>) :
    AutomataCorePlugin(automataCore) {
    override val operations: Array<IPeripheralOperation<*>>
        get() = arrayOf(SingleOperation.USE_ON_ANIMAL)

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun useOnAnimal(): MethodResult {
        return automataCore!!.withOperation(
            SingleOperation.USE_ON_ANIMAL,
            IPeripheralFunction<SingleOperationContext, MethodResult> {
                val owner: TurtlePeripheralOwner = automataCore.peripheralOwner
                val selectedTool: ItemStack = owner.toolInMainHand
                val previousDamageValue: Int = selectedTool.damageValue
                val result: InteractionResult =
                    owner.withPlayer { player -> player.useOnFilteredEntity(suitableEntity) }
                if (automataCore.hasAttribute(BaseAutomataCorePeripheral.ATTR_STORING_TOOL_DURABILITY))
                    selectedTool.damageValue = previousDamageValue
                MethodResult.of(true, result.toString())
            })
    }

    @LuaFunction(mainThread = true)
    fun inspectAnimal(): MethodResult {
        automataCore!!.addRotationCycle()
        val owner: TurtlePeripheralOwner = automataCore.peripheralOwner
        val entityHit = owner.withPlayer { player -> player.findHit(false,
            skipBlock = true,
            entityFilter = suitableEntity
        ) }
        if (entityHit.type == HitResult.Type.MISS) return MethodResult.of(null, "Nothing found")
        val entity: Entity = (entityHit as EntityHitResult).entity
        return if (entity !is Animal) MethodResult.of(
            null,
            "Well, entity is not animal entity, but how?"
        ) else MethodResult.of(LuaConverter.animalToLua(entity as Animal, owner.toolInMainHand))
    }

    @LuaFunction(mainThread = true)
    fun searchAnimals(): MethodResult {
        automataCore!!.addRotationCycle()
        val owner: TurtlePeripheralOwner = automataCore.peripheralOwner
        val currentPos: BlockPos = owner.pos
        val box = AABB(currentPos)
        val entities: MutableList<Map<String, Any>> = ArrayList()
        val itemInHand: ItemStack = owner.toolInMainHand
        owner.level!!.getEntities(null, box.inflate(automataCore.interactionRadius.toDouble()), suitableEntity)
            .forEach { entity ->
                entities.add(
                    LuaConverter.completeEntityWithPositionToLua(
                        entity,
                        itemInHand,
                        currentPos
                    )
                )
            }
        return MethodResult.of(entities)
    }
}