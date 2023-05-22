package site.siredvin.turtlematic

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.ItemSupplier
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import site.siredvin.turtlematic.common.entities.ShootedItemProjectile
import site.siredvin.turtlematic.common.setup.EntityTypes
import java.util.function.Consumer
import java.util.function.Supplier

object TurtlematicCoreClient {
    private val CLIENT_HOOKS: MutableList<Runnable> = mutableListOf()
    private var initialized: Boolean = false

    private val EXTRA_MODELS: Array<String> = arrayOf(
        "turtle/chatter_left",
        "turtle/chatter_right",
        "turtle/chunk_vial_left",
        "turtle/chunk_vial_right",
        "turtle/creative_chest_left",
        "turtle/creative_chest_right",
    )

    val EXTRA_ENTITY_RENDERERS: Array<Supplier<EntityType<Entity>>> = arrayOf(
        EntityTypes.SHOOTED_ITEM_TYPE as Supplier<EntityType<Entity>>
    )

    fun getEntityRendererProvider(type: EntityType<Entity>): EntityRendererProvider<Entity> {
        if (type == EntityTypes.SHOOTED_ITEM_TYPE.get())
            return EntityRendererProvider<ShootedItemProjectile> { ThrownItemRenderer(it) } as EntityRendererProvider<Entity>
        throw IllegalArgumentException("There is no extra renderer for $type")
    }

    fun registerExtraModels(register: Consumer<ResourceLocation>) {
        EXTRA_MODELS.forEach { register.accept(ResourceLocation(TurtlematicCore.MOD_ID, it)) }
    }

    fun registerHook(it: Runnable) {
        if (!initialized)
            CLIENT_HOOKS.add(it)
        else
            it.run()
    }

    fun onInit() {
        CLIENT_HOOKS.forEach { it.run() }
        initialized = true
    }
}