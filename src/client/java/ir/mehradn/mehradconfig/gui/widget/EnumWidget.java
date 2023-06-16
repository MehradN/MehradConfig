package ir.mehradn.mehradconfig.gui.widget;

import ir.mehradn.mehradconfig.entry.ConfigEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

/**
 * EnumWidget is a widget for displaying and updating enum config entries in gui.
 *
 * @param <T> type of the enum
 */
@Environment(EnvType.CLIENT)
public class EnumWidget <T extends Enum<T>> extends ConfigEntryWidget<T> {
    /**
     * @param enumClass    the class object of the enum
     * @param entry        the config entry that will get updated by this widget
     * @param textProvider a {@link ConfigEntryWidget.TextProvider}
     */
    public EnumWidget(int x, int y, int width, int height,
                      Class<T> enumClass, ConfigEntry<T> entry, TextProvider textProvider) {
        super(entry, textProvider, new Widget<>(x, y, width, height, enumClass, entry, textProvider));
        ((Widget<?>)this.widget).setReport(this::reportValueChange);
    }

    private static class Widget <T extends Enum<T>> extends AbstractButton {
        private final Class<T> enumClass;
        private final ConfigEntry<T> entry;
        private final TextProvider textProvider;
        private Runnable report;

        public Widget(int x, int y, int width, int height,
                      Class<T> enumClass, ConfigEntry<T> entry, TextProvider textProvider) {
            super(x, y, width, height, textProvider.get(entry));
            this.enumClass = enumClass;
            this.entry = entry;
            this.textProvider = textProvider;
        }

        public void setReport(Runnable report) {
            this.report = report;
        }

        @Override
        public void onPress() {
            this.entry.set(next(this.entry.get()));
            this.report.run();
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            narrationElementOutput.add(NarratedElementType.TITLE, createNarrationMessage());
            if (this.active) {
                Component text = this.textProvider.get(this.entry, next(this.entry.get()));
                if (this.isFocused()) {
                    narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.cycle_button.usage.focused", text));
                } else {
                    narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.cycle_button.usage.hovered", text));
                }
            }
        }

        private T next(T value) {
            T[] values = this.enumClass.getEnumConstants();
            int i = value.ordinal() + 1;
            return (i > values.length ? values[0] : values[i]);
        }
    }
}
