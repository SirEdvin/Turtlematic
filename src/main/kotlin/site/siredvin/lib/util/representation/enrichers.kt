package site.siredvin.lib.util.representation

import net.minecraft.core.Registry
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Shearable
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.level.block.entity.BeehiveBlockEntity
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.IntegerProperty


fun animalData(entity: Entity, data: MutableMap<String, Any>) {
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

fun cropAge(state: BlockState, data: MutableMap<String, Any>) {
    val ageProperty = state.properties.find { it.name == "age" } as IntegerProperty?
    if (ageProperty != null) {
        data["age"] = state.getValue(ageProperty)
        data["maxAge"] = ageProperty.possibleValues.maxOf { it }
    }
}


fun honeyLevel(state: BlockState, data: MutableMap<String, Any>) {
    val ageProperty = state.properties.find { it.name == "honey_level" } as IntegerProperty?
    if (ageProperty != null) {
        data["honeyLevel"] = state.getValue(ageProperty)
    }
}

fun beeNestAnalyze(entity: BlockEntity, data: MutableMap<String, Any>) {
    if (entity is BeehiveBlockEntity) {
        data["isSmoked"] = entity.isSedated
        data["isFull"] = entity.isFull
        data["bees"] = entity.writeBees().map {
            it as CompoundTag
            val beeData = hashMapOf<String, Any>()
            beeData["ticksInHive"] = it.getInt("TicksInHive")
            beeData["minOccupationTicks"] = it.getInt("MinOccupationTicks")
            val entityData = it.getCompound("EntityData")
            beeData["hasFlower"] = entityData.contains("FlowerPos")
            beeData["health"] = entityData.getInt("Health")
            beeData["hasStung"] = entityData.getBoolean("HasStung")
            beeData["hasNectar"] = entityData.getBoolean("HasNectar")
            beeData["id"] = entityData.getString("id")
            beeData["name"] = Registry.ENTITY_TYPE.get(ResourceLocation(beeData["id"] as String)).description.string
            return@map beeData
        }
    }
}
