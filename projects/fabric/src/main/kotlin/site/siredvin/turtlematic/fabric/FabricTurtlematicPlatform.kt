package site.siredvin.turtlematic.fabric

import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.turtle.TurtleUpgradeDataProvider
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import dan200.computercraft.api.upgrades.UpgradeDataProvider
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import site.siredvin.turtlematic.common.RegistrationQueue
import site.siredvin.turtlematic.data.ModTurtleUpgradeDataProvider
import site.siredvin.turtlematic.xplat.TurtlematicPlatform
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Supplier

object FabricTurtlematicPlatform: TurtlematicPlatform {
    override fun <T : Item> registerItem(key: ResourceLocation, item: Supplier<T>): Supplier<T> {
        val registeredItem = Registry.register(BuiltInRegistries.ITEM, key, item.get())
        return Supplier { registeredItem }
    }

    override fun <V: Entity, T: EntityType<V>> registerEntity(key: ResourceLocation, entityTypeSup: Supplier<T>): Supplier<T> {
        val registeredEntityType = Registry.register(BuiltInRegistries.ENTITY_TYPE, key, entityTypeSup.get())
        return Supplier { registeredEntityType }
    }

    private fun <V : ITurtleUpgrade> rawTurtleUpgradeRegistration(
        registry: Registry<TurtleUpgradeSerialiser<*>>, key: ResourceLocation, serializer: TurtleUpgradeSerialiser<V>,
        dataGenerator: BiFunction<TurtleUpgradeDataProvider, TurtleUpgradeSerialiser<V>, UpgradeDataProvider.Upgrade<TurtleUpgradeSerialiser<*>>>,
        postRegistrationHooks: List<Consumer<Supplier<TurtleUpgradeSerialiser<V>>>>
    ): TurtleUpgradeSerialiser<V> {
        val registeredSerializer = Registry.register(registry, key,serializer)

        ModTurtleUpgradeDataProvider.hookUpgrade {
            dataGenerator.apply(it, registeredSerializer)
        }
        val supplier = Supplier {registeredSerializer}
        postRegistrationHooks.forEach { it.accept(supplier) }
        return registeredSerializer
    }

    override fun <V : ITurtleUpgrade> registerTurtleUpgrade(
        key: ResourceLocation,
        serializer: TurtleUpgradeSerialiser<V>,
        dataGenerator: BiFunction<TurtleUpgradeDataProvider, TurtleUpgradeSerialiser<V>, UpgradeDataProvider.Upgrade<TurtleUpgradeSerialiser<*>>>,
        postRegistrationHooks: List<Consumer<Supplier<TurtleUpgradeSerialiser<V>>>>
    ) {
        val rawTurtleRegistry = BuiltInRegistries.REGISTRY.get(TurtleUpgradeSerialiser.REGISTRY_ID.location())

        if (rawTurtleRegistry == null) {
            RegistrationQueue.scheduleTurtleUpgrade { rawTurtleUpgradeRegistration(it, key, serializer, dataGenerator, postRegistrationHooks) }

        } else {
            val turtleSerializerRegister = rawTurtleRegistry as Registry<TurtleUpgradeSerialiser<*>>
            rawTurtleUpgradeRegistration(turtleSerializerRegister, key, serializer, dataGenerator, postRegistrationHooks)
        }
    }
}