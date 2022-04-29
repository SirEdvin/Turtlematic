package site.siredvin.turtlematic.common.recipe

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.npc.Villager
import net.minecraft.world.entity.npc.VillagerProfession

class VillagerSoulHarvestIngredient(private val profession: VillagerProfession): SoulHarvestIngredient {
    override val requiredCount: Int
        get() = 1

    override val name: String
        get() = "villager_${profession.name}"

    override val description: String
        get() = "${requiredCount}x${profession.name.replaceFirstChar { it.titlecase() }} ${EntityType.VILLAGER.description.string}"

    override fun match(entity: Entity): Boolean {
        return entity is Villager && entity.villagerData.profession == profession
    }
}