package site.siredvin.turtlematic

import net.minecraft.resources.ResourceLocation
import java.util.function.Consumer

object TurtlematicCoreClient {
    private val CLIENT_HOOKS: MutableList<Runnable> = mutableListOf()
    private var initialized: Boolean = false

    private val EXTRA_MODELS: Array<String> = arrayOf()

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