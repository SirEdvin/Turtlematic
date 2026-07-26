import { IPeripheralProvider } from "@siredvin/typed-peripheral-base";
import { FuelApi } from "@siredvin/typed-peripheral-api/fuel";
import { InteractionApi } from "@siredvin/typed-peripheral-api/interaction";
import { ConfigurationAPI } from "@siredvin/typed-peripheral-api/configuration";
import { OperationApi } from "@siredvin/typed-peripheral-api/operations";

/** @noSelf **/
export declare interface Automata
    extends FuelApi,
        InteractionApi,
        ConfigurationAPI<object>,
        OperationApi {}

export const automataProvider = new IPeripheralProvider<Automata>(
    "automata",
    () => null
);
