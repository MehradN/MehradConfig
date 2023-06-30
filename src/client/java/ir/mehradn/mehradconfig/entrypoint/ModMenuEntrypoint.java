package ir.mehradn.mehradconfig.entrypoint;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import ir.mehradn.mehradconfig.MehradConfig;
import ir.mehradn.mehradconfig.gui.ConfigScreenBuilder;
import ir.mehradn.mehradconfig.gui.screen.MehradConfigScreen;
import net.minecraft.client.gui.screens.Screen;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * This is the entrypoint used by ModMenu to create the config buttons. Use the {@link #register} to register your configs.
 */
public class ModMenuEntrypoint implements ModMenuApi {
    private static final Map<String, ConfigScreenLoader> screenBuilders = new HashMap<>();

    /**
     * Registers a config constructor and a config screen builder to build screens for ModMenu. The screen will be created by
     * {@link ConfigScreenBuilder#buildAndLoad}.
     *
     * @param modId               the mod id of your mod
     * @param configConstructor   a constructor for the type of the config to load, modify and save
     * @param configScreenBuilder the config screen builder to build the screen with
     */
    public static void register(String modId, Supplier<MehradConfig> configConstructor, ConfigScreenBuilder configScreenBuilder) {
        screenBuilders.put(modId, new ConfigScreenLoader(configConstructor, configScreenBuilder));
    }

    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        Map<String, ConfigScreenFactory<?>> factories = new HashMap<>();
        for (Map.Entry<String, ConfigScreenLoader> entry : screenBuilders.entrySet())
            factories.put(entry.getKey(), (parent) -> entry.getValue().build(parent));
        return factories;
    }

    private record ConfigScreenLoader(Supplier<MehradConfig> configConstructor, ConfigScreenBuilder configScreenBuilder) {
        MehradConfigScreen build(Screen parentScreen) {
            return this.configScreenBuilder.buildAndLoad(this.configConstructor, parentScreen);
        }
    }
}
