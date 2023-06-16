package ir.mehradn.mehradconfig.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;

@Environment(EnvType.CLIENT)
public class ConfigScreenBuilder {
    /**
     * Provides a number based on the properties of the screen. The number is usually width or height of a widget.
     */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface NumberProvider {
        int get(int width, int height, Font font);
    }

    /**
     * All of the properties for creating a config screen.
     *
     * @param widgetWidth      width of the entry widgets
     * @param buttonWidth      width of the save and cancel button
     * @param descriptionY     y of the description text
     * @param descriptionWidth width of the description text
     * @param onSave           the action that should happen when the save button is pressed
     * @param onCancel         the action that should happen when the cancel button is pressed
     */
    @Environment(EnvType.CLIENT)
    public record ScreenProperties(
        NumberProvider widgetWidth,
        NumberProvider buttonWidth,
        NumberProvider descriptionY,
        NumberProvider descriptionWidth,
        Button.OnPress onSave,
        Button.OnPress onCancel
    ) { }
}
