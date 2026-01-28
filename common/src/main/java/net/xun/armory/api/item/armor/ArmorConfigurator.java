package net.xun.armory.api.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;

/**
 * Factory interface for creating custom armor items.
 * <p>
 * This interface allows customization of how armor items are created,
 * enabling the use of custom armor classes or special initialization logic.
 * The {@link #DEFAULT} implementation creates standard {@link ArmorItem} instances
 * with vanilla durability calculation.
 * </p>
 * <p>
 * Example custom configurators:
 * <pre>{@code
 * // Example 1: Custom armor with fire resistance
 * ArmorConfigurator FIRE_RESISTANT = (type, material, factor, props) -> {
 *     int durability = type.getType().getDurability(factor);
 *     return new ArmorItem(material, type.getType(),
 *         props.durability(durability).fireResistant());
 * };
 *
 * // Example 2: Custom armor class with special behavior
 * ArmorConfigurator CUSTOM_CLASS = (type, material, factor, props) -> {
 *     int durability = type.getType().getDurability(factor);
 *     return new CustomArmorItem(material, type.getType(),
 *         props.durability(durability), type.getSlot());
 * };
 *
 * // Example 3: Different durability for different pieces
 * ArmorConfigurator UNBALANCED = (type, material, factor, props) -> {
 *     int durability = type.getType().getDurability(factor);
 *     // Helmets and boots get extra durability
 *     if (type == ArmorType.HELMET || type == ArmorType.BOOTS) {
 *         durability += 100;
 *     }
 *     return new ArmorItem(material, type.getType(), props.durability(durability));
 * };
 * }</pre>
 *
 * @see ArmorSet.Builder#withConfiguration(ArmorConfigurator)
 */
public interface ArmorConfigurator {

    /**
     * Default configuration using standard armor constructors.
     * <p>
     * This implementation creates standard {@link ArmorItem} instances with
     * durability calculated as: <code>type.getType().getDurability(factor)</code>
     * </p>
     */
    ArmorConfigurator DEFAULT = (type, material, factor, props) -> {
        int durability = type.getType().getDurability(factor);
        return new ArmorItem(material, type.getType(), props.durability(durability));
    };

    /**
     * Creates an armor item instance of the specified type.
     * <p>
     * Implementations should return a fully configured armor item with the
     * given material, durability factor, and properties. The armor type
     * determines which kind of armor to create (helmet, chestplate, etc.).
     * </p>
     *
     * @param type Type of armor to create
     * @param material Armor material holder defining protection and toughness
     * @param durabilityFactor Multiplier for base material durability
     * @param props Item properties (may already include durability)
     * @return Configured armor item
     * @throws NullPointerException if type, material, or props is null
     */
    ArmorItem createArmor(ArmorType type, Holder<ArmorMaterial> material, int durabilityFactor, Item.Properties props);
}
