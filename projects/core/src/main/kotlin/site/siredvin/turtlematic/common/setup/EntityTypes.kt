package site.siredvin.turtlematic.common.setup

import net.minecraft.resources.ResourceLocation
import site.siredvin.peripheralium.xplat.PeripheraliumPlatform
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.common.entities.ShootedItemProjectile
import site.siredvin.turtlematic.xplat.TurtlematicPlatform

object EntityTypes {
    val SHOOTED_ITEM_TYPE_ID = ResourceLocation(
        TurtlematicCore.MOD_ID,
        "shooted_item",
    )

    val SHOOTED_ITEM_TYPE = TurtlematicPlatform.registerEntity(SHOOTED_ITEM_TYPE_ID) {
        PeripheraliumPlatform.createEntityType(SHOOTED_ITEM_TYPE_ID, ::ShootedItemProjectile)
    }

    fun doSomething() {}
}
