package site.siredvin.turtlematic.fabric

import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.tweakium.modules.platform.FabricInnerComputerBasePlatform

object FabricModInnerPlatform : FabricInnerComputerBasePlatform() {
    override val modID: String
        get() = TurtlematicCore.MOD_ID
}
