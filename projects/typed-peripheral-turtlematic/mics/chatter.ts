import { IPeripheralProvider } from "@siredvin/typed-peripheral-base";

let DUMMY_MESSAGE: string | null = null;

/** @noSelf **/
export interface Chatter extends IPeripheral {
    getMessage(): string | null;
    setMessage(text: string): void;
    clearMessage(): void;
}

/** @noSelf **/
export class DummyChatter implements Chatter {
    getMessage(): string | null {
        return DUMMY_MESSAGE;
    }
    setMessage(text: string): void {
        DUMMY_MESSAGE = text;
        print("Setting message to chatter", DUMMY_MESSAGE);
    }
    clearMessage(): void {
        DUMMY_MESSAGE = null;
    }
}

export const chatterProvider = new IPeripheralProvider<Chatter>(
    "chatter",
    () => new DummyChatter()
);
