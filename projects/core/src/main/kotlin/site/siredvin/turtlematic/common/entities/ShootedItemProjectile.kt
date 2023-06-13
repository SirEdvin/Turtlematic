package site.siredvin.turtlematic.common.entities

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.Containers
import net.minecraft.world.entity.projectile.ItemSupplier
import net.minecraft.world.entity.projectile.ThrowableProjectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.Vec3
import site.siredvin.peripheralium.api.storage.ExtractorProxy
import site.siredvin.peripheralium.api.storage.StorageUtils
import site.siredvin.peripheralium.ext.toBlockPos
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.common.setup.EntityTypes

class ShootedItemProjectile(level: Level, x: Double, y: Double, z: Double) : ThrowableProjectile(EntityTypes.SHOOTED_ITEM_TYPE.get(), x, y, z, level), ItemSupplier {

    constructor(level: Level) : this(level, 0.0, 0.0, 0.0)

    companion object {
        private val DATA_ITEM_STACK: EntityDataAccessor<ItemStack> = SynchedEntityData.defineId(ShootedItemProjectile::class.java, EntityDataSerializers.ITEM_STACK)
        private const val DEFAULT_DECAY_TICKS = 10
    }

    var stack: ItemStack
        get() = getEntityData().get(DATA_ITEM_STACK) as ItemStack
        set(value) {
            entityData.set(DATA_ITEM_STACK, value.copy())
        }

    private var decayTicker: Int = DEFAULT_DECAY_TICKS
    private var startDecaying: Boolean = false

    override fun setPos(x: Double, y: Double, z: Double) {
        super.setPos(x, y, z)
        if (!level().isClientSide) {
            TurtlematicCore.LOGGER.info("New position $x $y $z")
        }
    }

    override fun onHitBlock(hit: BlockHitResult) {
        if (!level().isClientSide) {
            TurtlematicCore.LOGGER.info("Hit block ${hit.blockPos}")
            val targetableStorage =
                ExtractorProxy.extractTargetableStorage(level(), hit.blockPos, level().getBlockEntity(hit.blockPos))
            if (targetableStorage != null) {
                this.kill()
                StorageUtils.toInventoryOrToWorld(stack, targetableStorage, hit.blockPos, level())
                return
            }
        }
        super.onHitBlock(hit)
        if (deltaMovement.length() != 0.0) {
            deltaMovement = Vec3.ZERO
            startDecaying = true
        }
    }

    override fun onHitEntity(hit: EntityHitResult) {
        if (!level().isClientSide) {
            val targetableStorage = ExtractorProxy.extractTargetableStorage(level(), hit.entity)
            if (targetableStorage != null) {
                this.kill()
                StorageUtils.toInventoryOrToWorld(stack, targetableStorage, hit.location.toBlockPos(), level())
                return
            }
        }
        super.onHitEntity(hit)
    }

    override fun tick() {
        super.tick()
        if (!level().isClientSide) {
            if (deltaMovement.length() != 0.0) {
                TurtlematicCore.LOGGER.info("Current delta of movement $deltaMovement")
            } else if (startDecaying) {
                decayTicker--
                if (decayTicker <= 0) {
                    TurtlematicCore.LOGGER.info("Dropping item stack $x ${y + 0.5} $z")
                    Containers.dropItemStack(level(), x, y + 0.5, z, stack)
                    this.kill()
                }
            }
        }
    }

    override fun defineSynchedData() {
        getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY)
    }

    override fun isNoGravity(): Boolean {
        return super.isNoGravity() || deltaMovement.length() == 0.0
    }

    override fun addAdditionalSaveData(tag: CompoundTag) {
        super.addAdditionalSaveData(tag)
        val stack = this.stack
        if (!stack.isEmpty) {
            tag.put("Item", stack.save(CompoundTag()))
        }
    }

    override fun readAdditionalSaveData(tag: CompoundTag) {
        super.readAdditionalSaveData(tag)
        stack = ItemStack.of(tag.getCompound("Item"))
    }

    override fun getItem(): ItemStack {
        return stack
    }
}
