package site.siredvin.turtlematic.xplat

import dan200.computercraft.api.pocket.IPocketUpgrade
import dan200.computercraft.api.pocket.PocketUpgradeSerialiser
import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.turtle.TurtleUpgradeDataProvider
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import dan200.computercraft.api.upgrades.UpgradeDataProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.CreativeModeTab
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
        private val TURTLE_UPGRADES: MutableList<Supplier<TurtleUpgradeSerialiser<out ITurtleUpgrade>>> = mutableListOf()

        val holder: ModInformationHolder
            get() = get()

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

        fun registerCreativeTab(key: ResourceLocation, tab: CreativeModeTab): Supplier<CreativeModeTab> {
            return get().registerCreativeTab(key, tab)
        }

        fun <V : Entity, T : EntityType<V>> registerEntity(key: ResourceLocation, entityTypeSup: Supplier<T>): Supplier<T> {
            return get().registerEntity(key, entityTypeSup)
        }

        fun <V : ITurtleUpgrade> registerTurtleUpgrade(
            name: String,
            serializer: TurtleUpgradeSerialiser<V>,
        ): Supplier<TurtleUpgradeSerialiser<V>> {
            return registerTurtleUpgrade(ResourceLocation(TurtlematicCore.MOD_ID, name), serializer)
        }

        fun <V : ITurtleUpgrade> registerTurtleUpgrade(
            key: ResourceLocation,
            serializer: TurtleUpgradeSerialiser<V>,
        ): Supplier<TurtleUpgradeSerialiser<V>> {
            val registered = get().registerTurtleUpgrade(key, serializer)
            TURTLE_UPGRADES.add(registered as Supplier<TurtleUpgradeSerialiser<out ITurtleUpgrade>>)
            return registered
        }
    }

    override val blocks: List<Supplier<out Block>>
        get() = emptyList()

    override val items: List<Supplier<out Item>>
        get() = ITEMS

    override val turtleSerializers: List<Supplier<TurtleUpgradeSerialiser<out ITurtleUpgrade>>>
        get() = TURTLE_UPGRADES

    override val pocketSerializers: List<Supplier<PocketUpgradeSerialiser<out IPocketUpgrade>>>
        get() = emptyList()

    fun <T : Item> registerItem(key: ResourceLocation, item: Supplier<T>): Supplier<T>

    fun registerCreativeTab(key: ResourceLocation, tab: CreativeModeTab): Supplier<CreativeModeTab>

    fun <V : Entity, T : EntityType<V>> registerEntity(key: ResourceLocation, entityTypeSup: Supplier<T>): Supplier<T>

    fun <V : ITurtleUpgrade> registerTurtleUpgrade(
        key: ResourceLocation,
        serializer: TurtleUpgradeSerialiser<V>,
    ): Supplier<TurtleUpgradeSerialiser<V>>
}
