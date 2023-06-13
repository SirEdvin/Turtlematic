package site.siredvin.turtlematic.fabric

import dan200.computercraft.shared.ModRegistry
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import site.siredvin.turtlematic.xplat.ModRecipeIngredients

object FabricModRecipeIngredients : ModRecipeIngredients {

    override val peripheraliumUpgrade: Ingredient
        get() = Ingredient.of(site.siredvin.peripheralium.common.setup.Items.PERIPHERALIUM_UPGRADE_TEMPLATE.get())
    override val soulLantern: Ingredient
        get() = Ingredient.of(Items.SOUL_LANTERN)
    override val emerald: Ingredient
        get() = Ingredient.of(ConventionalItemTags.EMERALDS)
    override val redstoneDust: Ingredient
        get() = Ingredient.of(ConventionalItemTags.REDSTONE_DUSTS)
    override val diamond: Ingredient
        get() = Ingredient.of(ConventionalItemTags.DIAMONDS)
    override val stick: Ingredient
        get() = Ingredient.of(Items.STICK)
    override val ironIngot: Ingredient
        get() = Ingredient.of(ConventionalItemTags.IRON_INGOTS)
    override val goldIngot: Ingredient
        get() = Ingredient.of(ConventionalItemTags.GOLD_INGOTS)
    override val netheriteIngot: Ingredient
        get() = Ingredient.of(ConventionalItemTags.NETHERITE_INGOTS)
    override val netherStar: Ingredient
        get() = Ingredient.of(Items.NETHER_STAR)
    override val computerSpeaker: Ingredient
        get() = Ingredient.of(ModRegistry.Items.SPEAKER.get())
}
