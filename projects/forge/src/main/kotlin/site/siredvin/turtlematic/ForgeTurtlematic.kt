package site.siredvin.turtlematic

import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.upgrades.UpgradeType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.registries.DeferredRegister
import site.siredvin.peripheralium.ForgePeripheralium
import site.siredvin.turtlematic.common.configuration.ConfigHolder
import site.siredvin.turtlematic.forge.ForgeModInnerPlatform
import site.siredvin.turtlematic.forge.ForgeModRecipeIngredients
import site.siredvin.turtlematic.xplat.TurtlematicCommonHooks

@Mod(TurtlematicCore.MOD_ID)
class ForgeTurtlematic(modEventBus: IEventBus, modContainer: ModContainer) {

    companion object {
        val itemsRegistry: DeferredRegister<Item> =
            DeferredRegister.create(BuiltInRegistries.ITEM, TurtlematicCore.MOD_ID)
        val entityTypesRegistry: DeferredRegister<EntityType<*>> =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, TurtlematicCore.MOD_ID)
        val creativeTabRegistry: DeferredRegister<CreativeModeTab> =
            DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), TurtlematicCore.MOD_ID)
        val turtleUpgradesRegistry: DeferredRegister<UpgradeType<out ITurtleUpgrade>> =
            DeferredRegister.create(ITurtleUpgrade.typeRegistry(), TurtlematicCore.MOD_ID)
    }

    init {
        ForgePeripheralium.sayHi()
        // Configure configuration
        modContainer.registerConfig(ModConfig.Type.COMMON, ConfigHolder.commonConfigSpec, "${TurtlematicCore.MOD_ID}.toml")

        TurtlematicCore.configure(ForgeModInnerPlatform, ForgeModRecipeIngredients)
        modEventBus.addListener(this::commonSetup)
        // Register items and blocks
        TurtlematicCommonHooks.onRegister()
        itemsRegistry.register(modEventBus)
        entityTypesRegistry.register(modEventBus)
        creativeTabRegistry.register(modEventBus)
        turtleUpgradesRegistry.register(modEventBus)
    }

    @Suppress("UNUSED_PARAMETER")
    fun commonSetup(event: FMLCommonSetupEvent) {
        // Load all integrations
        event.enqueueWork(TurtlematicCommonHooks::commonSetup)
    }
}
