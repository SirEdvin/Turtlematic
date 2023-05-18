package site.siredvin.turtlematic.integrations.chipped

import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.computercraft.peripheral.forged.MasonAutomataCorePeripheral

class Integration: Runnable {
    override fun run() {
        Turtlematic.LOGGER.info(String.format("Loading chipped integration"))
        MasonAutomataCorePeripheral.addRecipeHandler(ChippedMasonRecipeHandler())
    }
}