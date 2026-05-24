package net.xun.armory.api.item.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.xun.armory.impl.item.tools.DefaultToolCustomizer;

import java.util.function.Consumer;

/**
 * Factory interface for creating custom tool items with specialized initialization.
 * <p>
 * Implementations of this interface provide fine-grained control over tool item
 * creation, enabling custom tool classes, modified attribute application, or
 * additional properties beyond standard tool behavior. The
 * {@link DefaultToolCustomizer#INSTANCE} serves as the default implementation
 * producing standard Minecraft tool items.
 * </p>
 *
 * <h2>Usage Examples:</h2>
 *
 * <pre>{@code
 * // Example 1: Custom tool classes per type
 * ToolCustomizer CUSTOM_CLASS = (type, material, properties, attackDamage, attackSpeed, additionalAttributes) -> {
 *     switch (type) {
 *         case SWORD:
 *             return new CustomSwordItem(material, properties, attackDamage, attackSpeed, additionalAttributes);
 *         case AXE:
 *             return new CustomAxeItem(material, properties, attackDamage, attackSpeed, additionalAttributes);
 *         default:
 *             // Fall back to standard tool for other types
 *             return new ToolItem(type, material, properties, attackDamage, attackSpeed, additionalAttributes);
 *     }
 * };
 *
 * // Example 2: Adding an extra attribute modifier to all tools
 * ToolCustomizer EXTRA_KNOCKBACK = (type, material, properties, attackDamage, attackSpeed, additionalAttributes) -> {
 *     // Combine the provided additionalAttributes with extra knockback
 *     Consumer<ItemAttributeModifiers.Builder> combined = builder -> {
 *         additionalAttributes.accept(builder);
 *         builder.add(Attributes.ATTACK_KNOCKBACK,
 *             new AttributeModifier(ResourceLocation.parse("mymod:extra_knockback"), 1.0, AttributeModifier.Operation.ADD_VALUE),
 *             EquipmentSlotGroup.MAINHAND);
 *     };
 *     return new ToolItem(type, material, properties, attackDamage, attackSpeed, combined);
 * };
 * }</pre>
 *
 * @see ToolType
 * @see ToolSet.Builder#withCustomizer(ToolCustomizer)
 * @see DefaultToolCustomizer#INSTANCE
 * @since 1.0.0
 */
public interface ToolCustomizer {

    /**
     * Creates a tool item instance of the specified type.
     * <p>
     * Implementations are responsible for constructing the tool item with the
     * appropriate tool material, attack stats, and properties. The returned item
     * should be fully configured and ready for registration and in-game use.
     * </p>
     * <strong>Implementation Notes:</strong>
     * <ul>
     *   <li>The {@code properties} parameter already includes durability,
     *       enchantability, and repair items from the material.</li>
     *   <li>Attack damage and speed are already combined with the material's base
     *       attributes; the implementation should pass them directly to the
     *       {@link ToolItem} constructor.</li>
     *   <li>The {@code additionalAttributes} consumer may be used to add extra
     *       attribute modifiers (e.g., movement speed, knockback).</li>
     *   <li>Custom implementations may return subclasses of {@link ToolItem}.</li>
     * </ul>
     *
     * @param type the type of tool to create (sword, axe, etc.)
     * @param material material tier defining durability, mining level, and base damage
     * @param properties item properties with applied durability and repair settings
     * @param attackDamage the total attack damage bonus (including material bonus)
     * @param attackSpeed the attack speed modifier
     * @param additionalAttributes consumer for extra attribute modifiers (may be a no‑op)
     * @return a fully configured tool item instance, never {@code null}
     * @throws NullPointerException if any parameter is {@code null}
     * @throws IllegalArgumentException if the tool material is incompatible with the
     *         tool type or properties are invalid
     */
    ToolItem create(ToolType type, ToolMaterial material, Item.Properties properties, float attackDamage, float attackSpeed, Consumer<ItemAttributeModifiers.Builder> additionalAttributes);
}