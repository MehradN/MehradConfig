package ir.mehradn.mehradconfig.entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

/**
 * The boolean config entry.
 *
 * @see ConfigEntry
 */
public class BooleanEntry extends DefaultValueEntry<Boolean> {
    /**
     * @param name         the name of the entry
     * @param defaultValue the default value of the entry.
     */
    public BooleanEntry(String name, boolean defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public Component getTranslatedValue(String modId, @NotNull Boolean value) {
        return Component.translatable(modId + ".config.bool." + getName() + "." + value);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(get());
    }

    @Override
    public void fromJson(JsonElement json) {
        set(json.getAsBoolean());
    }

    @Override
    public void writeToBuf(FriendlyByteBuf buf) {
        buf.writeBoolean(get());
    }

    @Override
    public void readFromBuf(FriendlyByteBuf buf) {
        set(buf.readBoolean());
    }

    @Override
    public Class<Boolean> entryTypeClass() {
        return Boolean.class;
    }

    @Override
    public BooleanTypeInfo entryTypeInfo() {
        return new BooleanTypeInfo();
    }

    public static final class BooleanTypeInfo implements EntryTypeInfo {
        public static final String ID = "mehrad-config:boolean";

        @Override
        public String id() {
            return ID;
        }
    }
}
