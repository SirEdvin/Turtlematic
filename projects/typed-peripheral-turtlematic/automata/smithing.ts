import { IPeripheralProvider } from "@siredvin/typed-peripheral-base";
import { DummyFuelApi, FuelApi } from "@siredvin/typed-peripheral-api/fuel";

/** @noSelf **/
export declare interface SmithingAutomata extends FuelApi {
    smelt(mode: "block", direction?: Direction): Result;
    smelt(mode: "inventory", limit: number): Result;
}

/** @noSelf **/
class DummySmithingAutomata extends DummyFuelApi implements SmithingAutomata {
    smelt(mode: "block", direction?: Direction): Result;
    smelt(mode: "inventory", limit: number): Result;
    smelt(mode: unknown, limit?: unknown): Result {
        return $multi(true, null);
    }
}

export const smithingProvider = new IPeripheralProvider<SmithingAutomata>(
    "smithingAutomata",
    () => new DummySmithingAutomata()
);
