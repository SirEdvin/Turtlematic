package site.siredvin.turtlematic.util

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.Pose
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.phys.Vec3
import site.siredvin.peripheralium.api.datatypes.InteractionMode
import site.siredvin.peripheralium.api.datatypes.VerticalDirection
import java.util.*
import java.util.stream.Collectors

fun ResourceLocation.toNetherite(): ResourceLocation {
    return ResourceLocation(this.namespace, "netherite_${this.path}")
}

fun ResourceLocation.toStarbound(): ResourceLocation {
    return ResourceLocation(this.namespace, "starbound_${this.path}")
}

fun ResourceLocation.toCreative(): ResourceLocation {
    return ResourceLocation(this.namespace, "creative_${this.path}")
}

fun String.camelToSnakeCase(): String {
    val pattern = "(?<=.)[A-Z]".toRegex()
    return this.replace(pattern, "_$0").lowercase()
}

fun Projectile.advancedShoot(deltaMovements: Vec3) {
    deltaMovement = deltaMovements
    val horizontalPower = deltaMovements.horizontalDistance()
    yRot = (Mth.atan2(deltaMovements.x, deltaMovements.z) * 57.2957763671875).toFloat()
    xRot = (Mth.atan2(deltaMovements.y, horizontalPower) * 57.2957763671875).toFloat()
    yRotO = yRot
    xRotO = xRot
}

fun IArguments.getInteractionMode(index: Int, allowedMods: Set<InteractionMode>): InteractionMode {
    return InteractionMode.luaValueOf(this.getString(index), allowedMods)
}

fun IArguments.optVerticalDirection(index: Int): VerticalDirection? {
    val directionArgument = this.optString(index)
    return if (directionArgument.isEmpty) {
        null
    } else {
        VerticalDirection.luaValueOf(
            directionArgument.get(),
        )
    }
}

fun IArguments.optPose(index: Int): Pose? {
    val poseArgument = this.optString(index)
    return if (poseArgument.isEmpty) {
        null
    } else {
        try {
            Pose.valueOf(poseArgument.get().uppercase())
        } catch (exc: IllegalArgumentException) {
            val allValues = Arrays.stream(Pose.values()).map { mode -> mode.name.lowercase() }.collect(
                Collectors.toList(),
            ).joinToString(", ")
            throw LuaException("Vertical direction should be one of: $allValues")
        }
    }
}
