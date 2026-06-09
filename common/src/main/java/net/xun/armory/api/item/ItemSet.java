package net.xun.armory.api.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.xun.armory.impl.item.PieceType;
import net.xun.armory.impl.util.LazyReference;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @since 2.0.0
 */
public class ItemSet<P extends PieceType, T extends Item> {

    /** The base name of this item set, used for generating registry IDs. */
    protected final String setName;

    private final LinkedHashMap<P, LazyReference<T>> pieces = new LinkedHashMap<>();
    private final Map<String, LazyReference<T>> piecesByRegistryName = new LinkedHashMap<>();
    private final LinkedHashMap<P, Function<Item.Properties, T>> factories = new LinkedHashMap<>();

    protected ItemSet(String setName, Collection<P> pieceTypes, BiFunction<P, Item.Properties, T> factory) {
        this.setName = Objects.requireNonNull(setName, "setName");

        Objects.requireNonNull(pieceTypes, "pieceTypes");
        Objects.requireNonNull(factory, "factory");

        if (pieceTypes.isEmpty()) {
            throw new IllegalArgumentException("pieceTypes cannot be empty");
        }

        for (P piece : pieceTypes) {
            Objects.requireNonNull(piece, "piece");

            String registryName = setName + piece.getNameSuffix();
            LazyReference<T> reference = new LazyReference<>(registryName);

            pieces.put(piece, reference);
            piecesByRegistryName.put(registryName, reference);
            factories.put(piece, properties -> factory.apply(piece, properties));
        }
    }

    /**
     * Generates a registration map for all items in this set.
     * <p>
     * This method creates a map of {@link ResourceLocation} to {@link Function}
     * pairs suitable for registration with Minecraft's registry system. The
     * function accepts an {@link Item.Properties} and produces the actual item.
     * Each entry corresponds to one piece in the item set.
     * </p>
     * <p>
     * <strong>Generated Resource Locations:</strong> Follow the pattern
     * {@code modId:setName_pieceSuffix}. For example, with mod ID "mymod",
     * base name "diamond", and piece suffix "_sword", the resulting
     * ResourceLocation would be {@code mymod:diamond_sword}.
     * </p>
     *
     * @param modId the namespace (mod ID) for all generated resource locations,
     *              never {@code null}
     * @return a map of registry entries where keys are resource locations and
     *         values are factories that create the item given properties,
     *         never {@code null}, never empty
     * @throws NullPointerException     if {@code modId} is {@code null}
     * @throws IllegalArgumentException if {@code modId} is not a valid namespace
     * @see ResourceLocation#fromNamespaceAndPath(String, String)
     */
    public Map<ResourceLocation, Function<Item.Properties, T>> getPiecesForRegistration(String modId) {
        Map<ResourceLocation, Function<Item.Properties, T>> registryEntries = new LinkedHashMap<>();

        for (Map.Entry<P, Function<Item.Properties, T>> entry : factories.entrySet()) {
            P piece = entry.getKey();
            String registryName = pieces.get(piece).getName();

            registryEntries.put(
                    ResourceLocation.fromNamespaceAndPath(modId, registryName),
                    entry.getValue()
            );
        }

        return registryEntries;
    }

    /**
     * Binds a concrete supplier to a lazy reference identified by its registry name.
     * <p>
     * This method is intended to be called during the item registration phase,
     * after the actual item instance has been created. The supplier is usually a
     * memoizing supplier (e.g., {@link LazyReference} itself or a similar cache)
     * that will be invoked when {@link #get(P)} is called.
     * </p>
     * <p>
     * <strong>Important:</strong> The binding must occur before any call to
     * {@link #get(P)} for the corresponding piece, otherwise an
     * {@link IllegalStateException} will be thrown when trying to obtain the item.
     * </p>
     *
     * @param registryName the exact registry name (suffix) of the piece, as generated
     *                     during construction (e.g., "diamond_sword")
     * @param supplier     the supplier that will provide the actual item instance
     *                     (typically a caching supplier)
     * @throws IllegalArgumentException if {@code registryName} does not correspond
     *                                  to any piece in this set
     * @throws NullPointerException     if {@code supplier} is {@code null}
     * @see LazyReference#bind(Supplier)
     */
    public void bind(String registryName, Supplier<T> supplier) {
        LazyReference<T> reference = piecesByRegistryName.get(registryName);
        if (reference == null) {
            throw new IllegalArgumentException("Unknown registry name '" + registryName + "' for set '" + setName + "'");
        }
        reference.bind(supplier);
    }

    /**
     * Retrieves a supplier for a specific piece in this item set.
     * <p>
     * The returned supplier will lazily create the item upon first invocation
     * and cache the result for subsequent calls.
     * </p>
     *
     * @param piece the piece to retrieve, must be a valid enum constant for
     *              this item set
     * @return a supplier providing the requested item piece, never {@code null}
     * @throws NullPointerException     if {@code piece} is {@code null}
     * @throws IllegalArgumentException if {@code piece} is not part of this set
     */
    public Supplier<T> get(P piece) {
        LazyReference<T> reference = pieces.get(piece);
        if (reference == null) {
            throw new IllegalArgumentException("Unknown piece '" + piece + "' for set '" + setName + "'");
        }
        return pieces.get(piece);
    }

    /**
     * Retrieves all items in this set, forcing initialization if not already created.
     * <p>
     * This method triggers the creation of all items in the set (if not already
     * initialized) and returns them as a collection. The collection iteration
     * order follows the natural order of the piece enum.
     * </p>
     * <p>
     * <strong>Performance Note:</strong> This method may cause initialization
     * of all items in the set, which could be expensive if items are
     * resource-intensive to create.
     * </p>
     *
     * @return an unmodifiable collection containing all items in this set,
     *         in enum constant order, never {@code null}, never empty
     */
    public Collection<T> getAll() {
        return pieces.values().stream().map(Supplier::get).toList();
    }

    public Set<P> getPieces() {
        return Collections.unmodifiableSet(pieces.keySet());
    }

    /**
     * Gets the base name of this item set.
     * <p>
     * This is the name used during construction and forms the basis for all
     * generated registry IDs within the set.
     * </p>
     *
     * @return the base name of this item set, never {@code null}
     */
    public String getSetName() {
        return setName;
    }
}