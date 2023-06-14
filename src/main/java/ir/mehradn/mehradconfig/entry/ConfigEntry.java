package ir.mehradn.mehradconfig.entry;

import com.google.gson.JsonElement;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

/**
 * ConfigEntry is an interface for storing and managing config values.
 *
 * @param <T> the type of the config value
 * @see BooleanEntry
 * @see NumberEntry
 * @see EnumEntry
 */
public interface ConfigEntry <T> {
    /**
     * The name of the entry, used for translation and json
     *
     * @return the name of the entry
     */
    String getName();

    /**
     * @return the value of this entry
     */
    T get();

    /**
     * Sets the value of this entry.
     *
     * @param value the new value
     */
    void set(T value);

    /**
     * Resets the value to the default value.
     */
    void reset();

    /**
     * @return whether the current value is same as the default value
     */
    boolean isDefault();

    /**
     * Whether the current value should be written. (in json, buffers, ...)
     *
     * @return whether the current value should be written
     */
    boolean shouldWrite();

    /**
     * Copies this entry's value to the given entry
     *
     * @param entry the entry to copy the value to
     */
    void copyTo(ConfigEntry<T> entry);

    /**
     * Returns a translated component of this entry's title. The title is a short text describing this entry.
     *
     * @param modId the modId that provides the translations
     * @return a translated component of this entry's title
     */
    default Component getTranslatedTitle(String modId) {
        return Component.translatable(modId + ".mehrad-config.title." + getName());
    }

    /**
     * Returns a translated component of this entry's description. The description is a long text describing this entry.
     *
     * @param modId the modId that provides the translations
     * @return a translated component of this entry's description
     */
    default Component getTranslatedDescription(String modId) {
        return Component.translatable(modId + ".mehrad-config.description." + getName());
    }

    /**
     * @param modId the modId that provides the translation
     * @param value the value to be translated
     * @return a translated component of the given value
     */
    Component getTranslatedValue(String modId, @NotNull T value);

    /**
     * @param modId the modId that provides the translation
     * @return a translated component of the current value
     */
    default Component getTranslatedValue(String modId) {
        return getTranslatedValue(modId, get());
    }

    /**
     * @return a json element describing the current value
     */
    JsonElement toJson();

    /**
     * Sets the value from a json element. It is safe to assume that the json was generated by {@link #toJson}.
     *
     * @param json a json element describing a value, most likely created by {@code toJson}
     */
    void fromJson(JsonElement json);

    /**
     * Writes the current value to the given packet byte buffer.
     *
     * @param buf the buffer to write to
     */
    void writeToBuf(FriendlyByteBuf buf);

    /**
     * Reads and sets the value from the given packet byte buffer.
     *
     * @param buf the buffer to read from
     */
    void readFromBuf(FriendlyByteBuf buf);

    /**
     * @return the entry type class object
     */
    Class<T> entryTypeClass();

    /**
     * @return the entry type info
     */
    EntryTypeInfo entryTypeInfo();

    /**
     * EntryTypeInfo is an interface for sharing the type information of config entries. It is mainly used in factories.
     */
    interface EntryTypeInfo {
        String id();
    }
}