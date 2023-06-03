package site.siredvin.turtlematic.data

import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import site.siredvin.peripheralium.data.blocks.ItemTagConsumer
import site.siredvin.peripheralium.data.blocks.TagConsumer
import site.siredvin.turtlematic.tags.BlockTags
import site.siredvin.turtlematic.tags.EntityTags
import site.siredvin.turtlematic.tags.ItemTags

object ModTagsProvider {
    val DEFAULT_ENCHANTMENT_POWER_PROVIDERS = arrayOf(
        Blocks.BOOKSHELF,
        Blocks.CREEPER_HEAD, Blocks.CREEPER_WALL_HEAD,
        Blocks.DRAGON_HEAD, Blocks.DRAGON_WALL_HEAD,
        Blocks.WITHER_SKELETON_SKULL, Blocks.WITHER_SKELETON_WALL_SKULL,
        Blocks.SKELETON_SKULL, Blocks.SKELETON_WALL_SKULL,
        Blocks.ZOMBIE_HEAD, Blocks.ZOMBIE_WALL_HEAD,
        Blocks.PLAYER_HEAD, Blocks.PLAYER_WALL_HEAD,
        Blocks.CANDLE, Blocks.CANDLE_CAKE,
    )
    val HUSBANDRY_EXTRA_CROPS = arrayOf(
        Blocks.NETHER_WART,
        Blocks.COCOA,
    )

    @JvmStatic
    fun blockTags(consumer: TagConsumer<Block>) {
        DEFAULT_ENCHANTMENT_POWER_PROVIDERS.forEach { consumer.tag(BlockTags.ENCHANTMENT_POWER_PROVIDER).add(it) }
        HUSBANDRY_EXTRA_CROPS.forEach { consumer.tag(BlockTags.HUSBANDRY_EXTRA_CROPS).add(it) }
    }

    @JvmStatic
    fun itemTags(consumer: ItemTagConsumer) {
        DEFAULT_ENCHANTMENT_POWER_PROVIDERS.forEach { consumer.tag(ItemTags.ENCHANTMENT_POWER_PROVIDER).add(it.asItem()) }
    }

    @JvmStatic
    fun entityTypeTags(consumer: TagConsumer<EntityType<*>>) {
        consumer.tag(EntityTags.ANIMAL).add(EntityType.HOGLIN)
        consumer.tag(EntityTags.AI_CONTROL_BLACKLIST).add(
            EntityType.ENDER_DRAGON,
            EntityType.WITHER,
            EntityType.WARDEN,
        )
    }
}
