package net.xun.armory.impl.item.tools;

import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.xun.armory.api.item.tools.ToolCustomizer;
import net.xun.armory.api.item.tools.ToolItem;
import net.xun.armory.api.item.tools.ToolType;
import net.xun.armory.api.item.tools.ToolSet;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

/**
 * Provides the default tool customization implementation using the standard
 * {@link ToolItem} class.
 * <p>
 * This implementation serves as the fallback configuration for tool sets when
 * no custom {@link ToolCustomizer} is specified. It delegates directly to the
 * {@link ToolItem} constructor, applying all provided attack stats and attributes
 * without additional modification.
 * </p>
 * <p>
 * <strong>Note:</strong> This class cannot be instantiated. Use {@link #INSTANCE}
 * to access the default customizer implementation.
 * </p>
 * <p>
 * <strong>Tool Mappings:</strong> All tool types use {@link ToolItem}, which internally
 * creates the appropriate Minecraft tool behavior based on the {@link ToolType}.
 * </p>
 *
 * @see ToolCustomizer
 * @see ToolSet.Builder
 * @since 2.0.0
 */
@ApiStatus.Internal
public enum DefaultToolCustomizer implements ToolCustomizer {
    INSTANCE;

    /**
     * Creates a {@link ToolItem} with the given configuration.
     * <p>
     * This method simply forwards all parameters to the {@link ToolItem} constructor.
     * No additional logic or modification is applied.
     * </p>
     *
     * @param type                the tool type to create
     * @param material        the material tier for the tool
     * @param properties          the item properties (durability, repair items, etc.)
     * @param attackDamage        the total attack damage bonus (including material bonus)
     * @param attackSpeed         the attack speed modifier
     * @param additionalAttributes consumer for extra attribute modifiers
     * @return a new {@link ToolItem} instance
     * @throws NullPointerException if any parameter is {@code null}
     */
    @Override
    public ToolItem create(ToolType type, ToolMaterial material, Item.Properties properties, float attackDamage, float attackSpeed, Consumer<ItemAttributeModifiers.Builder> additionalAttributes) {
        return new ToolItem(
                type,
                material,
                properties,
                attackDamage,
                attackSpeed,
                additionalAttributes
        );
    }
}