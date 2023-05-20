package site.siredvin.turtlematic.util

import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.phys.Vec3

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