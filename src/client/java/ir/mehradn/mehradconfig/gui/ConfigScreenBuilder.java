package ir.mehradn.mehradconfig.gui;

import ir.mehradn.mehradconfig.MehradConfig;
import ir.mehradn.mehradconfig.entry.ConfigEntry;
import ir.mehradn.mehradconfig.entrypoint.MehradConfigEntrypoint;
import ir.mehradn.mehradconfig.gui.screen.CompactConfigScreen;
import ir.mehradn.mehradconfig.gui.screen.MehradConfigScreen;
import ir.mehradn.mehradconfig.gui.screen.ResettableConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * ConfigScreenBuilder is a builder class for creating new {@link MehradConfigScreen config screens}. It allows you to customize the properties of
 * the screen.
 */
@Environment(EnvType.CLIENT)
public class ConfigScreenBuilder {
    private final EntryWidgetFactory entryWidgetFactory = new EntryWidgetFactory();
    private ConfigScreenConstructor constructor = null;
    private ScreenProperties defaultProperties = null;
    private NumberProvider widgetWidth = null;
    private NumberProvider buttonWidth = null;
    private NumberProvider descriptionY = null;
    private NumberProvider descriptionWidth = null;
    private ButtonAction onSave = null;
    private ButtonAction onCancel = null;

    public ConfigScreenBuilder() {
        this.entryWidgetFactory.addDefaultBuilders();
    }

    /**
     * Sets the type of the screen that should be used to modify this config.
     *
     * @param constructor       the constructor of the config screen
     * @param defaultProperties the default properties of the screen
     */
    public ConfigScreenBuilder setScreenType(ConfigScreenConstructor constructor, ScreenProperties defaultProperties) {
        this.constructor = constructor;
        this.defaultProperties = defaultProperties;
        return this;
    }

    /**
     * Sets the type of the screen that should be used to modify this config.
     *
     * @param constructor       the constructor of the config screen
     * @param defaultProperties the default screen type to copy the properties from
     */
    public ConfigScreenBuilder setScreenType(ConfigScreenConstructor constructor, DefaultScreens defaultProperties) {
        return setScreenType(constructor, defaultProperties.defaultProperties);
    }

    /**
     * Sets the type of the screen that should be used to modify this config.
     *
     * @param screenType the default screen type to use
     */
    public ConfigScreenBuilder setScreenType(DefaultScreens screenType) {
        return setScreenType(screenType.constructor, screenType.defaultProperties);
    }

    /**
     * Sets the width of the entry widgets.
     * <p>
     * For the {@link DefaultScreens#COMPACT default compact screen type}, the default value is 150. <br>
     * For the {@link DefaultScreens#RESETTABLE default ressetable screen type}, the default value is 200. <br>
     *
     * @param widgetWidth a number provider for determining the width of the entry widgets
     * @see ir.mehradn.mehradconfig.gui.widget.ConfigEntryWidget
     */
    public ConfigScreenBuilder setWidgetWidth(NumberProvider widgetWidth) {
        this.widgetWidth = widgetWidth;
        return this;
    }

    /**
     * Sets the width of the entry widgets.
     * <p>
     * For the {@link DefaultScreens#COMPACT default compact screen type}, the default value is 150. <br>
     * For the {@link DefaultScreens#RESETTABLE default ressetable screen type}, the default value is 200. <br>
     *
     * @param widgetWidth the width of the entry widgets as a constant number.
     * @see ir.mehradn.mehradconfig.gui.widget.ConfigEntryWidget
     */
    public ConfigScreenBuilder setWidgetWidth(int widgetWidth) {
        return setWidgetWidth((w, h, f) -> widgetWidth);
    }

    /**
     * Sets the width of the save and cancel buttons.
     * <p>
     * For the {@link DefaultScreens#COMPACT default compact screen type}, the default value is 150. <br>
     * For the {@link DefaultScreens#RESETTABLE default ressetable screen type}, the default value is 125. <br>
     *
     * @param buttonWidth a number provider for determining the width of the save and cancel buttons
     */
    public ConfigScreenBuilder setButtonWidth(NumberProvider buttonWidth) {
        this.buttonWidth = buttonWidth;
        return this;
    }

    /**
     * Sets the width of the save and cancel buttons.
     * <p>
     * For the {@link DefaultScreens#COMPACT default compact screen type}, the default value is 150. <br>
     * For the {@link DefaultScreens#RESETTABLE default ressetable screen type}, the default value is 125. <br>
     *
     * @param buttonWidth the width of the save and cancel buttons as a constant number
     */
    public ConfigScreenBuilder setButtonWidth(int buttonWidth) {
        return setButtonWidth((w, h, f) -> buttonWidth);
    }

