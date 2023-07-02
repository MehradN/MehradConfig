package ir.mehradn.mehradconfigtest;

import ir.mehradn.mehradconfig.entrypoint.ModMenuSupport;
import ir.mehradn.mehradconfig.gui.ConfigScreenBuilder;
import ir.mehradn.mehradconfigtest.config.TestConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MehradConfigTest implements ModInitializer {
    public static final String MOD_ID = "mehrad-config-test";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ConfigScreenBuilder.DefaultScreens defaultScreen =
            ConfigScreenBuilder.DefaultScreens.RESETTABLE;
            //ConfigScreenBuilder.DefaultScreens.COMPACT;
        ConfigScreenBuilder builder = new ConfigScreenBuilder()
            .setScreenType(defaultScreen)
            //.setWidgetWidth(175)
            //.setButtonWidth(175)
            //.setDescriptionY((width, height, font) -> height / 2)
            //.setDescriptionWidth(500)
            //.setOnSave((minecraft, thisScreen, parentScreen) -> { LOGGER.info("SAVED!"); minecraft.setScreen(parentScreen); })
            //.setOnCancel((minecraft, thisScreen, parentScreen) -> { LOGGER.info("CANCELED!"); minecraft.setScreen(parentScreen); })
            ;
        ModMenuSupport.register(MOD_ID, TestConfig::new, builder);
    }
}
