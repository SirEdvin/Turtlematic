package site.siredvin.lib.metaphysics;

import site.siredvin.lib.misc.IConfigHandler;

public interface IAutomataCoreTier extends IConfigHandler {
    int getInteractionRadius();
    int getMaxFuelConsumptionRate();
}
