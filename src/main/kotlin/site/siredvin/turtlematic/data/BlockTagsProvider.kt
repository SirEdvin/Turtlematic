package site.siredvin.turtlematic.data

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import site.siredvin.turtlematic.data.TagsTargetCollection.DEFAULT_ENCHANTMENT_POWER_PROVIDERS
import site.siredvin.turtlematic.data.TagsTargetCollection.HUSBANDRY_EXTRA_CROPS
import site.siredvin.turtlematic.tags.BlockTags

class BlockTagsProvider(dataGenerator: FabricDataGenerator) : FabricTagProvider.BlockTagProvider(dataGenerator) {
    override fun generateTags() {
        DEFAULT_ENCHANTMENT_POWER_PROVIDERS.forEach { this.tag(BlockTags.ENCHANTMENT_POWER_PROVIDER).add(it) }
        HUSBANDRY_EXTRA_CROPS.forEach { this.tag(BlockTags.HUSBANDRY_EXTRA_CROPS).add(it) }
    }
}