package site.siredvin.turtlematic

import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.NewRegistryEvent
import site.siredvin.peripheralium.ForgePeripheralium
import site.siredvin.turtlematic.common.configuration.ConfigHolder
import site.siredvin.turtlematic.forge.ForgeModRecipeIngredients
import site.siredvin.turtlematic.forge.ForgeTurtlematicPlatform
import site.siredvin.turtlematic.xplat.TurtlematicCommonHooks
import thedarkcolour.kotlinforforge.forge.MOD_CONTEXT

@Mod(TurtlematicCore.MOD_ID)
@Mod.EventBusSubscriber(modid = TurtlematicCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
object ForgeTurtlematic {

    val itemsRegistry: DeferredRegister<Item> =
        DeferredRegister.create(ForgeRegistries.ITEMS, TurtlematicCore.MOD_ID)
    val entityTypesRegistry: DeferredRegister<EntityType<*>> =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TurtlematicCore.MOD_ID)
    val creativeTabRegistry: DeferredRegister<CreativeModeTab> = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), TurtlematicCore.MOD_ID)
    val turtleSerializers: DeferredRegister<TurtleUpgradeSerialiser<*>> = DeferredRegister.create(
        TurtleUpgradeSerialiser.registryId(),
        TurtlematicCore.MOD_ID,
    )

    init {
        ForgePeripheralium.sayHi()
        // Configure configuration
        val context = ModLoadingContext.get()
        context.registerConfig(ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC, "${TurtlematicCore.MOD_ID}.toml")

        TurtlematicCore.configure(ForgeTurtlematicPlatform, ForgeModRecipeIngredients)
        val eventBus = MOD_CONTEXT.getKEventBus()
        eventBus.addListener(this::commonSetup)
        eventBus.addListener(this::registrySetup)
        // Register items and blocks
        TurtlematicCommonHooks.onRegister()
        itemsRegistry.register(eventBus)
        entityTypesRegistry.register(eventBus)
        turtleSerializers.register(eventBus)
    }

    fun commonSetup(event: FMLCommonSetupEvent) {
        // Load all integrations
        TurtlematicCommonHooks.commonSetup()
    }

    fun registrySetup(event: NewRegistryEvent) {
    }
}
