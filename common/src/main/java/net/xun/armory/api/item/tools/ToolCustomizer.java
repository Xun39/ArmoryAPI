package net.xun.armory.api.item.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.xun.armory.impl.item.tools.DefaultToolCustomizer;

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
 * @see ToolSet.Builder#withCustomizer(ToolCustomizer)
 * @see DefaultToolCustomizer#INSTANCE
 * @since 1.0.0
 */
public interface ToolCustomizer {

    TieredItem create(ToolPieceType piece, ToolContext context, Item.Properties properties);
}
