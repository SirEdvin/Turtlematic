package site.siredvin.lib.metaphysics

import site.siredvin.lib.misc.IConfigHandler

interface IAutomataCoreTier : IConfigHandler {
    val interactionRadius: Int
    val maxFuelConsumptionRate: Int
}