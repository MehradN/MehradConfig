package ir.mehradn.mehradconfig.gui.widget;

import ir.mehradn.mehradconfig.entry.ConfigEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

/**
 * BooleanWidget is a widget for displaying and updating boolean config entries in gui.
 */
@Environment(EnvType.CLIENT)
public class BooleanWidget extends ConfigEntryWidget<Boolean> {
    /**
     * @param entry        the config entry that will get updated by this widget
     * @param textProvider a {@link ConfigEntryWidget.TextProvider}
     */
    public BooleanWidget(int x, int y, int width, int height,
                         ConfigEntry<Boolean> entry, TextProvider<Boolean> textProvider) {
        super(entry, textProvider, new Widget(x, y, width, height, entry, textProvider));
        ((Widget)this.widget).setReport(this::reportValueChange);
    }

    private static class Widget extends AbstractButton {
        private final ConfigEntry<Boolean> entry;
        private final TextProvider<Boolean> textProvider;
        private Runnable report;

        public Widget(int x, int y, int width, int height,
                      ConfigEntry<Boolean> entry, TextProvider<Boolean> textProvider) {
            super(x, y, width, height, textProvider.get(entry));
            this.entry = entry;
            this.textProvider = textProvider;
        }

        public void setReport(Runnable report) {
            this.report = report;
        }

        @Override
        public void onPress() {
            this.entry.set(!this.entry.get());
            this.report.run();
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
            narrationElementOutput.add(NarratedElementType.TITLE, createNarrationMessage());
            if (this.active) {
                Component text = this.textProvider.get(this.entry, !this.entry.get());
                if (this.isFocused()) {
                    narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.cycle_button.usage.focused", text));
                } else {
                    narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.cycle_button.usage.hovered", text));
                }
            }
        }
    }
}
