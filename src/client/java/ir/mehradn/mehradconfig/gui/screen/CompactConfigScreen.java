package ir.mehradn.mehradconfig.gui.screen;

import ir.mehradn.mehradconfig.MehradConfig;
import ir.mehradn.mehradconfig.entry.ConfigEntry;
import ir.mehradn.mehradconfig.gui.ConfigScreenBuilder;
import ir.mehradn.mehradconfig.gui.EntryWidgetFactory;
import ir.mehradn.mehradconfig.gui.widget.ConfigEntryWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/**
 * CompactConfigScreen is a config screen similar to the vanilla options screen.
 */
@Environment(EnvType.CLIENT)
public class CompactConfigScreen extends MehradConfigScreen {
    /**
     * @param config             the config to display and modify
     * @param properties         some screen properties to base this screen of
     * @param entryWidgetFactory an {@code EntryWidgetFactory} to create widgets for config entries
     */
    public CompactConfigScreen(MehradConfig config, ConfigScreenBuilder.ScreenProperties properties, EntryWidgetFactory entryWidgetFactory) {
        super(config, properties, entryWidgetFactory, new SimpleTextProvider(config.modId));
    }

    @Override
    protected ScreenRectangle getEntryWidgetBounds(int i) {
        int width = this.properties.widgetWidth().get(this.width, this.height, this.font);
        int height = 20;
        int x = this.width / 2 + (i % 2 == 0 ? -width - 2 : 2);
        int y = 20 + this.font.lineHeight + (i / 2) * 24;
        return new ScreenRectangle(x, y, width, height);
    }

    @Override
    protected ScreenRectangle getEntryHoverRegion(int i) {
        ScreenRectangle bounds = getEntryWidgetBounds(i);
        return new ScreenRectangle(bounds.left(), bounds.top(), bounds.width() + 4, bounds.height() + 4);
    }

    @Environment(EnvType.CLIENT)
    private record SimpleTextProvider(String modId) implements ConfigEntryWidget.TextProvider {
        @Override
        public Component get(ConfigEntry<?> entry) {
            return addTitle(entry, entry.getTranslatedValue(this.modId));
        }

        @Override
        public <T> Component get(ConfigEntry<T> entry, T value) {
            return addTitle(entry, entry.getTranslatedValue(this.modId, value));
        }

        private Component addTitle(ConfigEntry<?> entry, Component value) {
            return CommonComponents.optionNameValue(entry.getTranslatedTitle(this.modId), value);
        }
    }
}
