import { IPeripheralProvider } from "@siredvin/typed-peripheral-base";
import { DummyFuelApi, FuelApi } from "@siredvin/typed-peripheral-api/fuel";
import { InteractionApi } from "@siredvin/typed-peripheral-api/interaction";
import { ConfigurationAPI } from "@siredvin/typed-peripheral-api/configuration";
import { CaptureAPI } from "@siredvin/typed-peripheral-api/capture";
import { OperationApi } from "@siredvin/typed-peripheral-api/operations";
import { LookApi } from "@siredvin/typed-peripheral-api/look";
import { SuckAPI } from "@siredvin/typed-peripheral-api/suck";

/** @noSelf **/
export declare interface ProtectiveAutomata
    extends FuelApi,
        InteractionApi,
        ConfigurationAPI<object>,
        CaptureAPI,
        OperationApi,
        LookApi,
        SuckAPI {}

export const protectiveProvider = new IPeripheralProvider<ProtectiveAutomata>(
    "protectiveAutomata",
    () => null
);
