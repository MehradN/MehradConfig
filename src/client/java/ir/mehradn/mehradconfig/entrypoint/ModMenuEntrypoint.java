package ir.mehradn.mehradconfig.entrypoint;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import java.util.HashMap;
import java.util.Map;

public class ModMenuEntrypoint implements ModMenuApi {
    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        Map<String, ConfigScreenFactory<?>> factories = new HashMap<>();
        for (Map.Entry<String, ModMenuSupport.ConfigScreenLoader> entry : ModMenuSupport.screenBuilders.entrySet())
            factories.put(entry.getKey(), (parent) -> entry.getValue().build(parent));
        return factories;
    }
}
