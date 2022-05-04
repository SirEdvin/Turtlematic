package site.siredvin.turtlematic.api

import net.minecraft.resources.ResourceLocation
import site.siredvin.lib.api.IConfigHandler

interface IAutomataCoreTier : IConfigHandler {
    val interactionRadius: Int
    val maxFuelConsumptionRate: Int
    /**
     * This is number, that cooldown will be multiply at
     */
    val cooldownReduceFactor: Double
    val traits: Set<ResourceLocation>
}