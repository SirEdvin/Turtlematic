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

    private val turtleLeftUpgradeCache: MutableMap<ResourceLocation, ModelResourceLocation> = hashMapOf()
    private val turtleRightUpgradeCache: MutableMap<ResourceLocation, ModelResourceLocation> = hashMapOf()

    fun onModelBakeEvent(manager: ResourceManager, out: Consumer<ResourceLocation>) {
        for (model in EXTRA_MODELS) {
            val location = ResourceLocation(Turtlematic.MOD_ID, model)
            out.accept(ModelResourceLocation(location, "inventory"))
        }
    }

    fun getLeftTurtleUpgradeModel(turtleID: ResourceLocation): ModelResourceLocation {
        if (!turtleLeftUpgradeCache.containsKey(turtleID))
            turtleLeftUpgradeCache[turtleID] = ModelResourceLocation("${Turtlematic.MOD_ID}:turtle_${turtleID.path}_upgrade_left", "inventory")
        return turtleLeftUpgradeCache[turtleID]!!
    }

    fun getRightTurtleUpgradeModel(turtleID: ResourceLocation): ModelResourceLocation {
        if (!turtleRightUpgradeCache.containsKey(turtleID))
            turtleRightUpgradeCache[turtleID] = ModelResourceLocation("${Turtlematic.MOD_ID}:turtle_${turtleID.path}_upgrade_right", "inventory")
        return turtleRightUpgradeCache[turtleID]!!
    }


    override fun onInitializeClient() {
        ModelLoadingRegistry.INSTANCE.registerModelProvider(this::onModelBakeEvent)
    }
}