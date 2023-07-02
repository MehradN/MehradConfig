package ir.mehradn.mehradconfig.entrypoint;

import ir.mehradn.mehradconfig.gui.ConfigScreenBuilder;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used for registering your config screen builders for ModMenu.
 *
 * @see #register
 */
public class ModMenuConfigScreen {
    static final Map<String, ConfigScreenBuilder> modMenuScreenBuilders = new HashMap<>();

    /**
     * Registers a config screen builder to build screens for ModMenu. The screen will be created by {@link ConfigScreenBuilder#buildAndLoad}.
     * This method by itself won't register your config, and you need to call {@link ModMenuConfig#register} before this in the main entrypoint of
     * your mod.
     *
     * @param modId               the mod id of your mod
     * @param configScreenBuilder the config screen builder to build the screen with
     */
    public static void register(String modId, ConfigScreenBuilder configScreenBuilder) {
        modMenuScreenBuilders.put(modId, configScreenBuilder);
    }
}
