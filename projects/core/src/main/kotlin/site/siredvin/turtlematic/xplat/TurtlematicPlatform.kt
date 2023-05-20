package site.siredvin.turtlematic.xplat

import dan200.computercraft.api.pocket.IPocketUpgrade
import dan200.computercraft.api.pocket.PocketUpgradeDataProvider
import dan200.computercraft.api.pocket.PocketUpgradeSerialiser
import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.turtle.TurtleUpgradeDataProvider
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import dan200.computercraft.api.upgrades.UpgradeDataProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import site.siredvin.peripheralium.common.items.DescriptiveBlockItem
import site.siredvin.turtlematic.TurtlematicCore
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Supplier

interface TurtlematicPlatform {
    companion object {
        private var _IMPL: TurtlematicPlatform? = null
        val ITEMS: MutableList<Supplier<Item>> = mutableListOf()

        fun configure(impl: TurtlematicPlatform) {
            _IMPL = impl
        }

        private fun get(): TurtlematicPlatform {
            if (_IMPL == null)
                throw IllegalStateException("You should init Turtlematic Platform first")
            return _IMPL!!
        }

        fun <T: Item> registerItem(key: ResourceLocation, item: Supplier<T>): Supplier<T> {
            val registeredItem = get().registerItem(key, item)
            ITEMS.add(registeredItem as Supplier<Item>)
            return registeredItem
        }

        fun <T: Item> registerItem(name: String, item: Supplier<T>): Supplier<T> {
            return registerItem(ResourceLocation(TurtlematicCore.MOD_ID, name), item)
        }

        fun <V: Entity, T: EntityType<V>> registerEntity(key: ResourceLocation, entityTypeSup: Supplier<T>): Supplier<T> {
            return get().registerEntity(key, entityTypeSup)
        }

        fun <V: ITurtleUpgrade> registerTurtleUpgrade(
            key: ResourceLocation,
            serializer: TurtleUpgradeSerialiser<V>,
            dataGenerator: BiFunction<TurtleUpgradeDataProvider, TurtleUpgradeSerialiser<V>, UpgradeDataProvider.Upgrade<TurtleUpgradeSerialiser<*>>>,
            postRegistrationHooks: List<Consumer<Supplier<TurtleUpgradeSerialiser<V>>>>
        ) {
            get().registerTurtleUpgrade(key, serializer, dataGenerator, postRegistrationHooks)
        }
    }

    fun <T: Item> registerItem(key: ResourceLocation, item: Supplier<T>): Supplier<T>

    fun <V: Entity, T: EntityType<V>> registerEntity(key: ResourceLocation, entityTypeSup: Supplier<T>): Supplier<T>

    fun <V: ITurtleUpgrade> registerTurtleUpgrade(
        key: ResourceLocation,
        serializer: TurtleUpgradeSerialiser<V>,
        dataGenerator: BiFunction<TurtleUpgradeDataProvider, TurtleUpgradeSerialiser<V>, UpgradeDataProvider.Upgrade<TurtleUpgradeSerialiser<*>>>,
        postRegistrationHooks: List<Consumer<Supplier<TurtleUpgradeSerialiser<V>>>>
    )
}
