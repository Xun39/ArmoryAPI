package net.xun.armory.impl.util;

import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Internal utility class for lazy initialization of objects.
 * <p>
 * <strong>Note:</strong> This class is part of the internal API and is not intended for use by external mods.
 * It may change or be removed without notice in future versions.
 * </p>
 * <p>
 * This class wraps a {@link Supplier} and ensures that the object is created only once,
 * on the first call to {@link #get()}. Subsequent calls return the same instance.
 * This pattern is useful for deferring expensive object creation until it's actually needed,
 * and for avoiding circular dependencies during initialization.
 * </p>
 *
 * <p>
 * This is an internal implementation detail of the Armory API. Use the public API classes
 * ({@link net.xun.armory.api.item.tools.ToolSet}, {@link net.xun.armory.api.item.armor.ArmorSet})
 * instead of interacting with this class directly.
 * </p>
 *
 * @param <T> the type of object to be lazily initialized
 *
 *
 */
@ApiStatus.Internal
public class LazyReference<T> implements Supplier<T> {

    private final String name;
    private volatile Supplier<T> delegate;

    /**
     * Constructs a new LazyReference with a name and a supplier.
     *
     * @param name     a descriptive name for the object (used for debugging or logging)
     * @throws NullPointerException if name or supplier is null
     */
    public LazyReference(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    /**
     * Gets the name associated with this lazy reference.
     * <p>
     * This name is typically used for registration or identification purposes,
     * such as constructing registry IDs for items.
     * </p>
     *
     * @return the name of this lazy reference
     */
    public String getName() {
        return name;
    }

    public synchronized void bind(Supplier<T> delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    public boolean isBound() {
        return delegate != null;
    }

    /**
     * Returns the lazily initialized object.
     * <p>
     * If this is the first call, the object is created by calling the supplier.
     * Subsequent calls return the same instance (no additional initialization occurs).
     * This method is thread-safe for the initial creation, but subsequent reads
     * may not be synchronized.
     * </p>
     *
     * @return the lazily initialized object
     */
    @Override
    public T get() {
        Supplier<T> current = delegate;
        if (current == null) {
            throw new IllegalStateException("Item reference '" + name + "' has not been registered yet");
        }
        return current.get();
    }
}
