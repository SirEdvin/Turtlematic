package site.siredvin.turtlematic.forge

import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.upgrades.UpgradeType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredRegister
import site.siredvin.turtlematic.ForgeTurtlematic
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.tweakium.modules.platform.ForgeInnerComputerBasePlatform
import java.util.function.Supplier

object ForgeModInnerPlatform : ForgeInnerComputerBasePlatform() {

    override val itemsRegistry: DeferredRegister<Item>
        get() = ForgeTurtlematic.itemsRegistry

    override val entityTypesRegistry: DeferredRegister<EntityType<*>>
        get() = ForgeTurtlematic.entityTypesRegistry

    override val creativeTabRegistry: DeferredRegister<CreativeModeTab>
        get() = ForgeTurtlematic.creativeTabRegistry

    override val modID: String
        get() = TurtlematicCore.MOD_ID

    override fun <V : ITurtleUpgrade> registerTurtleUpgrade(
        key: ResourceLocation,
        upgrade: UpgradeType<V>,
    ): Supplier<UpgradeType<V>> = ForgeTurtlematic.turtleUpgradesRegistry.register(key.path, Supplier { upgrade })
}