    /**
     * Sets the y position of the description text.
     * <p>
     * For all of the {@link DefaultScreens default screen types}, the default value is {@code height - 3 * font.lineHeight - 40}. <br>
     *
     * @param descriptionY a number provider for determining the y position of the description text
     */
    public ConfigScreenBuilder setDescriptionY(NumberProvider descriptionY) {
        this.descriptionY = descriptionY;
        return this;
    }

    /**
     * Sets the y position of the description text.
     * <p>
     * For all of the {@link DefaultScreens default screen types}, the default value is {@code height - 3 * font.lineHeight - 40}. <br>
     *
     * @param descriptionY the y position of the description text as a constant number
     */
    public ConfigScreenBuilder setDescriptionY(int descriptionY) {
        return setDescriptionY((w, h, f) -> descriptionY);
    }

    /**
     * Sets the width of the description text.
     * <p>
     * For all of the {@link DefaultScreens default screen types}, the default value is 300. <br>
     *
     * @param descriptionWidth a number provider for determining the width of the description text
     */
    public ConfigScreenBuilder setDescriptionWidth(NumberProvider descriptionWidth) {
        this.descriptionWidth = descriptionWidth;
        return this;
    }

    /**
     * Sets the width of the description text.
     * <p>
     * For all of the {@link DefaultScreens default screen types}, the default value is 300. <br>
     *
     * @param descriptionWidth the width of the description text as a constant number
     */
    public ConfigScreenBuilder setDescriptionWidth(int descriptionWidth) {
        return setDescriptionWidth((w, h, f) -> descriptionWidth);
    }

    /**
     * Sets the action that should happen when the save button is pressed. Saving/updating the config will happen before this action.
     * <p>
     * For all of the {@link DefaultScreens default screen types}, the default action sets the current screen to the parent screen.
     *
     * @param onSave the action that should happen when the save button is pressed
     * @see #buildForInstance
     * @see #buildAndLoad
     */
    public ConfigScreenBuilder setOnSave(ButtonAction onSave) {
        this.onSave = onSave;
        return this;
    }

    /**
     * Sets the action that should happen when the cancel button is pressed.
     * <p>
     * For all of the {@link DefaultScreens default screen types}, the default action sets the current screen to the parent screen.
     *
     * @param onCancel the action that should happen when the cancel button is pressed
     */
    public ConfigScreenBuilder setOnCancel(ButtonAction onCancel) {
        this.onCancel = onCancel;
        return this;
    }

    /**
     * Adds a widget builder to the entry widget factory. The default widget builders are added to the factory before this method. Feel free to
     * override them if you want to use an alternative widget for the default entry types.
     *
     * @see EntryWidgetFactory#addWidgetBuilder
     * @see EntryWidgetFactory#addDefaultBuilders
     */
    public <S, T extends ConfigEntry.EntryTypeInfo<S>> ConfigScreenBuilder addWidgetBuilder(String entryTypeId,
                                                                                            EntryWidgetFactory.WidgetBuilder<S, T> widgetBuilder) {
        this.entryWidgetFactory.addWidgetBuilder(entryTypeId, widgetBuilder);
        return this;
    }

    /**
     * Builds a config screen for the given config instance. The instance only gets updated if the save button is pressed. The updating happens
     * before the {@link #setOnSave} action. <b> {@link #setScreenType} must be called before this method. </b>
     *
     * @param instance     the config instance to modify with this screen
     * @param parentScreen the parent screen
     * @return a config screen for modifying the given config instance
     */
    public MehradConfigScreen buildForInstance(MehradConfig instance, Screen parentScreen) {
        if (this.constructor == null)
            throw new IllegalStateException("setScreenType must be called before build methods");

        MehradConfig modifiedConfig = instance.createNewInstance();
        instance.copyTo(modifiedConfig);
        ScreenProperties properties = buildProperties(wrapOnSave(() -> modifiedConfig.copyTo(instance)));

        return this.constructor.create(modifiedConfig, properties, this.entryWidgetFactory, parentScreen);
    }

    /**
     * Builds a config screen for the given config instance. The instance only gets updated if the save button is pressed. The updating happens
     * before the {@link #setOnSave} action. <b> {@link #setScreenType} must be called before this method. </b>
     * <p>
     * This version of the method will fetch the parent screen via {@code Minecraft.getInstance().screen}.
     *
     * @param instance the config instance to modify with this screen
     * @return a config screen for modifying the given config instance
     */
    public MehradConfigScreen buildForInstance(MehradConfig instance) {
        return buildForInstance(instance, Minecraft.getInstance().screen);
    }

