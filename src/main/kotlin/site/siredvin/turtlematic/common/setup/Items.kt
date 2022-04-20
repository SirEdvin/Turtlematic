package site.siredvin.turtlematic.common.setup

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.items.AutomataCore

object Items {
    val AUTOMATA_CORE = AutomataCore()

    fun register() {
        Registry.register(Registry.ITEM, ResourceLocation(Turtlematic.MOD_ID, "automata_core"), AUTOMATA_CORE)
    }
}