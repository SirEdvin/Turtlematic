package site.siredvin.turtlematic.fabric

import site.siredvin.peripheralium.fabric.FabricBaseInnerPlatform
import site.siredvin.turtlematic.TurtlematicCore

object FabricModInnerPlatform : FabricBaseInnerPlatform() {
    override val modID: String
        get() = TurtlematicCore.MOD_ID
}
