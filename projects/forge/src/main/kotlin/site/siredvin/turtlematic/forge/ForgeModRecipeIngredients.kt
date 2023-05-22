package site.siredvin.turtlematic.forge

import dan200.computercraft.shared.ModRegistry
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraftforge.common.Tags
import site.siredvin.turtlematic.xplat.ModRecipeIngredients

object ForgeModRecipeIngredients: ModRecipeIngredients {
    override val soulLantern: Ingredient
        get() = Ingredient.of(Items.SOUL_LANTERN)
    override val emerald: Ingredient
        get() = Ingredient.of(Tags.Items.GEMS_EMERALD)
    override val redstoneDust: Ingredient
        get() = Ingredient.of(Tags.Items.DUSTS_REDSTONE)
    override val diamond: Ingredient
        get() = Ingredient.of(Tags.Items.GEMS_DIAMOND)
    override val stick: Ingredient
        get() = Ingredient.of(Items.STICK)
    override val ironIngot: Ingredient
        get() = Ingredient.of(Tags.Items.INGOTS_IRON)
    override val goldIngot: Ingredient
        get() = Ingredient.of(Tags.Items.INGOTS_GOLD)
    override val netheriteIngot: Ingredient
        get() = Ingredient.of(Tags.Items.INGOTS_NETHERITE)
    override val netherStar: Ingredient
        get() = Ingredient.of(Tags.Items.NETHER_STARS)
    override val computerSpeaker: Ingredient
        get() = Ingredient.of(ModRegistry.Items.SPEAKER.get())
}