package ir.mehradn.mehradconfigtest.config;

import ir.mehradn.mehradconfig.MehradConfig;
import ir.mehradn.mehradconfig.entry.*;
import ir.mehradn.mehradconfigtest.MehradConfigTest;
import java.util.List;

public class TestConfig extends MehradConfig {
    public final BooleanEntry testBoolean = new BooleanEntry("testBoolean", true);
    public final EnumEntry<TestEnum> testEnum = new EnumEntry<>("testEnum", TestEnum.class, TestEnum.VALUE_2);
    public final NumberEntry testNumber = new NumberEntry("testNumber", 21, 69, 34);
    public final OptionalEntry<Boolean> testOptional = new BooleanEntry("testOptional", false).makeOptional(this.testBoolean);

    public TestConfig() {
        super(MehradConfigTest.MOD_ID);
    }

    @Override
    public List<ConfigEntry<?>> getEntries() {
        return List.of(
            this.testBoolean,
            this.testEnum,
            this.testNumber,
            this.testOptional
        );
    }

    @Override
    public MehradConfig createNewInstance() {
        return new TestConfig();
    }

    public enum TestEnum {
        VALUE_1,
        VALUE_2,
        VALUE_3,
        VALUE_4
    }
}
