package ir.mehradn.mehradconfig.gui.screen;

import ir.mehradn.mehradconfig.MehradConfig;
import ir.mehradn.mehradconfig.entry.ConfigEntry;
import ir.mehradn.mehradconfig.gui.ConfigScreenBuilder;
import ir.mehradn.mehradconfig.gui.EntryWidgetFactory;
import ir.mehradn.mehradconfig.gui.widget.ConfigEntryWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * MehradConfigScreen is an abstract gui for {@link MehradConfig}. Extend this or one of its subclasses to create your own custom gui.
 *
 * @see CompactConfigScreen
 * @see ResettableConfigScreen
 */
@Environment(EnvType.CLIENT)
public abstract class MehradConfigScreen extends Screen {
    /**
     * The config to display and modify.
     */
    public final MehradConfig config;
    /**
     * Some screen properties to base this screen of.
     */
    protected final ConfigScreenBuilder.ScreenProperties properties;
    /**
     * An {@code EntryWidgetFactory} to create widgets for config entries.
     */
    protected final EntryWidgetFactory entryWidgetFactory;
    /**
     * A text provider for setting the messages of the config entry widgets.
     */
    protected final ConfigEntryWidget.TextProvider textProvider;
    /**
     * The parent screen. The screen that opened this screen.
     */
    protected final Screen parentScreen;
    /**
     * A list of the config entry widgets being displayed. Initialized after {@link #init()}.
     */
    protected List<ConfigEntryWidget<?>> entryWidgets;
    /**
     * A {@code MultiLineTextWidget} for displaying the description of config entries. Initialized after {@link #init()}.
     */
    protected MultiLineTextWidget hoverText;
    /**
     * A button for saving the config and closing the screen. Initialized after {@link #init()}.
     */
    protected Button saveButton;
    /**
     * A Button for canceling the operation and closing the screen. Initialized after {@link #init()}.
     */
    protected Button cancelButton;

    /**
     * @param config             the config to display and modify
     * @param properties         some screen properties to base this screen of
     * @param entryWidgetFactory an {@code EntryWidgetFactory} to create widgets for config entries
     * @param textProvider       a text provider for setting the messages of the config entry widgets
     * @param parentScreen       the parent screen
     */
    protected MehradConfigScreen(MehradConfig config, ConfigScreenBuilder.ScreenProperties properties, EntryWidgetFactory entryWidgetFactory,
                                 ConfigEntryWidget.TextProvider textProvider, Screen parentScreen) {
        super(Component.translatable(config.modId + ".mehrad-config.screenTitle." + config.name));
        this.config = config;
        this.properties = properties;
        this.entryWidgetFactory = entryWidgetFactory;
        this.textProvider = textProvider;
        this.parentScreen = parentScreen;
    }

    @Override
    public void init() {
        int textY = this.properties.descriptionY().get(this.width, this.height, this.font);
        int textWidth = this.properties.descriptionWidth().get(this.width, this.height, this.font);
        this.hoverText = addRenderableWidget(new MultiLineTextWidget((this.width - textWidth) / 2, textY, Component.empty(), this.font)
            .setMaxWidth(textWidth));

        this.entryWidgets = new ArrayList<>();
        List<ConfigEntry<?>> entries = this.config.getEntries();
        for (int i = 0; i < entries.size(); i++) {
            ScreenRectangle rect = getEntryWidgetBounds(i);
            ConfigEntryWidget<?> widget = this.entryWidgetFactory.build(rect.left(), rect.top(), rect.width(), rect.height(),
                entries.get(i), this.textProvider);
            addRenderableWidget(widget.widget);
            this.entryWidgets.add(widget);
        }

        int buttonWidth = this.properties.buttonWidth().get(this.width, this.height, this.font);
        this.cancelButton = addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, (btn) -> onClose())
            .bounds(this.width / 2 - buttonWidth - 2, this.height - 35, buttonWidth, 20).build());
        this.saveButton = addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (btn) -> onSave())
            .bounds(this.width / 2 + 2, this.height - 35, buttonWidth, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Component description = Component.empty();
        for (int i = 0; i < this.entryWidgets.size(); i++) {
            ConfigEntryWidget<?> widget = this.entryWidgets.get(i);
            ScreenRectangle hoverRegion = getEntryHoverRegion(i);
            if (hoverRegion.left() <= mouseX && mouseX <= hoverRegion.right() &&
                hoverRegion.top() <= mouseY && mouseY <= hoverRegion.bottom())
                description = widget.entry.getTranslatedDescription(this.config.modId);
        }
        this.hoverText.setMessage(description);

        renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        this.properties.onCancel().onClick(this.minecraft, this, this.parentScreen);
    }

    /**
     * @param i the index of the config entry
     * @return the bounds of the entry widget
     */
    protected abstract ScreenRectangle getEntryWidgetBounds(int i);

    /**
     * A hover region is an area that when the mouse is hovering over it, it is considered that the mouse is hovering the corresponding entry widget.
     * It should fully cover the widget's bounding box, and it should also cover the empty are between the widgets.
     * (For example if there is a 4 pixel gap between the entry widgets, this gaps should be covered by these regions as well)
     * Avoid having these regions overlapping, as it might cause some unknown behaviour.
     *
     * @param i the index of the config entry
     * @return the bounds of the entry widget hover region
     */
    protected abstract ScreenRectangle getEntryHoverRegion(int i);

    private void onSave() {
        this.properties.onSave().onClick(this.minecraft, this, this.parentScreen);
    }
}
