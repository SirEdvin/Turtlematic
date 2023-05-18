package site.siredvin.turtlematic.common.recipe

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

class SimpleSoulHarvestIngredient(private val entityType: EntityType<*>, private val _requiredCount: Int): SoulHarvestIngredient {
    override val requiredCount: Int
        get() = _requiredCount

    override val name: String
        get() = EntityType.getKey(entityType).toString()

    override val description: String
        get() = "${requiredCount}x${entityType.description.string}"

    override fun match(entity: Entity): Boolean {
        return entity.type == entityType
    }
}