package site.siredvin.turtlematic.fabric

import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import site.siredvin.turtlematic.xplat.TurtlematicPlatform
import java.util.function.Supplier

object FabricTurtlematicPlatform : TurtlematicPlatform {
    override fun <T : Item> registerItem(key: ResourceLocation, item: Supplier<T>): Supplier<T> {
        val registeredItem = Registry.register(BuiltInRegistries.ITEM, key, item.get())
        return Supplier { registeredItem }
    }

    override fun <V : Entity, T : EntityType<V>> registerEntity(key: ResourceLocation, entityTypeSup: Supplier<T>): Supplier<T> {
        val registeredEntityType = Registry.register(BuiltInRegistries.ENTITY_TYPE, key, entityTypeSup.get())
        return Supplier { registeredEntityType }
    }

    override fun registerCreativeTab(key: ResourceLocation, tab: CreativeModeTab): Supplier<CreativeModeTab> {
        val registeredTab = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab)
        return Supplier { registeredTab }
    }

    override fun <V : ITurtleUpgrade> registerTurtleUpgrade(
        key: ResourceLocation,
        serializer: TurtleUpgradeSerialiser<V>,
    ): Supplier<TurtleUpgradeSerialiser<V>> {
        val registry: Registry<TurtleUpgradeSerialiser<*>> = (
            BuiltInRegistries.REGISTRY.get(TurtleUpgradeSerialiser.registryId().location())
                ?: throw IllegalStateException("Something is not correct with turtle registry")
            ) as Registry<TurtleUpgradeSerialiser<*>>
        val registered = Registry.register(registry, key, serializer)
        return Supplier { registered }
    }
}
