package site.siredvin.turtlematic.forge

import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import site.siredvin.turtlematic.ForgeTurtlematic
import site.siredvin.turtlematic.xplat.TurtlematicPlatform
import java.util.function.Supplier

object ForgeTurtlematicPlatform : TurtlematicPlatform {
    override fun <T : Item> registerItem(key: ResourceLocation, item: Supplier<T>): Supplier<T> {
        return ForgeTurtlematic.itemsRegistry.register(key.path, item)
    }

    override fun <V : Entity, T : EntityType<V>> registerEntity(
        key: ResourceLocation,
        entityTypeSup: Supplier<T>,
    ): Supplier<T> {
        return ForgeTurtlematic.entityTypesRegistry.register(key.path, entityTypeSup)
    }

    override fun registerCreativeTab(key: ResourceLocation, tab: CreativeModeTab): Supplier<CreativeModeTab> {
        return ForgeTurtlematic.creativeTabRegistry.register(key.path) { tab }
    }

    override fun <V : ITurtleUpgrade> registerTurtleUpgrade(
        key: ResourceLocation,
        serializer: TurtleUpgradeSerialiser<V>,
    ): Supplier<TurtleUpgradeSerialiser<V>> {
        return ForgeTurtlematic.turtleSerializers.register(key.path) { serializer }
    }
}
