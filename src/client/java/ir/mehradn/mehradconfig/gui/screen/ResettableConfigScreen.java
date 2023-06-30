package ir.mehradn.mehradconfig.gui.screen;

import ir.mehradn.mehradconfig.MehradConfig;
import ir.mehradn.mehradconfig.entry.ConfigEntry;
import ir.mehradn.mehradconfig.gui.ConfigScreenBuilder;
import ir.mehradn.mehradconfig.gui.EntryWidgetFactory;
import ir.mehradn.mehradconfig.gui.widget.ConfigEntryWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/**
 * ResettableConfigScreen is a config screen for option and resettable config entries. Each entry widget comes with a reset button.
 */
@Environment(EnvType.CLIENT)
public class ResettableConfigScreen extends MehradConfigScreen {
    /**
     * @see ir.mehradn.mehradconfig.gui.ConfigScreenBuilder.DefaultScreens
     */
    public static final ConfigScreenBuilder.ScreenProperties DEFAULT_PROPERTIES = new ConfigScreenBuilder.ScreenProperties(
        (w, h, f) -> 200, (w, h, f) -> 125, (w, h, f) -> h - f.lineHeight * 3 - 40, (w, h, f) -> 300,
        (minecraft, thisScreen, parentScreen) -> minecraft.setScreen(parentScreen),
        (minecraft, thisScreen, parentScreen) -> minecraft.setScreen(parentScreen)
    );

    /**
     * @param config             the config to display and modify
     * @param properties         some screen properties to base this screen of
     * @param entryWidgetFactory an {@code EntryWidgetFactory} to create widgets for config entries
     * @param parentScreen       the parent screen
     */
    public ResettableConfigScreen(MehradConfig config, ConfigScreenBuilder.ScreenProperties properties, EntryWidgetFactory entryWidgetFactory,
                                  Screen parentScreen) {
        super(config, properties, entryWidgetFactory, new DefaultValueTextProvider(config.modId), parentScreen);
    }

    @Override
    public void init() {
        super.init();
        for (ConfigEntryWidget<?> widget : this.entryWidgets) {
            int x = widget.widget.getX() + this.properties.widgetWidth().get(this.width, this.height, this.font) + 4;
            int y = widget.widget.getY();
            Button resetButton = addRenderableWidget(Button.builder(Component.translatable("mehrad-config.resetButton.message"), (btn) -> {
                    widget.entry.reset();
                    widget.reportValueChange();
                })
                .bounds(x, y, 50, 20)
                .tooltip(Tooltip.create(Component.translatable("mehrad-config.resetButton.tooltip")))
                .build());
            resetButton.active = !widget.entry.isDefault();
            widget.onValueChange((entry) -> resetButton.active = !entry.isDefault());
        }
    }

    @Override
    protected ScreenRectangle getEntryWidgetBounds(int i) {
        int width = this.properties.widgetWidth().get(this.width, this.height, this.font);
        int height = 20;
        int x = (this.width - width - 54) / 2;
        int y = 20 + this.font.lineHeight + i * 24;
        return new ScreenRectangle(x, y, width, height);
    }

    @Override
    protected ScreenRectangle getEntryHoverRegion(int i) {
        ScreenRectangle bounds = getEntryWidgetBounds(i);
        return new ScreenRectangle(bounds.left(), bounds.top(), bounds.width() + 54, bounds.height() + 4);
    }

    @Environment(EnvType.CLIENT)
    public record DefaultValueTextProvider(String modId) implements ConfigEntryWidget.TextProvider {
        @Override
        public Component get(ConfigEntry<?> entry) {
            return wrapValueAndAddTitle(entry, entry.getTranslatedValue(this.modId));
        }

        @Override
        public <T> Component get(ConfigEntry<T> entry, T value) {
            return wrapValueAndAddTitle(entry, entry.getTranslatedValue(this.modId, value));
        }

        private Component wrapValueAndAddTitle(ConfigEntry<?> entry, Component value) {
            Component wrapped;
            if (!entry.shouldWrite())
                wrapped = Component.translatable("mehrad-config.valueWrapper.shouldNotWrite", value);
            else if (entry.isDefault())
                wrapped = Component.translatable("mehrad-config.valueWrapper.isDefault", value);
            else
                wrapped = value;

            return CommonComponents.optionNameValue(entry.getTranslatedTitle(this.modId), wrapped);
        }
    }
}
