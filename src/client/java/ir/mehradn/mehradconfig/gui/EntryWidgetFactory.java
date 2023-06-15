package ir.mehradn.mehradconfig.gui;

import ir.mehradn.mehradconfig.entry.BooleanEntry;
import ir.mehradn.mehradconfig.entry.ConfigEntry;
import ir.mehradn.mehradconfig.entry.EnumEntry;
import ir.mehradn.mehradconfig.entry.NumberEntry;
import ir.mehradn.mehradconfig.gui.widget.BooleanWidget;
import ir.mehradn.mehradconfig.gui.widget.ConfigEntryWidget;
import ir.mehradn.mehradconfig.gui.widget.EnumWidget;
import ir.mehradn.mehradconfig.gui.widget.SliderNumberWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.util.HashMap;

/**
 * EntryWidgetFactory is a factory for creating {@link ConfigEntryWidget ConfigEntryWidgets} from config entries.
 */
@Environment(EnvType.CLIENT)
public class EntryWidgetFactory {
    private final HashMap<String, WidgetBuilder<?, ?>> widgetBuilders = new HashMap<>();

    /**
     * Adds a widget builder to the factory.
     *
     * @param entryTypeId   the type id that determines which entries should use this builder
     * @param widgetBuilder the widget builder to be used
     * @param <S>           the type of the config value
     * @param <T>           the type of the config entry type information object
     * @see ConfigEntry.EntryTypeInfo#id
     */
    public <S, T extends ConfigEntry.EntryTypeInfo<S>> void addWidgetBuilder(String entryTypeId, WidgetBuilder<S, T> widgetBuilder) {
        this.widgetBuilders.put(entryTypeId, widgetBuilder);
    }

    /**
     * Builds a widget from the given information.
     *
     * @param entry the config entry to build the widget for
     * @param textProvider the text provider to be used in the widget
     * @return a config entry widget which will display and update this given config entry
     * @param <S> the type of the config value
     */
    public <S> ConfigEntryWidget<S> build(int x, int y, int width, int height,
                                          ConfigEntry<S> entry, ConfigEntryWidget.TextProvider<S> textProvider) {
        ConfigEntry.EntryTypeInfo<S> typeInfo = entry.entryTypeInfo();
        String typeId = typeInfo.id();
        WidgetBuilder<?, ?> widgetBuilder = this.widgetBuilders.get(typeId);
        return typeFreeBuild(x, y, width, height, widgetBuilder, typeInfo, entry, textProvider);
    }

    /**
     * Adds the default widget builders for the default config entries to this widget factory.
     */
    public void addDefaultBuilders() {
        this.<Boolean, BooleanEntry.BooleanTypeInfo>addWidgetBuilder(
            BooleanEntry.BooleanTypeInfo.ID,
            (x, y, w, h, typeInfo, entry, textProvider)
                -> new BooleanWidget(x, y, w, h, entry, textProvider));
        this.<Integer, NumberEntry.NumberTypeInfo>addWidgetBuilder(
            NumberEntry.NumberTypeInfo.ID,
            (x, y, w, h, typeInfo, entry, textProvider)
                -> new SliderNumberWidget(x, y, w, h, typeInfo.min(), typeInfo.max(), entry, textProvider));
        addEnumBuilder();
    }

    @SuppressWarnings("unchecked")
    private <S, T extends ConfigEntry.EntryTypeInfo<S>>
    ConfigEntryWidget<S> typeFreeBuild(int x, int y, int width, int height,
                                       WidgetBuilder<?, ?> widgetBuilder, ConfigEntry.EntryTypeInfo<S> typeInfo,
                                       ConfigEntry<S> entry, ConfigEntryWidget.TextProvider<S> textProvider) {
        return ((WidgetBuilder<S, T>)widgetBuilder).build(x, y, width, height, (T)typeInfo, entry, textProvider);
    }

    private <E extends Enum<E>> void addEnumBuilder() {
        this.<E, EnumEntry.EnumTypeInfo<E>>addWidgetBuilder(
            EnumEntry.EnumTypeInfo.ID,
            (x, y, w, h, typeInfo, entry, textProvider)
                -> new EnumWidget<>(x, y, w, h, typeInfo.enumClass(), entry, textProvider));
    }

    /**
     * WidgetBuilder is a functional interface for creating a {@link ConfigEntryWidget} from all the information that you should need.
     *
     * @param <S> the type of the config value
     * @param <T> the type of the config entry type information object
     */
    @FunctionalInterface
    public interface WidgetBuilder <S, T extends ConfigEntry.EntryTypeInfo<S>> {
        ConfigEntryWidget<S> build(int x, int y, int width, int height,
                                   T entryTypeInfo, ConfigEntry<S> entry, ConfigEntryWidget.TextProvider<S> textProvider);
    }
}
