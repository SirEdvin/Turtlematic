package site.siredvin.turtlematic.forge

import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import site.siredvin.peripheralium.forge.ForgeBaseInnerPlatform
import site.siredvin.turtlematic.ForgeTurtlematic
import site.siredvin.turtlematic.TurtlematicCore

object ForgeModInnerPlatform: ForgeBaseInnerPlatform() {

    override val itemsRegistry: DeferredRegister<Item>
        get() = ForgeTurtlematic.itemsRegistry

    override val entityTypesRegistry: DeferredRegister<EntityType<*>>
        get() = ForgeTurtlematic.entityTypesRegistry

    override val creativeTabRegistry: DeferredRegister<CreativeModeTab>
        get() = ForgeTurtlematic.creativeTabRegistry

    override val turtleSerializers: DeferredRegister<TurtleUpgradeSerialiser<*>>
        get() = ForgeTurtlematic.turtleSerializers

    override val modID: String
        get() = TurtlematicCore.MOD_ID
}