package ir.mehradn.mehradconfig.entry;

import org.jetbrains.annotations.NotNull;

/**
 * DefaultValueEntry is an implementation of {@link ConfigEntry}, Its default value is based on the value passed in the constructor.
 *
 * @param <T> the type of the config value
 * @see ConfigEntry
 */
public abstract class DefaultValueEntry <T> implements ConfigEntry<T> {
    private final String name;
    private final T defaultValue;
    private T value;

    /**
     * @param name         the name of the entry
     * @param defaultValue the default value of the entry, it is expected to be pre-trimmed
     */
    protected DefaultValueEntry(String name, @NotNull T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = this.defaultValue;
    }

    /**
     * Creates an optional entry from this config entry.
     *
     * @param fallbackEntry the config entry that should provide the default value, avoid using a config entry from the same config
     * @return an optional entry
     * @see OptionalEntry
     */
    public OptionalEntry<T> makeOptional(ConfigEntry<T> fallbackEntry) {
        return new OptionalEntry<>(this, fallbackEntry);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public void set(T value) {
        this.value = trim(value);
    }

    @Override
    public void reset() {
        this.value = this.defaultValue;
    }

    @Override
    public boolean isDefault() {
        return (this.value == this.defaultValue);
    }

    @Override
    public boolean shouldWrite() {
        return true;
    }

    @Override
    public void copyTo(ConfigEntry<T> entry) {
        entry.set(this.value);
    }

    /**
     * Gets a value and returns a trimmed value. By default, returns the exact same value. Override it to limit the values that can be used in entry
     * even more.
     *
     * @param value the value to be trimmed
     * @return the trimmed value
     */
    protected T trim(T value) {
        return value;
    }
}
