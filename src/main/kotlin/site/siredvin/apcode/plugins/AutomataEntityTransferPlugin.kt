package site.siredvin.apcode.plugins

import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import site.siredvin.lib.operations.SingleOperation
import site.siredvin.lib.peripherals.BaseAutomataCorePeripheral
import site.siredvin.lib.peripherals.IPeripheralOperation
import site.siredvin.lib.peripherals.owner.TurtlePeripheralOwner
import site.siredvin.lib.util.LuaConverter
import java.util.function.Predicate

class AutomataEntityTransferPlugin(
    automataCore: BaseAutomataCorePeripheral?,
    private val suitableEntity: Predicate<Entity>
) : AutomataCorePlugin(automataCore) {
    override val operations: Array<IPeripheralOperation<*>>
        get() = arrayOf(SingleOperation.CAPTURE_ANIMAL)
    protected val isEntityInside: Boolean
        get() = !automataCore!!.peripheralOwner.dataStorage.getCompound(ENTITY_NBT_KEY).isEmpty

    protected fun saveEntity(data: CompoundTag?) {
        automataCore!!.peripheralOwner.dataStorage.put(ENTITY_NBT_KEY, data)
    }

    protected val entity: CompoundTag
        get() = automataCore!!.peripheralOwner.dataStorage.getCompound(ENTITY_NBT_KEY)

    protected fun removeEntity() {
        automataCore!!.peripheralOwner.dataStorage.remove(ENTITY_NBT_KEY)
    }

    protected fun extractEntity(): Entity? {
        val data: CompoundTag = entity
        val type: EntityType<*>? = EntityType.byString(data.getString("entity")).orElse(null)
        if (type != null) {
            val entity: Entity = type.create(automataCore!!.peripheralOwner.level!!) ?: return null
            entity.load(data)
            return entity
        }
        return null
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun captureAnimal(): MethodResult {
        // TODO: Fix after user appiear
//        val entityHit: HitResult =
//            automataCore!!.peripheralOwner.withPlayer { player -> player.findHit(false, true, suitableEntity) }
//        return if (entityHit.getType() == HitResult.Type.MISS) MethodResult.of(
//            null,
//            "Nothing found"
//        ) else automataCore.withOperation(
//            CAPTURE_ANIMAL,
//            IPeripheralFunction<SingleOperationContext, MethodResult> { context: SingleOperationContext? ->
//                val entity: LivingEntity = (entityHit as EntityHitResult).getEntity() as LivingEntity
//                if (entity is Player || !entity.isAlive()) return@withOperation MethodResult.of(
//                    null,
//                    "Unsuitable entity"
//                )
//                val nbt = CompoundTag()
//                nbt.putString("entity", EntityType.getKey(entity.getType()).toString())
//                entity.saveWithoutId(nbt)
//                entity.remove(Entity.RemovalReason.CHANGED_DIMENSION)
//                saveEntity(nbt)
//                MethodResult.of(true)
//            },
//            IPeripheralCheck<SingleOperationContext> { context: SingleOperationContext? ->
//                if (isEntityInside) return@withOperation MethodResult.of(null, "Another entity already captured")
//                null
//            })
        return MethodResult.of(null, "Nothing found")
    }

    @LuaFunction(mainThread = true)
    fun releaseAnimal(): MethodResult {
        if (!isEntityInside) return MethodResult.of(null, "No entity is stored")
        val owner: TurtlePeripheralOwner = automataCore!!.peripheralOwner
        automataCore.addRotationCycle()
        val extractedEntity = extractEntity() ?: return MethodResult.of(null, "Problem with entity unpacking")
        val blockPos: BlockPos = owner.pos.offset(owner.facing.getNormal())
        extractedEntity.absMoveTo(blockPos.getX() + 0.5, blockPos.getY().toDouble(), blockPos.getZ() + 0.5, 0f, 0f)
        removeEntity()
        owner.level!!.addFreshEntity(extractedEntity)
        return MethodResult.of(true)
    }

    @get:LuaFunction(mainThread = true)
    val capturedAnimal: MethodResult
        get() {
            val extractedEntity = extractEntity()
            return MethodResult.of(
                LuaConverter.completeEntityToLua(
                    extractedEntity,
                    automataCore!!.peripheralOwner.toolInMainHand
                )
            )
        }

    companion object {
        private const val ENTITY_NBT_KEY = "storedEntity"
    }
}