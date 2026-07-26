import { IPeripheralProvider } from "@siredvin/typed-peripheral-base";
import { FuelApi } from "@siredvin/typed-peripheral-api/fuel";
import { InteractionApi } from "@siredvin/typed-peripheral-api/interaction";
import { ConfigurationAPI } from "@siredvin/typed-peripheral-api/configuration";
import { OperationApi } from "@siredvin/typed-peripheral-api/operations";
import { LookApi } from "@siredvin/typed-peripheral-api/look";
import { CaptureAPI } from "@siredvin/typed-peripheral-api/capture";
import { SuckAPI } from "@siredvin/typed-peripheral-api/suck";
import { WarpAPI } from "@siredvin/typed-peripheral-api/warp";

/** @noSelf **/
export declare interface EndAutomata
    extends FuelApi,
        InteractionApi,
        ConfigurationAPI<object>,
        OperationApi,
        LookApi,
        CaptureAPI,
        SuckAPI,
        WarpAPI {}

export const endAutomataProvider = new IPeripheralProvider<EndAutomata>(
    "endAutomata",
    () => null
);
