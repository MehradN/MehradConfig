package ir.mehradn.mehradconfig.entrypoint;

import ir.mehradn.mehradconfig.MehradConfig;
import ir.mehradn.mehradconfig.gui.ConfigScreenBuilder;
import ir.mehradn.mehradconfig.gui.screen.MehradConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * This class is used for registering your configs for ModMenu.
 *
 * @see #register
 */
@Environment(EnvType.CLIENT)
public class ModMenuSupport {
    static final Map<String, ConfigScreenLoader> screenBuilders = new HashMap<>();

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

    @Environment(EnvType.CLIENT)
    record ConfigScreenLoader(Supplier<MehradConfig> configConstructor, ConfigScreenBuilder configScreenBuilder) {
        MehradConfigScreen build(Screen parentScreen) {
            return this.configScreenBuilder.buildAndLoad(this.configConstructor, parentScreen);
        }
    }
}
