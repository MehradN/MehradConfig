package ir.mehradn.mehradconfig.entry;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

/**
 * The enum config entry.
 *
 * @param <T> type of the enum
 * @see ConfigEntry
 */
public class EnumEntry <T extends Enum<T>> extends DefaultValueEntry<T> {
    private static final Gson GSON = new Gson();
    private final Class<T> enumClass;

    /**
     * The main constructor.
     *
     * @param name         the name of the entry
     * @param enumClass    the class object of the enum
     * @param defaultValue the default value of the entry.
     */
    public EnumEntry(String name, Class<T> enumClass, T defaultValue) {
        super(name, defaultValue);
        this.enumClass = enumClass;
    }

    @Override
    public Component getTranslatedValue(String modId, @NotNull T value) {
        return Component.translatable(modId + ".mehrad-config.enum." + getName() + "." + value);
    }

    @Override
    public JsonElement toJson() {
        return GSON.toJsonTree(get(), this.enumClass);
    }

    @Override
    public void fromJson(JsonElement json) {
        set(GSON.fromJson(json, this.enumClass));
    }

    @Override
    public void writeToBuf(FriendlyByteBuf buf) {
        buf.writeEnum(get());
    }

    @Override
    public void readFromBuf(FriendlyByteBuf buf) {
        set(buf.readEnum(this.enumClass));
    }

    @Override
    public EnumTypeInfo<T> entryTypeInfo() {
        return new EnumTypeInfo<>(this.enumClass);
    }

    public record EnumTypeInfo <T extends Enum<T>>(Class<T> enumClass) implements EntryTypeInfo<T> {
        public static final String ID = "mehrad-config:enum";

        @Override
        public String id() {
            return ID;
        }

        @Override
        public Class<T> typeClass() {
            return this.enumClass;
        }
    }
}