    /**
     * Loads, modifies and saves a config instance with a config screen. The config only gets save if the save button is pressed. The saving happens
     * before the {@link #setOnSave} action. <b> {@link #setScreenType} must be called before this method. </b>
     *
     * @param configConstructor a constructor for the type of the config to load, modify and save
     * @param parentScreen      the parent screen
     * @return a config screen for modifying the loaded config instance, it's {@code null} if something fails while loading the config
     */
    public @Nullable MehradConfigScreen buildAndLoad(Supplier<MehradConfig> configConstructor, Screen parentScreen) {
        if (this.constructor == null)
            throw new IllegalStateException("setScreenType must be called before build methods");

        MehradConfig config = configConstructor.get();
        try {
            config.load();
        } catch (IOException e) {
            MehradConfigEntrypoint.LOGGER.warn("Failed to load the config for \"" + config.modId + "\"!", e);
        }

        ScreenProperties properties = buildProperties(wrapOnSave(() -> {
            try {
                config.save();
            } catch (IOException e) {
                MehradConfigEntrypoint.LOGGER.error("Failed to save the config for \"" + config.modId + "\"!", e);
            }
        }));

        return this.constructor.create(config, properties, this.entryWidgetFactory, parentScreen);
    }

    /**
     * Loads, modifies and saves a config instance with a config screen. The config only gets save if the save button is pressed. The saving happens
     * before the {@link #setOnSave} action. <b> {@link #setScreenType} must be called before this method. </b>
     * <p>
     * This version of the method will fetch the parent screen via {@code Minecraft.getInstance().screen}.
     *
     * @param configConstructor a constructor for the type of the config to load, modify and save
     * @return a config screen for modifying the loaded config instance, it's {@code null} if something fails while loading the config
     */
    public @Nullable MehradConfigScreen buildAndLoad(Supplier<MehradConfig> configConstructor) {
        return buildAndLoad(configConstructor, Minecraft.getInstance().screen);
    }

    private ScreenProperties buildProperties(ButtonAction onSave) {
        if (this.defaultProperties == null)
            throw new IllegalStateException();
        return new ScreenProperties(
            Objects.requireNonNullElse(this.widgetWidth, this.defaultProperties.widgetWidth),
            Objects.requireNonNullElse(this.buttonWidth, this.defaultProperties.buttonWidth),
            Objects.requireNonNullElse(this.descriptionY, this.defaultProperties.descriptionY),
            Objects.requireNonNullElse(this.descriptionWidth, this.defaultProperties.descriptionWidth),
            onSave,
            Objects.requireNonNullElse(this.onCancel, this.defaultProperties.onCancel)
        );
    }

    private ButtonAction wrapOnSave(Runnable action) {
        if (this.defaultProperties == null)
            throw new IllegalStateException();
        ButtonAction action2 = Objects.requireNonNullElse(this.onSave, this.defaultProperties.onSave);
        return (minecraft, thisScreen, parentScreen) -> {
            action.run();
            action2.onClick(minecraft, thisScreen, parentScreen);
        };
    }

    /**
     * An enum of default config screens implemented by this library.
     */
    @Environment(EnvType.CLIENT)
    public enum DefaultScreens {
        /**
         * @see ir.mehradn.mehradconfig.gui.screen.CompactConfigScreen
         */
        COMPACT(CompactConfigScreen::new, CompactConfigScreen.DEFAULT_PROPERTIES),
        /**
         * @see ir.mehradn.mehradconfig.gui.screen.ResettableConfigScreen
         */
        RESETTABLE(ResettableConfigScreen::new, ResettableConfigScreen.DEFAULT_PROPERTIES);
        /**
         * The constructor for creating a new instance of the screen.
         */
        public final ConfigScreenConstructor constructor;
        /**
         * The default properties of this screen.
         */
        public final ScreenProperties defaultProperties;

        DefaultScreens(ConfigScreenConstructor constructor, ScreenProperties defaultProperties) {
            this.constructor = constructor;
            this.defaultProperties = defaultProperties;
        }
    }

    /**
     * Definition of the constructor of config screens.
     */
    @Environment(EnvType.CLIENT)
    public interface ConfigScreenConstructor {
        MehradConfigScreen create(MehradConfig config, ScreenProperties properties, EntryWidgetFactory widgetFactory, Screen parentScreen);
    }

    /**
     * Provides a number based on the properties of the screen. The number is usually width or height of a widget.
     */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface NumberProvider {
        int get(int width, int height, Font font);
    }

    /**
     * Actions that should happen when a button is pressed.
     */
    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface ButtonAction {
        void onClick(Minecraft minecraft, MehradConfigScreen thisScreen, Screen parentScreen);
    }

    /**
     * All of the properties for creating a config screen.
     *
     * @param widgetWidth      width of the entry widgets
     * @param buttonWidth      width of the save and cancel buttons
     * @param descriptionY     y position of the description text
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
        ButtonAction onSave,
        ButtonAction onCancel
    ) { }
}
