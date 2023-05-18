package site.siredvin.turtlematic.common.recipe

import net.minecraft.world.entity.Entity

interface SoulHarvestIngredient{
    fun match(entity: Entity): Boolean
    val name: String
    val description: String
    val requiredCount: Int
}