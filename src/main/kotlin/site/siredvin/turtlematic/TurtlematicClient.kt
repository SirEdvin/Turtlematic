package site.siredvin.turtlematic

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import java.util.function.Consumer


@Suppress("UNUSED")
object TurtlematicClient: ClientModInitializer {

    private val EXTRA_MODELS = arrayOf(
        "turtle_chatter_upgrade_left",
        "turtle_chatter_upgrade_right",
        "turtle_creative_chest_upgrade_left",
        "turtle_creative_chest_upgrade_right",
    )

    fun onModelBakeEvent(manager: ResourceManager, out: Consumer<ResourceLocation>) {
        for (model in EXTRA_MODELS) {
            val location = ResourceLocation(Turtlematic.MOD_ID, model)
            out.accept(ModelResourceLocation(location, "inventory"))
        }
    }

    override fun onInitializeClient() {
        ModelLoadingRegistry.INSTANCE.registerModelProvider(this::onModelBakeEvent)
    }
}