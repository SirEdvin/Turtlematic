package site.siredvin.turtlematic.data

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import site.siredvin.turtlematic.tags.ItemTags

class ItemTagsProvider(dataGenerator: FabricDataGenerator) : FabricTagProvider.ItemTagProvider(dataGenerator) {
    override fun generateTags() {
        TagsTargetCollection.DEFAULT_ENCHANTMENT_POWER_PROVIDERS.forEach { this.tag(ItemTags.ENCHANTMENT_POWER_PROVIDER).add(it.asItem()) }
    }
}