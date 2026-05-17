package net.xun.armory.api.item.tools;

import net.minecraft.world.item.Item;
import net.xun.armory.impl.item.tools.GenericAttributeHelper;

/**
 * Defines a strategy for applying combat attribute modifiers to tool items.
 * <p>
 * Implementations of this interface enhance item properties with combat attributes
 * such as attack damage and attack speed. This abstraction allows for different
 * attribute application strategies across various modding environments or
 * attribute systems, while maintaining compatibility with Minecraft's
 * attribute system.
 * </p>
 *
 * <h2>Common Implementations:</h2>
 * <ul>
 *   <li>{@link GenericAttributeHelper}: Standard implementation using
 *       Minecraft's attribute modifier system</li>
 *   <li>Custom implementations for specialized mod environments or
 *       alternative attribute systems</li>
 * </ul>
 *
 * @see GenericAttributeHelper
 * @see ToolSet.Builder
 * @since 1.0.0
 */
public interface AttributeHelper {

    /**
     * Applies combat attributes to tool item properties.
     * <p>
     * This method enhances the provided item properties with the specified
     * attack damage and speed values. The exact implementation may vary:
     * some may use Minecraft's attribute modifier system, while others might
     * apply custom NBT data or alternative attribute systems.
     * </p>
     * <p></p>
     * <strong>Implementation Notes:</strong>
     * <ul>
     *   <li>The returned {@link Item.Properties} should be a modified copy
     *       or enhanced version of the input</li>
     *   <li>Implementations must not mutate the input properties directly</li>
     *   <li>The method should handle any necessary attribute registration
     *       or validation</li>
     * </ul>
     *
     * @param properties initial item properties to enhance, never {@code null}
     * @param damage total attack damage value (including tier base damage)
     * @param speed attack speed value
     * @return enhanced item properties with combat attributes applied,
     *         never {@code null}
     * @throws NullPointerException if {@code properties} is {@code null}
     * @throws IllegalArgumentException if attribute values are outside
     *         acceptable ranges for the implementation
     */
    Item.Properties applyAttributes(Item.Properties properties, float damage, float speed);
}