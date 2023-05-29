package site.siredvin.turtlematic.data

import site.siredvin.peripheralium.data.blocks.GeneratorSink
import site.siredvin.turtlematic.TurtlematicCore

object ModDataProviders {
    fun add(generator: GeneratorSink) {
        generator.add {
            ModRecipeProvider(it)
        }
        generator.add {
            ModTurtleUpgradeDataProvider(it)
        }
        val blockTags = generator.blockTags(TurtlematicCore.MOD_ID, ModTagsProvider::blockTags)
        generator.itemTags(TurtlematicCore.MOD_ID, ModTagsProvider::itemTags, blockTags)
        generator.entityTags(TurtlematicCore.MOD_ID, ModTagsProvider::entityTypeTags)
    }
}
