import { IPeripheralProvider } from "@siredvin/typed-peripheral-base";

let IS_SILENT_GLOBAL = false;

/** @noSelf **/
export interface Piston extends IPeripheral {
    push(direction?: Direction): Result;
    isSilent(): boolean;
    setSilent(value: boolean): void;
}

/** @noSelf **/
export class DummyPiston implements Piston {
    isSilent(): boolean {
        return IS_SILENT_GLOBAL;
    }
    setSilent(value: boolean): void {
        IS_SILENT_GLOBAL = value;
    }
    push(direction?: Direction): Result {
        return $multi(true, null);
    }
}

export const pistonProvider = new IPeripheralProvider<Piston>(
    "piston",
    () => new DummyPiston()
);
