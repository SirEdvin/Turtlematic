import { IPeripheralProvider } from "@siredvin/typed-peripheral-base";

/** @noSelf **/
export interface LavaBucket extends IPeripheral {
    void(): void;
}

/** @noSelf **/
export class DummyLavaBucket implements LavaBucket {
    void(): void {}
}

export const lavaBucketProvider = new IPeripheralProvider<LavaBucket>(
    "lava_bucket",
    () => new DummyLavaBucket()
);
