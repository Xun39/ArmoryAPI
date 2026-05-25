package net.xun.armory.api.item.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
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
 * ToolCustomizer CUSTOM_CLASS = (type, tier, props) -> {
 *     switch (type) {
 *         case SWORD:
 *             return new CustomSwordItem(tier, props);
 *         case AXE:
 *             return new CustomAxeItem(tier, props);
 *         default:
 *             // (throw an exception)
 *     }
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
     * appropriate tier and properties. The returned item should be fully
     * configured and ready for registration and in-game use.
     * </p>
     * <p></p>
     * <strong>Implementation Notes:</strong>
     * <ul>
     *   <li>Properties already include any attribute modifications from
     *       {@link AttributeHelper}</li>
     *   <li>The tier provides durability, mining level, and base damage</li>
     *   <li>Custom implementations may return subclasses of standard tools</li>
     * </ul>
     *
     * @param type the type of tool to create (sword, axe, etc.)
     * @param tier material tier defining durability, mining level, and base damage
     * @param properties item properties with applied attributes
     * @return a fully configured tool item instance, never {@code null}
     * @throws NullPointerException if {@code type}, {@code tier}, or
     *         {@code properties} is {@code null}
     * @throws IllegalArgumentException if the tier is incompatible with the
     *         tool type or properties are invalid
     */
    ToolItem create(ToolType type, Tier tier, Item.Properties properties, float attackDamage, float attackSpeed, Consumer<ItemAttributeModifiers.Builder> additionalAttributes);
}
