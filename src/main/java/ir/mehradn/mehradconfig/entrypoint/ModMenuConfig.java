package ir.mehradn.mehradconfig.entrypoint;

import ir.mehradn.mehradconfig.MehradConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * This class is used for registering your configs for ModMenu.
 *
 * @see #register
 */
public final class ModMenuConfig {
    static final Map<String, Supplier<MehradConfig>> modMenuConfigs = new HashMap<>();

    /**
     * Registers a config constructor for ModMenu. The screen will by default use the compact default screen type.
     * Checkout {@code ModMenuConfigScreen} for customizing the config screen.
     *
     * @param modId               the mod id of your mod
     * @param configConstructor   a constructor for the type of the config to load, modify and save
     */
    public static void register(String modId, Supplier<MehradConfig> configConstructor) {
        modMenuConfigs.put(modId, configConstructor);
    }
}
