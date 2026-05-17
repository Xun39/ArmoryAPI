package net.xun.armory.internal.item;

import net.minecraft.world.item.Item;
import net.xun.armory.api.item.ItemSet;
import org.jetbrains.annotations.ApiStatus;

/**
 * Internal factory interface for creating individual item pieces within a set.
 * <p>
 * This interface provides the mechanism for creating specific item instances
 * based on piece identifiers. Implementations are responsible for:
 * </p>
 * <ul>
 *   <li>Mapping piece identifiers to their corresponding {@link PieceType}</li>
 *   <li>Creating the actual item instances with appropriate configuration</li>
 * </ul>
 * <p>
 * <strong>Internal Use Only:</strong> This interface is part of the internal API
 * and is not intended for direct implementation by mod developers. Breaking
 * changes may occur without notice.
 * </p>
 * <p>
 * Implementations are typically used by {@link ItemSet} to lazily create
 * individual pieces of armor sets, tool sets, or other related item collections.
 * </p>
 *
 * @param <P> the type representing individual pieces (typically an enum)
 * @param <T> the type of item created by this factory, must extend {@link Item}
 *
 * @see ItemSet
 * @see PieceType
 * @since 2.0.0
 */
@ApiStatus.Internal
public interface ItemPieceFactory<P, T extends Item> {

    /**
     * Retrieves the piece type metadata for a given piece identifier.
     * <p>
     * This method provides the naming and classification information needed
     * for proper item registration and identification within a set.
     * </p>
     *
     * @param piece the piece identifier, never {@code null}
     * @return the piece type metadata for the specified piece, never {@code null}
     * @throws NullPointerException if {@code piece} is {@code null}
     * @throws IllegalArgumentException if {@code piece} is not recognized
     */
    PieceType getPieceType(P piece);

    /**
     * Creates an item instance for the specified piece.
     * <p>
     * This method is responsible for constructing the actual item with all
     * necessary properties, attributes, and configuration. The returned item
     * should be fully configured and ready for registration and use.
     * </p>
     *
     * @param piece the piece identifier for which to create an item,
     *              never {@code null}
     * @return a newly created item instance for the specified piece,
     *         never {@code null}
     * @throws NullPointerException if {@code piece} is {@code null}
     * @throws IllegalStateException if the item cannot be created due to
     *         invalid configuration or missing dependencies
     */
    T create(P piece);
}