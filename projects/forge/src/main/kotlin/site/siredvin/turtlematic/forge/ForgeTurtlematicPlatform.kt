package site.siredvin.turtlematic.forge

import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.turtle.TurtleUpgradeDataProvider
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import dan200.computercraft.api.upgrades.UpgradeDataProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import site.siredvin.turtlematic.ForgeTurtlematic
import site.siredvin.turtlematic.data.ModTurtleUpgradeDataProvider
import site.siredvin.turtlematic.xplat.TurtlematicPlatform
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Supplier

object ForgeTurtlematicPlatform: TurtlematicPlatform {
    override fun <T : Item> registerItem(key: ResourceLocation, item: Supplier<T>): Supplier<T> {
        return ForgeTurtlematic.itemsRegistry.register(key.path, item)
    }

    override fun <V : Entity, T : EntityType<V>> registerEntity(
        key: ResourceLocation,
        entityTypeSup: Supplier<T>
    ): Supplier<T> {
        return ForgeTurtlematic.entityTypesRegistry.register(key.path, entityTypeSup)
    }

    override fun <V : ITurtleUpgrade> registerTurtleUpgrade(
        key: ResourceLocation,
        serializer: TurtleUpgradeSerialiser<V>,
        dataGenerator: BiFunction<TurtleUpgradeDataProvider, TurtleUpgradeSerialiser<V>, UpgradeDataProvider.Upgrade<TurtleUpgradeSerialiser<*>>>,
        postRegistrationHooks: List<Consumer<Supplier<TurtleUpgradeSerialiser<V>>>>
    ) {
        val turtleUpgrade = ForgeTurtlematic.turtleSerializers.register(key.path) { serializer }
        ModTurtleUpgradeDataProvider.hookUpgrade { dataGenerator.apply(it, turtleUpgrade.get()) }
        postRegistrationHooks.forEach { it.accept(turtleUpgrade) }
    }
}