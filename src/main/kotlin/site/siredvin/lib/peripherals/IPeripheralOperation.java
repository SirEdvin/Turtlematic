package site.siredvin.lib.peripherals;

import site.siredvin.lib.misc.IConfigHandler;

import java.util.Map;

public interface IPeripheralOperation<T> extends IConfigHandler {
    int getInitialCooldown();
    int getCooldown(T context);
    int getCost(T context);
    Map<String, Object> computerDescription();
}
