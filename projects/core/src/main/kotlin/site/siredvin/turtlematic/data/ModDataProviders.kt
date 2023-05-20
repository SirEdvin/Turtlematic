package site.siredvin.turtlematic.data

import site.siredvin.peripheralium.data.blocks.GeneratorSink

object ModDataProviders {
    fun add(generator: GeneratorSink) {
        generator.add {
            ModRecipeProvider(it)
        }
        generator.add {
            ModTurtleUpgradeDataProvider(it)
        }
    }
}