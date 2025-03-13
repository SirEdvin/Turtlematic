package site.siredvin.turtlematic.common.setup

import net.minecraft.resources.ResourceLocation
import site.siredvin.broccolium.modules.platform.PlatformToolkit
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.common.entities.ShootedItemProjectile
import site.siredvin.turtlematic.xplat.ModPlatform

object EntityTypes {
    val SHOOTED_ITEM_TYPE_ID = ResourceLocation.fromNamespaceAndPath(
        TurtlematicCore.MOD_ID,
        "shooted_item",
    )

    val SHOOTED_ITEM_TYPE = ModPlatform.registerEntity(SHOOTED_ITEM_TYPE_ID) {
        PlatformToolkit.get().createEntityType(SHOOTED_ITEM_TYPE_ID, ::ShootedItemProjectile)
    }

    fun doSomething() {}
}
