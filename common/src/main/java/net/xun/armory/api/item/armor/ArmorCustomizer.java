package net.xun.armory.api.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.xun.armory.impl.item.armor.DefaultArmorCustomizer;

/**
 * Factory interface for creating custom armor items with specialized initialization logic.
 * <p>
 * Implementations of this interface provide fine-grained control over armor item creation,
 * allowing for custom armor classes, modified durability calculations, or additional
 * properties beyond the standard {@link ArmorItem} behavior. The {@link DefaultArmorCustomizer#INSTANCE}
 * serves as the default implementation producing standard armor items with vanilla durability calculation.
 * </p>
 *
 * <h2>Usage Examples:</h2>
 *
 * <pre>{@code
 * // Example 1: Fire-resistant armor with standard durability
 * ArmorCustomizer FIRE_RESISTANT = (type, material, factor, props) -> {
 *     int durability = type.getArmorType().getDurability(factor);
 *     return new ArmorItem(material, type.getArmorType(),
 *         props.durability(durability).fireResistant());
 * };
 *
 * // Example 2: Custom armor class with special behavior
 * ArmorCustomizer CUSTOM_CLASS = (type, material, factor, props) -> {
 *     int durability = type.getArmorType().getDurability(factor);
 *     return new CustomArmorItem(material, type.getArmorType(),
 *         props.durability(durability), type.getEquipmentSlot());
 * };
 *
 * // Example 3: Variable durability based on armor piece
 * ArmorCustomizer UNBALANCED = (type, material, factor, props) -> {
 *     int durability = type.getArmorType().getDurability(factor);
 *     // Helmets and boots receive additional durability
 *     if (type == ArmorType.HELMET || type == ArmorType.BOOTS) {
 *         durability += 100;
 *     }
 *     return new ArmorItem(material, type.getArmorType(),
 *         props.durability(durability));
 * };
 * }</pre>
 *
 * @see ArmorSet.Builder#withCustomizer(ArmorCustomizer)
 * @see DefaultArmorCustomizer#INSTANCE
 * @since 1.0.0
 */
public interface ArmorCustomizer {

    /**
     * Creates a fully configured armor item instance of the specified type.
     * <p>
     * Implementations are responsible for constructing the armor item with appropriate
     * durability calculation and property configuration. The returned item should be
     * ready for registration and use in-game.
     * </p>
     * <strong>Implementation Notes:</strong>
     * <ul>
     *   <li>Durability calculation should typically use {@code type.getArmorType().getDurability(factor)}</li>
     *   <li>Properties may be modified but should not be shared between item instances</li>
     *   <li>The material holder provides armor statistics and enchantability</li>
     * </ul>
     *
     * @param type the type of armor piece to create (helmet, chestplate, leggings, or boots)
     * @param material holder for the armor material defining protection values and toughness
     * @param durabilityFactor multiplier applied to the base material durability
     * @param props base item properties (durability should be set by the implementation)
     * @return a fully configured armor item instance, never {@code null}
     * @throws NullPointerException if {@code type}, {@code material}, or {@code props} is {@code null}
     * @throws IllegalArgumentException if the durability factor is negative or invalid
     * @see ArmorItem.Type#getDurability(int)
     */
    ArmorItem createArmorItem(ArmorType type, Holder<ArmorMaterial> material, int durabilityFactor, Item.Properties props);
}
