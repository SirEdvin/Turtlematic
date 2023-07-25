package site.siredvin.turtlematic.client

import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import dan200.computercraft.impl.TurtleUpgrades

object TurtleRenderTrickRegistry {
    private val registry = mutableMapOf<TurtleUpgradeSerialiser<out ITurtleUpgrade>, TurtleRenderTrick>()

    fun getSerializer(upgrade: ITurtleUpgrade): TurtleUpgradeSerialiser<ITurtleUpgrade>? {
        @Suppress("UNCHECKED_CAST")
        return TurtleUpgrades.instance().getWrapper(upgrade)?.serialiser as? TurtleUpgradeSerialiser<ITurtleUpgrade>
    }

    fun registerTrick(serializer: TurtleUpgradeSerialiser<out ITurtleUpgrade>, trick: TurtleRenderTrick) {
        registry[serializer] = trick
    }

    fun getTrick(upgrade: ITurtleUpgrade): TurtleRenderTrick? {
        val serializer = getSerializer(upgrade) ?: return null
        return registry[serializer]
    }
}
