package site.siredvin.turtlematic.data

import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.data.models.model.ModelLocationUtils
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import site.siredvin.peripheralium.data.blocks.TURTLE_LEFT_UPGRADE
import site.siredvin.peripheralium.data.blocks.TURTLE_RIGHT_UPGRADE
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.peripheralium.data.blocks.createFlatItem
import site.siredvin.peripheralium.data.blocks.turtleUpgrades
import site.siredvin.peripheralium.xplat.XplatRegistries
import site.siredvin.turtlematic.TurtlematicCore

object ModItemModelProvider {

    val BASE_GEAR = ResourceLocation(TurtlematicCore.MOD_ID, "item/base_gear")
    val NETHERITE_GEAR = ResourceLocation(TurtlematicCore.MOD_ID, "item/netherite_gear")
    val STARBOUND_GEAR = ResourceLocation(TurtlematicCore.MOD_ID, "item/starbound_gear")
    val CREATIVE_GEAR = ResourceLocation(TurtlematicCore.MOD_ID, "item/creative_gear")
    val FIRE = ResourceLocation(TurtlematicCore.MOD_ID, "item/fire/")
    val BIG_FIRE = ResourceLocation(TurtlematicCore.MOD_ID, "item/big_fire/")


    fun createAutomataCore(
        generators: ItemModelGenerators, item: Item, gear: ResourceLocation,
        fire: String, fireTemplate: ResourceLocation = FIRE) {
        createFlatItem(
            generators, ModelLocationUtils.getModelLocation(item), gear,
            fireTemplate.withSuffix(fire)
        )
    }

    fun turtleUpgrades(generators: ItemModelGenerators, item: Item, textureSuffix: String = "", baseID: ResourceLocation? = null) {
        val realBaseID = baseID ?: XplatRegistries.ITEMS.getKey(item).withPrefix("turtle/")
        TURTLE_RIGHT_UPGRADE.create(
            realBaseID.withSuffix("_right"),
            TextureMapping().put(TextureSlot.TEXTURE, TextureMapping.getItemTexture(item).withSuffix(textureSuffix)),
            generators.output,
        )
        TURTLE_LEFT_UPGRADE.create(
            realBaseID.withSuffix("_left"),
            TextureMapping().put(TextureSlot.TEXTURE, TextureMapping.getItemTexture(item).withSuffix(textureSuffix)),
            generators.output,
        )
    }

    fun addModels(generators: ItemModelGenerators) {
        generators.generateFlatItem(Items.TURTLE_CHATTER.get(), ModelTemplates.FLAT_ITEM)
        generators.generateFlatItem(Items.SOUL_SCRAPPER.get(), ModelTemplates.FLAT_ITEM)
        generators.generateFlatItem(Items.CREATIVE_CHEST.get(), ModelTemplates.FLAT_ITEM)
        generators.generateFlatItem(Items.CHUNK_VIAL.get(), ModelTemplates.FLAT_ITEM)

        turtleUpgrades(generators, Items.CHUNK_VIAL.get())
        turtleUpgrades(generators, Items.CREATIVE_CHEST.get())
        turtleUpgrades(generators, Items.TURTLE_CHATTER.get(), baseID = ResourceLocation(TurtlematicCore.MOD_ID, "turtle/chatter"))

        createAutomataCore(generators, Items.AUTOMATA_CORE.get(), BASE_GEAR, "base")
        createAutomataCore(generators, Items.HUSBANDRY_AUTOMATA_CORE.get(), BASE_GEAR, "green")
        createAutomataCore(generators, Items.END_AUTOMATA_CORE.get(), BASE_GEAR, "purple")
        createAutomataCore(generators, Items.PROTECTIVE_AUTOMATA_CORE.get(), BASE_GEAR, "red")

        createAutomataCore(generators, Items.NETHERITE_HUSBANDRY_AUTOMATA_CORE.get(), NETHERITE_GEAR, "green")
        createAutomataCore(generators, Items.NETHERITE_END_AUTOMATA_CORE.get(), NETHERITE_GEAR, "purple")
        createAutomataCore(generators, Items.NETHERITE_PROTECTIVE_AUTOMATA_CORE.get(), NETHERITE_GEAR, "red")

        createAutomataCore(generators, Items.STARBOUND_HUSBANDRY_AUTOMATA_CORE.get(), STARBOUND_GEAR, "green", BIG_FIRE)
        createAutomataCore(generators, Items.STARBOUND_END_AUTOMATA_CORE.get(), STARBOUND_GEAR, "purple", BIG_FIRE)
        createAutomataCore(generators, Items.STARBOUND_PROTECTIVE_AUTOMATA_CORE.get(), STARBOUND_GEAR, "red", BIG_FIRE)

        createAutomataCore(generators, Items.CREATIVE_HUSBANDRY_AUTOMATA_CORE.get(), CREATIVE_GEAR, "green", BIG_FIRE)
        createAutomataCore(generators, Items.CREATIVE_END_AUTOMATA_CORE.get(), CREATIVE_GEAR, "purple", BIG_FIRE)
        createAutomataCore(generators, Items.CREATIVE_PROTECTIVE_AUTOMATA_CORE.get(), CREATIVE_GEAR, "red", BIG_FIRE)
    }

}