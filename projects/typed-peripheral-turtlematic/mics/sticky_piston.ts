import { IPeripheralProvider } from "@siredvin/typed-peripheral-base";

let IS_SILENT_GLOBAL = false;

/** @noSelf **/
export interface StickyPiston extends IPeripheral {
    pull(direction?: Direction): Result;
    push(direction?: Direction): Result;
    isSilent(): boolean;
    setSilent(value: boolean): void;
}

/** @noSelf **/
export class DummyStickyPiston implements StickyPiston {
    isSilent(): boolean {
        return IS_SILENT_GLOBAL;
    }
    setSilent(value: boolean): void {
        IS_SILENT_GLOBAL = value;
    }
    push(direction?: Direction): Result {
        return $multi(true, null);
    }
    pull(direction?: Direction): Result {
        return $multi(true, null);
    }
}

export const stickyPistonProvider = new IPeripheralProvider<StickyPiston>(
    "stickyPiston",
    () => new DummyStickyPiston()
);
