package ir.mehradn.mehradconfig.entry;

import com.google.gson.JsonElement;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

/**
 * OptionalEntry is an implementation of {@link ConfigEntry}, Its default value is based on another {@code ConfigEntry}.
 *
 * @param <T> the type of the config value
 * @see ConfigEntry
 */
public class OptionalEntry <T> implements ConfigEntry<T> {
    private final ConfigEntry<T> optionalEntry;
    private final ConfigEntry<T> fallbackEntry;
    private boolean hasValue;

    /**
     * The main constructor.
     * <p>
     * <b>Note:</b> Always at the initial state after the constructor, the value of the optional entry is ignored.
     *
     * @param optionalEntry the config entry that should hold the optional value
     * @param fallbackEntry the config entry that should provide the default value
     */
    public OptionalEntry(ConfigEntry<T> optionalEntry, ConfigEntry<T> fallbackEntry) {
        this.optionalEntry = optionalEntry;
        this.fallbackEntry = fallbackEntry;
        this.hasValue = false;
    }

    /**
     * If the optional value is active, sets the given entry's value to that.
     *
     * @param entry the entry to copy the value to
     * @see #copyTo
     */
    public void mergeTo(ConfigEntry<T> entry) {
        if (this.hasValue)
            entry.set(this.optionalEntry.get());
    }

    @Override
    public String getName() {
        return this.optionalEntry.getName();
    }

    @Override
    public T get() {
        if (this.hasValue)
            return this.optionalEntry.get();
        else
            return this.fallbackEntry.get();
    }

    @Override
    public void set(T value) {
        this.hasValue = true;
        this.optionalEntry.set(value);
    }

    @Override
    public void reset() {
        this.hasValue = false;
    }

    @Override
    public boolean isDefault() {
        return !this.hasValue;
    }

    @Override
    public boolean shouldWrite() {
        return this.hasValue;
    }

    /**
     * If the optional value is active, sets the given entry's value to that (same as {@link #mergeTo}), otherwise, resets the given entry.
     *
     * @param entry the entry to copy the value to
     * @see #mergeTo
     * @see #reset
     */
    @Override
    public void copyTo(ConfigEntry<T> entry) {
        if (this.hasValue)
            entry.set(this.optionalEntry.get());
        else
            entry.reset();
    }

    @Override
    public Component getTranslatedValue(String modId, @NotNull T value) {
        return this.optionalEntry.getTranslatedValue(modId, value);
    }

    @Override
    public JsonElement toJson() {
        return this.optionalEntry.toJson();
    }

    @Override
    public void fromJson(JsonElement json) {
        this.optionalEntry.fromJson(json);
    }

    @Override
    public void writeToBuf(FriendlyByteBuf buf) {
        this.optionalEntry.writeToBuf(buf);
    }

    @Override
    public void readFromBuf(FriendlyByteBuf buf) {
        this.optionalEntry.readFromBuf(buf);
    }

    @Override
    public EntryTypeInfo<T> entryTypeInfo() {
        return this.optionalEntry.entryTypeInfo();
    }
}
