package ir.mehradn.mehradconfig.entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

/**
 * The number config entry.
 *
 * @see ConfigEntry
 */
public class NumberEntry extends DefaultValueEntry<Integer> {
    private final int min;
    private final int max;

    /**
     * The main constructor.
     *
     * @param name         the name of the entry
     * @param min          the minimum value allowed
     * @param max          the maximum value allowed
     * @param defaultValue the default value of the entry.
     */
    public NumberEntry(String name, int min, int max, int defaultValue) {
        super(name, trim(defaultValue, min, max));
        if (max < min)
            throw new IllegalArgumentException("Min cannot be more than max!");
        this.min = min;
        this.max = max;
    }

    @Override
    public Component getTranslatedValue(String modId, @NotNull Integer value) {
        return Component.literal(value.toString());
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(get());
    }

    @Override
    public void fromJson(JsonElement json) {
        set(json.getAsInt());
    }

    @Override
    public void writeToBuf(FriendlyByteBuf buf) {
        buf.writeInt(get());
    }

    @Override
    public void readFromBuf(FriendlyByteBuf buf) {
        set(buf.readInt());
    }

    @Override
    public NumberTypeInfo entryTypeInfo() {
        return new NumberTypeInfo(this.min, this.max);
    }

    @Override
    protected Integer trim(Integer value) {
        return trim(value, this.min, this.max);
    }

    private static int trim(int value, int min, int max) {
        return Mth.clamp(value, min, max);
    }

    public record NumberTypeInfo(int min, int max) implements EntryTypeInfo<Integer> {
        public static final String ID = "mehrad-config:number";

        @Override
        public String id() {
            return ID;
        }

        @Override
        public Class<Integer> typeClass() {
            return Integer.class;
        }
    }
}
