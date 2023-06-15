package ir.mehradn.mehradconfig.entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

/**
 * The number config entry.
 *
 * @see ConfigEntry
 */
public class NumberEntry extends DefaultValueEntry<Short> {
    private final short min;
    private final short max;

    /**
     * The main constructor.
     *
     * @param name         the name of the entry
     * @param min          the minimum value allowed
     * @param max          the maximum value allowed
     * @param defaultValue the default value of the entry.
     */
    public NumberEntry(String name, short min, short max, short defaultValue) {
        super(name, defaultValue);
        if (max < min)
            throw new IllegalArgumentException("Min cannot be more than max!");
        this.min = min;
        this.max = max;
    }

    @Override
    public Component getTranslatedValue(String modId, @NotNull Short value) {
        return Component.literal(value.toString());
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(get());
    }

    @Override
    public void fromJson(JsonElement json) {
        set(json.getAsShort());
    }

    @Override
    public void writeToBuf(FriendlyByteBuf buf) {
        buf.writeShort(get());
    }

    @Override
    public void readFromBuf(FriendlyByteBuf buf) {
        set(buf.readShort());
    }

    @Override
    public NumberTypeInfo entryTypeInfo() {
        return new NumberTypeInfo(this.min, this.max);
    }

    @Override
    protected Short trim(Short value) {
        if (value < this.min)
            return this.min;
        if (value > this.max)
            return this.max;
        return value;
    }

    public record NumberTypeInfo(short min, short max) implements EntryTypeInfo<Short> {
        public static final String ID = "mehrad-config:number";

        @Override
        public String id() {
            return ID;
        }

        @Override
        public Class<Short> typeClass() {
            return Short.class;
        }
    }
}
