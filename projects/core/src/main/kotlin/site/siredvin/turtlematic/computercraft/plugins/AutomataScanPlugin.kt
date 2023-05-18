package site.siredvin.turtlematic.computercraft.plugins

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.block.state.BlockState
import site.siredvin.peripheralium.api.datatypes.AreaInteractionMode
import site.siredvin.peripheralium.api.peripheral.IPeripheralOperation
import site.siredvin.peripheralium.computercraft.operations.SphereOperationContext
import site.siredvin.peripheralium.extra.plugins.AbstractScanningPlugin
import site.siredvin.turtlematic.computercraft.operations.SphereOperation
import site.siredvin.turtlematic.computercraft.peripheral.automatas.BaseAutomataCorePeripheral
import java.util.function.BiConsumer
import java.util.function.Predicate

class AutomataScanPlugin(
    private val automataCore: BaseAutomataCorePeripheral,
    override val entityEnriches: List<BiConsumer<Entity, MutableMap<String, Any>>> = emptyList(),
    override val blockStateEnriches: List<BiConsumer<BlockState, MutableMap<String, Any>>> = emptyList(),
    override val itemEnriches: List<BiConsumer<ItemEntity, MutableMap<String, Any>>> = emptyList(),
    override val suitableEntity: Predicate<Entity> = Predicate { false },
    override val allowedMods: Set<AreaInteractionMode> = setOf(AreaInteractionMode.ITEM)
) : AbstractScanningPlugin(automataCore.peripheralOwner) {
    override val scanBlocksOperation: IPeripheralOperation<SphereOperationContext> = SphereOperation.SCAN_BLOCKS
    override val scanEntitiesOperation: IPeripheralOperation<SphereOperationContext> = SphereOperation.SCAN_ENTITIES
    override val scanItemsOperation: IPeripheralOperation<SphereOperationContext> = SphereOperation.SCAN_ITEMS
    override val scanRadius: Int
        get() = automataCore.interactionRadius
}