package site.siredvin.lib.util.representation

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Shearable
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.level.block.state.BlockState


fun animalData(entity: Entity, data: MutableMap<String, Any>,) {
    if (entity is Animal) {
        data["baby"] = entity.isBaby
        data["inLove"] = entity.isInLove
        data["aggressive"] = entity.isAggressive
        if (entity is Shearable) {
            data["shareable"] = entity.readyForShearing();
        }
    }
}

fun effectsData(entity: Entity, data: MutableMap<String, Any>) {
    if (entity is LivingEntity) {
        val effects: MutableList<MutableMap<String, Any>> = mutableListOf()
        entity.activeEffectsMap.forEach {
            effects.add(
                hashMapOf(
                    "name" to it.key.displayName.string,
                    "technicalName" to it.key.descriptionId,
                    "duration" to it.value.duration,
                    "amplifier" to it.value.amplifier,
                    "isAmbient" to it.value.isAmbient
                )
            )
        }
        data["effects"] = effects
    }
}

fun stateProperties(state: BlockState, data: MutableMap<String, Any>) {
    val properties: MutableMap<String, Any> = mutableMapOf()
    state.properties.forEach {
        properties[it.name] = state.getValue(it).toString()
    }
    data["properties"] = properties
}
