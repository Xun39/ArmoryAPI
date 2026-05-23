package net.xun.armory.impl.item;

import net.xun.armory.api.item.ItemSet;
import org.jetbrains.annotations.ApiStatus;

/**
 * Internal interface for item pieces that have a naming suffix for registration.
 * <p>
 * This interface provides a standardized way to generate registry names for
 * items within a set by combining a base name with a type-specific suffix.
 * </p>
 * <p>
 * <strong>Internal Use Only:</strong> This interface is part of the internal API
 * and is not intended for direct implementation by mod developers. Breaking
 * changes may occur without notice.
 * </p>
 *
 * @see ItemSet
 * @see ItemPieceFactory
 * @since 2.0.0
 */
@ApiStatus.Internal
public interface PieceType {

    /**
     * Gets the naming suffix for this piece type.
     * <p>
     * The suffix is appended to a base name to create the full registry name
     * for an item. It should typically start with an underscore ("_") to
     * separate it from the base name, following Minecraft's naming conventions.
     * </p>
     * <p>
     * <strong>Examples:</strong>
     * </p>
     * <ul>
     *   <li>"_sword" for swords</li>
     *   <li>"_helmet" for helmets</li>
     *   <li>"_chestplate" for chestplates</li>
     * </ul>
     *
     * @return the naming suffix for this piece type, never {@code null}
     */
    String getNameSuffix();
}