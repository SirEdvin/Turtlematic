package site.siredvin.turtlematic.computercraft.plugins

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import site.siredvin.peripheralium.api.datatypes.InteractionMode
import site.siredvin.peripheralium.api.datatypes.VerticalDirection
import site.siredvin.peripheralium.api.peripheral.IPeripheralCheck
import site.siredvin.peripheralium.api.peripheral.IPeripheralFunction
import site.siredvin.peripheralium.api.peripheral.IPeripheralOperation
import site.siredvin.peripheralium.util.representation.LuaRepresentation
import site.siredvin.peripheralium.util.world.FakePlayerProxy
import site.siredvin.peripheralium.xplat.PeripheraliumPlatform
import site.siredvin.peripheralium.xplat.XplatRegistries
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.computercraft.peripheral.automatas.BaseAutomataCorePeripheral
import site.siredvin.turtlematic.tags.BlockTags
import site.siredvin.turtlematic.tags.EntityTags
import java.util.function.Predicate

class AutomataCapturePlugin(
    automataCore: BaseAutomataCorePeripheral,
    private val allowedMods: Set<InteractionMode>,
    private val suitableEntity: Predicate<Entity> = Predicate { false },
) : AutomataCorePlugin(automataCore) {
    override val operations: List<IPeripheralOperation<*>>
        get() = listOf(SingleOperation.CAPTURE)

    protected val isFilled: Boolean
        get() = !automataCore.peripheralOwner.dataStorage.getCompound(STORED_OBJECT_NBT_KEY).isEmpty

    protected fun saveSomething(data: CompoundTag, type: InteractionMode) {
        automataCore.peripheralOwner.dataStorage.put(STORED_OBJECT_NBT_KEY, data)
        automataCore.peripheralOwner.dataStorage.putString(STORED_OBJECT_TYPE_NBT_KEY, type.toString())
    }

    protected val storedData: CompoundTag
        get() = automataCore.peripheralOwner.dataStorage.getCompound(STORED_OBJECT_NBT_KEY)

    protected val storedType: InteractionMode?
        get() = InteractionMode.optValueOf(automataCore.peripheralOwner.dataStorage.getString(STORED_OBJECT_TYPE_NBT_KEY))

    protected fun clear() {
        automataCore.peripheralOwner.dataStorage.remove(STORED_OBJECT_NBT_KEY)
        automataCore.peripheralOwner.dataStorage.remove(STORED_OBJECT_TYPE_NBT_KEY)
    }

    protected fun extractEntity(): Entity? {
        if (storedType != InteractionMode.ENTITY) {
            return null
        }
        val data: CompoundTag = storedData
        val type: EntityType<*>? = EntityType.byString(data.getString("entity")).orElse(null)
        if (type != null) {
            val entity: Entity = type.create(automataCore.peripheralOwner.level!!) ?: return null
            entity.load(data)
            return entity
        }
        return null
    }

    protected fun captureEntity(hit: EntityHitResult): MethodResult {
        return automataCore.withOperation(
            SingleOperation.CAPTURE,
            IPeripheralFunction {
                val entity = hit.entity
                if (entity is Player || !entity.isAlive) {
                    return@IPeripheralFunction MethodResult.of(null, "Unsuitable entity")
                }
                if (entity.type.`is`(EntityTags.CAPTURE_BLACKLIST)) {
                    return@IPeripheralFunction MethodResult.of(null, "Entity in blacklist")
                }
                val nbt = CompoundTag()
                nbt.putString("entity", EntityType.getKey(entity.type).toString())
                entity.saveWithoutId(nbt)
                entity.remove(Entity.RemovalReason.CHANGED_DIMENSION)
                saveSomething(nbt, InteractionMode.ENTITY)
                MethodResult.of(true)
            },
            IPeripheralCheck {
                if (isFilled) return@IPeripheralCheck MethodResult.of(null, "Something else already captured")
                null
            },
        )
    }

    protected fun extractBlock(): Pair<BlockState, CompoundTag>? {
        if (storedType != InteractionMode.BLOCK) {
            return null
        }
        val data: CompoundTag = storedData
        val blockState = NbtUtils.readBlockState(XplatRegistries.BLOCKS, data.getCompound("state"))
        if (blockState.isAir) {
            return null
        }
        return Pair(blockState, data.getCompound("nbt"))
    }

    protected fun captureBlock(hit: BlockHitResult): MethodResult {
        return automataCore.withOperation(
            SingleOperation.CAPTURE,
            {
                val owner = automataCore.peripheralOwner
                val level = owner.level!!
                val state = level.getBlockState(hit.blockPos)
                if (owner.withPlayer({ PeripheraliumPlatform.isBlockProtected(hit.blockPos, state, it) })) {
                    return@withOperation MethodResult.of(null, "Block is protected")
                }
                if (state.`is`(BlockTags.CAPTURE_BLACKLIST)) {
                    return@withOperation MethodResult.of(null, "Block is in blacklist")
                }
                val serializedData = CompoundTag()
                serializedData.put("state", NbtUtils.writeBlockState(state))
                val entity = level.getBlockEntity(hit.blockPos)
                if (entity != null) {
                    serializedData.put("nbt", entity.saveWithoutMetadata())
                }
                saveSomething(serializedData, InteractionMode.BLOCK)
                level.setBlockAndUpdate(hit.blockPos, Blocks.AIR.defaultBlockState())
                MethodResult.of(true)
            },
            IPeripheralCheck {
                if (isFilled) return@IPeripheralCheck MethodResult.of(null, "Something else already captured")
                null
            },
        )
    }

    protected fun releaseEntity(): MethodResult {
        val owner = automataCore.peripheralOwner
        val extractedEntity = extractEntity() ?: return MethodResult.of(null, "Problem with entity unpacking")
        val blockPos = owner.pos.offset(owner.facing.normal)
        extractedEntity.absMoveTo(blockPos.x + 0.5, blockPos.y.toDouble(), blockPos.z + 0.5, 0f, 0f)
        clear()
        owner.level!!.addFreshEntity(extractedEntity)
        return MethodResult.of(true)
    }

    protected fun releaseBlock(): MethodResult {
        val owner = automataCore.peripheralOwner
        val level = owner.level!!
        val extractedBlock = extractBlock() ?: return MethodResult.of(null, "Problem with block unpacking")
        val pos = owner.pos.offset(owner.facing.normal)
        if (!level.isEmptyBlock(pos)) {
            return MethodResult.of(null, "Target area should be empty")
        }
        val isProtected = owner.withPlayer(
            { PeripheraliumPlatform.isBlockProtected(pos, level.getBlockState(pos), it) },
        )
        if (isProtected) {
            return MethodResult.of(null, "This block is protected")
        }
        level.setBlockAndUpdate(pos, extractedBlock.first)
        val entity = level.getBlockEntity(pos)
        if (entity != null && !extractedBlock.second.isEmpty) {
            entity.load(extractedBlock.second)
        }
        clear()
        return MethodResult.of(true)
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun capture(arguments: IArguments): MethodResult {
        val mode = InteractionMode.luaValueOf(arguments.getString(0), allowedMods)
        val directionArgument = arguments.optString(1)
        val overwrittenDirection = if (directionArgument.isEmpty) {
            null
        } else {
            VerticalDirection.luaValueOf(
                directionArgument.get(),
            )
        }
        val hit = automataCore.peripheralOwner.withPlayer({
            FakePlayerProxy(it).findHit(
                skipEntity = mode.skipEntry,
                skipBlock = mode.skipBlock,
                entityFilter = suitableEntity,
            )
        }, overwrittenDirection = overwrittenDirection?.minecraftDirection)
        return when (hit.type) {
            HitResult.Type.MISS -> MethodResult.of(null, "nothing found")
            HitResult.Type.BLOCK -> captureBlock(hit as BlockHitResult)
            HitResult.Type.ENTITY -> captureEntity(hit as EntityHitResult)
            null -> throw LuaException("This should never, never happen at all")
        }
    }

    @LuaFunction(mainThread = true)
    fun release(): MethodResult {
        if (!isFilled) return MethodResult.of(null, "Nothing is stored")
        return when (storedType) {
            InteractionMode.BLOCK -> releaseBlock()
            InteractionMode.ENTITY -> releaseEntity()
            InteractionMode.ANY, null -> throw LuaException("This is pretty impossible to occur, how is this?")
        }
    }

    @get:LuaFunction(mainThread = true)
    val captured: MethodResult
        get() {
            return when (storedType) {
                InteractionMode.ENTITY -> {
                    val entity = extractEntity()!!
                    val base = LuaRepresentation.forEntity(entity)
                    val tag = CompoundTag()
                    entity.saveWithoutId(tag)
                    if (!tag.isEmpty) {
                        val serializerTag = PeripheraliumPlatform.nbtToLua(tag)
                        if (serializerTag != null) {
                            base["nbt"] = serializerTag
                        }
                    }
                    MethodResult.of(base)
                }
                InteractionMode.BLOCK -> {
                    val blockData = extractBlock()!!
                    val base = LuaRepresentation.forBlockState(blockData.first)
                    if (!blockData.second.isEmpty) {
                        val serializerTag = PeripheraliumPlatform.nbtToLua(blockData.second)
                        if (serializerTag != null) {
                            base["nbt"] = serializerTag
                        }
                    }
                    MethodResult.of(base)
                }
                InteractionMode.ANY, null -> MethodResult.of(emptyMap<String, Any>())
            }
        }

    companion object {
        private const val STORED_OBJECT_NBT_KEY = "storedObject"
        private const val STORED_OBJECT_TYPE_NBT_KEY = "storedObjectType"
    }
}
