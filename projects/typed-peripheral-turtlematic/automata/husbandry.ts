import { IPeripheralProvider } from "@siredvin/typed-peripheral-base";
import { FuelApi, FuelAPIConfiguration } from "@siredvin/typed-peripheral-api/fuel";
import { InteractionApi } from "@siredvin/typed-peripheral-api/interaction";
import { ConfigurationAPI } from "@siredvin/typed-peripheral-api/configuration";
import { OperationApi } from "@siredvin/typed-peripheral-api/operations";
import { LookApi } from "@siredvin/typed-peripheral-api/look";
import { CaptureAPI } from "@siredvin/typed-peripheral-api/capture";

export declare interface HusbandryConfiguration extends FuelAPIConfiguration {
    maxHusbandryPoints: number;
    simulationGrowCost: number;
}

/** @noSelf **/
export declare interface HusbandryAutomata
    extends FuelApi,
        InteractionApi,
        ConfigurationAPI<HusbandryConfiguration>,
        OperationApi,
        LookApi,
        CaptureAPI {
    harvest(direction?: "up" | "down"): Result;
    getHusbandryPoints(): number;
    simulateGrow(): Result;
}

export const husbandryAutomataProvider =
    new IPeripheralProvider<HusbandryAutomata>("husbandryAutomata", () => null);
