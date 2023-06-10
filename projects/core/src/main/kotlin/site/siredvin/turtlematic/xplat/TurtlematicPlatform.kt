package site.siredvin.turtlematic.xplat

import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.turtle.TurtleUpgradeDataProvider
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import dan200.computercraft.api.upgrades.UpgradeDataProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import site.siredvin.peripheralium.data.language.ModInformationHolder
import site.siredvin.turtlematic.TurtlematicCore
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Supplier

interface TurtlematicPlatform: ModInformationHolder {
    companion object {
        private var _IMPL: TurtlematicPlatform? = null
        private val ITEMS: MutableList<Supplier<Item>> = mutableListOf()
        private val TURTLE_UPGRADES: MutableList<ResourceLocation> = mutableListOf()

        val holder: ModInformationHolder
            get() = get()

        val turtleUpgrades: List<ResourceLocation>
            get() = TURTLE_UPGRADES

        fun configure(impl: TurtlematicPlatform) {
            _IMPL = impl
        }

        private fun get(): TurtlematicPlatform {
            if (_IMPL == null) {
                throw IllegalStateException("You should init Turtlematic Platform first")
            }
            return _IMPL!!
        }

        fun <T : Item> registerItem(key: ResourceLocation, item: Supplier<T>): Supplier<T> {
            val registeredItem = get().registerItem(key, item)
            ITEMS.add(registeredItem as Supplier<Item>)
            return registeredItem
        }

        fun <T : Item> registerItem(name: String, item: Supplier<T>): Supplier<T> {
            return registerItem(ResourceLocation(TurtlematicCore.MOD_ID, name), item)
        }

        fun <V : Entity, T : EntityType<V>> registerEntity(key: ResourceLocation, entityTypeSup: Supplier<T>): Supplier<T> {
            return get().registerEntity(key, entityTypeSup)
        }

        fun <V : ITurtleUpgrade> registerTurtleUpgrade(
            key: ResourceLocation,
            serializer: TurtleUpgradeSerialiser<V>,
            dataGenerator: BiFunction<TurtleUpgradeDataProvider, TurtleUpgradeSerialiser<V>, UpgradeDataProvider.Upgrade<TurtleUpgradeSerialiser<*>>>,
            postRegistrationHooks: List<Consumer<Supplier<TurtleUpgradeSerialiser<V>>>>,
        ) {
            TURTLE_UPGRADES.add(key)
            get().registerTurtleUpgrade(key, serializer, dataGenerator, postRegistrationHooks)
        }
    }

    override fun getBlocks(): List<Supplier<out Block>> {
        return emptyList()
    }

    override fun getItems(): List<Supplier<out Item>> {
        return ITEMS
    }

    fun <T : Item> registerItem(key: ResourceLocation, item: Supplier<T>): Supplier<T>

    fun <V : Entity, T : EntityType<V>> registerEntity(key: ResourceLocation, entityTypeSup: Supplier<T>): Supplier<T>

    fun <V : ITurtleUpgrade> registerTurtleUpgrade(
        key: ResourceLocation,
        serializer: TurtleUpgradeSerialiser<V>,
        dataGenerator: BiFunction<TurtleUpgradeDataProvider, TurtleUpgradeSerialiser<V>, UpgradeDataProvider.Upgrade<TurtleUpgradeSerialiser<*>>>,
        postRegistrationHooks: List<Consumer<Supplier<TurtleUpgradeSerialiser<V>>>>,
    )
}
