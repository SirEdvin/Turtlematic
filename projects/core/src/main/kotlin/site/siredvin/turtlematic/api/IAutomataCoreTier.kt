package site.siredvin.turtlematic.api

import net.minecraft.resources.ResourceLocation
import site.siredvin.peripheralium.api.config.IConfigHandler
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import kotlin.random.Random

interface IAutomataCoreTier : IConfigHandler {
    val interactionRadius: Int
    val maxFuelConsumptionRate: Int
    val storageScaleFactor: Double

    /**
     * This is number, that cooldown will be multiply at
     */
    val cooldownReduceFactor: Double
    val traits: Set<ResourceLocation>

    fun needRestoreDurability(): Boolean {
        if (traits.contains(AutomataCoreTraits.DURABILITY_REFUND)) {
            return true
        }
        if (traits.contains(AutomataCoreTraits.DURABILITY_REFUND_CHANCE)) {
            return Random.nextDouble() <= TurtlematicConfig.durabilityRestoreChance
        }
        return false
    }
}
