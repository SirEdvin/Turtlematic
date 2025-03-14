package site.siredvin.turtlematic.client

import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.upgrades.UpgradeType

object TurtleRenderTrickRegistry {
    private val registry = mutableMapOf<UpgradeType<out ITurtleUpgrade>, TurtleRenderTrick>()

    fun registerTrick(serializer: UpgradeType<out ITurtleUpgrade>, trick: TurtleRenderTrick) {
        registry[serializer] = trick
    }

    fun getTrick(upgrade: ITurtleUpgrade): TurtleRenderTrick? = registry[upgrade.type]
}
