package ir.mehradn.mehradconfig.entrypoint;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import ir.mehradn.mehradconfig.MehradConfig;
import ir.mehradn.mehradconfig.gui.ConfigScreenBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class ModMenuEntrypoint implements ModMenuApi {
    private static final ConfigScreenBuilder DEFAULT = new ConfigScreenBuilder().setScreenType(ConfigScreenBuilder.DefaultScreens.COMPACT);

    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        Map<String, ConfigScreenFactory<?>> factories = new HashMap<>();
        for (String modId : ModMenuConfig.modMenuConfigs.keySet()) {
            factories.put(modId, (parent) -> {
                Supplier<MehradConfig> configConstructor = ModMenuConfig.modMenuConfigs.get(modId);
                ConfigScreenBuilder configScreenBuilder = ModMenuConfigScreen.modMenuScreenBuilders.getOrDefault(modId, DEFAULT);
                return configScreenBuilder.buildAndLoad(configConstructor, parent);
            });
        }
        return factories;
    }
}
