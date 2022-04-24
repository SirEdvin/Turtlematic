package site.siredvin.turtlematic.api

import site.siredvin.lib.misc.IConfigHandler

interface IAutomataCoreTier : IConfigHandler {
    val interactionRadius: Int
    val maxFuelConsumptionRate: Int
}