package site.siredvin.turtlematic.common.setup

import net.minecraft.util.registry.Registry
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.items.AutomataCore

class Items {
    companion object {
        val AUTOMATA_CORE = AutomataCore()

        fun register() {
            Registry.register(Registry.ITEM, Turtlematic.MOD_ID, AUTOMATA_CORE)
        }
    }
}