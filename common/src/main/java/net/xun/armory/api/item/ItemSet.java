package net.xun.armory.api.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.xun.armory.impl.item.ItemPieceFactory;
import net.xun.armory.impl.item.PieceType;
import net.xun.armory.impl.util.LazyReference;

import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Base class for managing sets of related items with lazy initialization.
 * <p>
 * This class provides a foundation for creating and managing item sets (such as
 * armor sets or tool sets) where each piece is represented by an enum constant
 * and lazily initialized upon first access. It handles the creation, caching,
 * and registration of individual item pieces within a cohesive set.
 * </p>
 *
 * @param <P> the enum type representing individual pieces in the set
 * @param <T> the type of item managed by this set, must extend {@link Item}
 *
 * @see ItemPieceFactory
 * @see PieceType
 * @see LazyReference
 * @since 2.0.0
 */
public class ItemSet<P extends Enum<P>, T extends Item> {

    /** The base name of this item set, used for generating registry IDs. */
    protected final String setName;

    /** Map storing lazy references to each item piece in the set. */
    protected final Map<P, LazyReference<T>> pieces;

    /**
     * Constructs a new item set with the specified configuration.
     * <p>
     * This constructor initializes the item set by:
     * </p>
     * <ol>
     *   <li>Creating an {@link EnumMap} for the piece enum class</li>
     *   <li>Iterating through all enum constants of the piece type</li>
     *   <li>Creating a {@link LazyReference} for each piece using the factory</li>
     *   <li>Generating appropriate names for each piece</li>
     * </ol>
     *
     * @param setName the base name for all items in this set (e.g., "diamond"),
     *                used to generate registry IDs, never {@code null}
     * @param pieceEnumClass the class object for the piece enum type,
     *                       used to discover all possible pieces,
     *                       never {@code null}
     * @param factory the factory responsible for creating individual item pieces,
     *                never {@code null}
     * @throws NullPointerException if any parameter is {@code null}
     * @throws IllegalArgumentException if the piece enum class has no constants
     */
    protected ItemSet(String setName, Class<P> pieceEnumClass, ItemPieceFactory<P, T> factory) {
        this.setName = setName;
        this.pieces = new EnumMap<>(pieceEnumClass);

        for (P piece : pieceEnumClass.getEnumConstants()) {
            PieceType pieceType = factory.getPieceType(piece);

            pieces.put(piece, new LazyReference<>(
                    setName + pieceType.getNameSuffix(),
                    () -> factory.create(piece)
            ));
        }
    }

    /**
     * Generates a registration map for all items in this set.
     * <p>
     * This method creates a map of {@link ResourceLocation} to {@link Supplier}
     * pairs suitable for registration with Minecraft's registry system. Each
     * entry corresponds to one piece in the item set.
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
     *         values are suppliers providing the registered items,
     *         never {@code null}, never empty
     * @throws NullPointerException if {@code modId} is {@code null}
     * @throws IllegalArgumentException if {@code modId} is not a valid namespace
     *
     * @see ResourceLocation#fromNamespaceAndPath(String, String)
     */
    public Map<ResourceLocation, Supplier<T>> getPiecesForRegistration(String modId) {
        Map<ResourceLocation, Supplier<T>> registryEntries = new LinkedHashMap<>();

        for (LazyReference<T> ref : pieces.values()) {
            registryEntries.put(
                    ResourceLocation.fromNamespaceAndPath(modId, ref.getName()),
                    ref
            );
        }

        return registryEntries;
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
     * @throws NullPointerException if {@code piece} is {@code null}
     * @throws IllegalArgumentException if {@code piece} is not part of this set
     */
    public Supplier<T> get(P piece) {
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