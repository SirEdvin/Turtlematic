package site.siredvin.lib.peripherals

import net.minecraft.world.level.block.entity.BlockEntity

abstract class BlockEntityIntegrationPeripheral<T : BlockEntity?>(entity: BlockEntity) : IntegrationPeripheral() {
    protected val blockEntity: T

    init {
        blockEntity = entity as T
    }

    override fun getTarget(): Any? {
        return blockEntity
    }
}