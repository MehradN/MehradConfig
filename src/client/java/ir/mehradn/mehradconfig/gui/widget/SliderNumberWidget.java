package ir.mehradn.mehradconfig.gui.widget;

import ir.mehradn.mehradconfig.entry.ConfigEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractSliderButton;

/**
 * SliderNumberWidget is a widget for displaying and updating integer config entries in gui using a slider.
 * It works for config entries that have a constant min and max.
 */
@Environment(EnvType.CLIENT)
public class SliderNumberWidget extends ConfigEntryWidget<Integer> {
    /**
     * @param min          the minimum value allowed
     * @param max          the maximum value allowed
     * @param entry        the config entry that will get updated by this widget
     * @param textProvider a text provider for setting the message of the config entry widget
     */
    public SliderNumberWidget(int x, int y, int width, int height,
                              int min, int max, ConfigEntry<Integer> entry, TextProvider textProvider) {
        super(entry, textProvider, new Widget(x, y, width, height, min, max, entry, textProvider));
        ((Widget)this.widget).setReport(this::reportValueChange);
    }

    @Override
    public void reportValueChange() {
        super.reportValueChange();
        ((Widget)this.widget).updateSlider();
    }

    @Environment(EnvType.CLIENT)
    private static class Widget extends AbstractSliderButton {
        private final int min;
        private final int max;
        private final ConfigEntry<Integer> entry;
        private Runnable report;

        public Widget(int x, int y, int width, int height,
                      int min, int max, ConfigEntry<Integer> entry, TextProvider textProvider) {
            super(x, y, width, height, textProvider.get(entry), valueToSlider(min, max, entry.get()));
            this.min = min;
            this.max = max;
            this.entry = entry;
        }

        public void setReport(Runnable report) {
            this.report = report;
        }

        public void updateSlider() {
            this.value = valueToSlider(this.min, this.max, this.entry.get());
        }

        @Override
        protected void updateMessage() {
            this.report.run();
        }

        @Override
        protected void applyValue() {
            this.entry.set(sliderToValue(this.min, this.max, this.value));
        }

        private static int sliderToValue(int min, int max, double slider) {
            return (int)Math.round(min + slider * (max - min));
        }

        private static double valueToSlider(int min, int max, int value) {
            return (double)(value - min) / (max - min);
        }
    }
}
