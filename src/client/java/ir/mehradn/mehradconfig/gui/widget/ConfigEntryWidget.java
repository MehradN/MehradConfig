package ir.mehradn.mehradconfig.gui.widget;

import ir.mehradn.mehradconfig.entry.ConfigEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;

/**
 * ConfigEntryWidget is an interface used for config screen widgets.
 *
 * @param <T> the type of the config value
 */
@Environment(EnvType.CLIENT)
public abstract class ConfigEntryWidget <T> {
    /**
     * The config entry that gets updated by this widget.
     */
    public final ConfigEntry<T> entry;
    /**
     * The actual widget that should be rendered
     */
    public final AbstractWidget widget;
    protected final TextProvider<T> textProvider;
    private Consumer<ConfigEntry<T>> onChange = null;

    /**
     * @param entry        the config entry that will get updated by this widget
     * @param textProvider a {@link ConfigEntryWidget.TextProvider}
     * @param widget       the actual widget that should be rendered
     */
    protected ConfigEntryWidget(ConfigEntry<T> entry, TextProvider<T> textProvider, AbstractWidget widget) {
        this.entry = entry;
        this.widget = widget;
        this.textProvider = textProvider;
    }

    /**
     * The action passed to this method will get called when the value of the config entry changes. Calling this method multiple times will override
     * itself. Calling it with {@code null} will stop the method from being called.
     *
     * @param onChange the method to be called when the value of the config entry changes
     */
    public void onValueChange(@Nullable Consumer<ConfigEntry<T>> onChange) {
        this.onChange = onChange;
    }

    /**
     * This method must be called each time the value of the config entry changes. No matter if it happened inside the class or outside the class.
     */
    public void reportValueChange() {
        onValueChanged();
        if (this.onChange != null)
            this.onChange.accept(this.entry);
    }

    /**
     * This method gets called when the value of the config entry changes. Use it to update the gui.
     */
    protected void onValueChanged() {
        this.widget.setMessage(this.textProvider.get(this.entry));
    }

    /**
     * TextProvider is an interface for displaying a config entry as a {@link Component}. Should be based on the methods in "See Also".
     *
     * @param <T> The type of the config entry value
     * @see ConfigEntry#getTranslatedTitle
     * @see ConfigEntry#getTranslatedValue
     */
    public interface TextProvider <T> {
        /**
         * The returned text should be based on the current value of the entry,
         *
         * @see ConfigEntry#get
         * @see ConfigEntry#getTranslatedTitle(String)
         */
        Component get(ConfigEntry<T> entry);

        /**
         * The returned text should be based on the given value.
         *
         * @see ConfigEntry#getTranslatedValue(String, Object)
         */
        Component get(ConfigEntry<T> entry, T value);
    }
}
