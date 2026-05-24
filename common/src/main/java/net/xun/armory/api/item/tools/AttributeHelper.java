package net.xun.armory.api.item.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.xun.armory.impl.item.tools.GenericAttributeHelper;

/**
 * Defines a strategy for applying attribute modifiers to tool items.
 * <p>
 * Implementations of this interface are responsible for enhancing item properties
 * with combat attributes like attack damage and attack speed. This allows for
 * customization of how attributes are applied to tools, supporting different
 * attribute systems or modding environments.
 * </p>
 *
 * @see GenericAttributeHelper
 */
public interface AttributeHelper {

    /**
     * Applies combat attributes to tool properties.
     * <p>
     * This method takes initial item properties and enhances them with the
     * specified attack damage and speed values. The implementation determines
     * how these attributes are applied (e.g., as attribute modifiers, direct
     * property settings, or custom NBT data).
     * </p>
     *
     * @param properties Initial item properties to enhance
     * @param attackDamage Total attack damage value (including tier base)
     * @param attackSpeed Attack speed value
     * @return Enhanced item properties with combat attributes applied
     * @throws NullPointerException if properties is null
     */
    Item.Properties apply(Item.Properties properties, ToolType type, ToolMaterial material, float attackDamage, float attackSpeed);
}