package site.siredvin.turtlematic.data

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.setup.Items

class ModDataGenerationEntrypoint: DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        fabricDataGenerator.addProvider(ModRecipeProvider(fabricDataGenerator))
        fabricDataGenerator.addProvider(BlockTagsProvider(fabricDataGenerator))
        fabricDataGenerator.addProvider(ItemTagsProvider(fabricDataGenerator))
    }
}