package ir.mehradn.mehradconfig.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ConfigScreenBuilder {
    /**
     * Provides a number based on the properties of the screen. Usually width or height of a widget.
     */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface NumberProvider {
        int get(int width, int height, Font font);
    }

    /**
     * Provides a rectangle based on the properties of the screen. Usually bounds of a button.
     */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface BoundsProvider {
        ScreenRectangle get(int width, int height, Font font);
    }

    /**
     * Properties for creating a custom button.
     *
     * @param message the message/text of the button
     * @param bounds  the bounds of the button
     * @param onPress the action that should happen when the button is clicked
     */
    @Environment(EnvType.CLIENT)
    public record CustomButton(
        Component message,
        BoundsProvider bounds,
        Button.OnPress onPress
    ) {
        /**
         * @return a button based on the given properties
         */
        public Button build(int width, int height, Font font) {
            ScreenRectangle rect = this.bounds.get(width, height, font);
            return Button.builder(this.message, this.onPress).bounds(rect.left(), rect.top(), rect.width(), rect.height()).build();
        }
    }

    /**
     * All of the properties for creating a config screen.
     *
     * @param widgetWidth       width of the entry widgets
     * @param descriptionHeight height of the description text
     * @param descriptionWidth  width of the description text
     * @param saveBounds        bounds of the save button
     * @param cancelBounds      bounds of the cancel button
     * @param onSave            the action that should happen when the save button is pressed, after the config has been updated
     * @param onCancel          the action that should happen when the cancel button is pressed
     * @param customButtons     a list of custom buttons to add to the screen
     */
    @Environment(EnvType.CLIENT)
    public record ScreenProperties(
        NumberProvider widgetWidth,
        NumberProvider descriptionHeight,
        NumberProvider descriptionWidth,
        BoundsProvider saveBounds,
        BoundsProvider cancelBounds,
        Button.OnPress onSave,
        Button.OnPress onCancel,
        List<CustomButton> customButtons
    ) { }
}
