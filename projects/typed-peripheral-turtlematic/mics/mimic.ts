import { IPeripheralProvider } from "@siredvin/typed-peripheral-base";

/** @noSelf **/
export interface Mimic extends IPeripheral {
    reset();
    setTransformation(rlm: string);
    getTransformation(): string;
    setMimic(block: LuaTable, nbtData?: string);
    getMimic(): LuaMultiReturn<[LuaTable | null, string | null]>;
}

export const mimicProvider = new IPeripheralProvider<Mimic>("mimic");
