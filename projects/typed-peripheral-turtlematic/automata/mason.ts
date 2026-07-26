import { IPeripheralProvider } from "@siredvin/typed-peripheral-base";
import { DummyFuelApi, FuelApi } from "@siredvin/typed-peripheral-api/fuel";

/** @noSelf **/
export declare interface MasonAutomata extends FuelApi {
    chisel(mode: "block", target: string, direction?: Direction): Result;
}

/** @noSelf **/
class DummyMasonAutomata extends DummyFuelApi implements MasonAutomata {
    chisel(mode: "block", target: string, direction?: Direction): Result {
        return $multi(true, null);
    }
}

export const masonProvider = new IPeripheralProvider<MasonAutomata>(
    "masonAutomata",
    () => new DummyMasonAutomata()
);
