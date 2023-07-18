package site.siredvin.turtlematic.xplat

import net.minecraft.world.item.crafting.Ingredient
import site.siredvin.peripheralium.common.setup.Items

interface ModRecipeIngredients {
    companion object {
        private var _IMPL: ModRecipeIngredients? = null

        fun configure(impl: ModRecipeIngredients) {
            _IMPL = impl
        }

        fun get(): ModRecipeIngredients {
            if (_IMPL == null) {
                throw IllegalStateException("You should init Turtlematic Platform first")
            }
            return _IMPL!!
        }
    }

    val peripheralium: Ingredient
        get() = Ingredient.of(Items.PERIPHERALIUM_DUST.get())

    val peripheraliumUpgrade: Ingredient

    val soulLantern: Ingredient
    val emerald: Ingredient
    val redstoneDust: Ingredient
    val diamond: Ingredient
    val stick: Ingredient
    val ironIngot: Ingredient
    val goldIngot: Ingredient
    val netheriteIngot: Ingredient
    val netherStar: Ingredient

    val computerSpeaker: Ingredient
}
