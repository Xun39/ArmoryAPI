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
 * Provides the default tool customization implementation using standard
 * Minecraft tool item classes.
 * <p>
 * This implementation serves as the fallback configuration for tool sets when
 * no custom {@link ToolCustomizer} is specified. It creates the appropriate
 * Minecraft tool item for each {@link ToolType}.
 * </p>
 * <p>
 * <strong>Note:</strong> This class cannot be instantiated. Use
 * {@link #INSTANCE} to access the default customizer implementation.
 * </p>
 * <p>
 * </p>
 * <strong>Tool Mappings:</strong>
 * <table border="1">
 *   <caption>Default Tool Creation</caption>
 *   <tr><th>ToolType</th><th>Minecraft Class</th></tr>
 *   <tr><td>SWORD</td><td>{@link SwordItem}</td></tr>
 *   <tr><td>AXE</td><td>{@link AxeItem}</td></tr>
 *   <tr><td>PICKAXE</td><td>{@link PickaxeItem}</td></tr>
 *   <tr><td>HOE</td><td>{@link HoeItem}</td></tr>
 *   <tr><td>SHOVEL</td><td>{@link ShovelItem}</td></tr>
 * </table>
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
     * @param type                 the tool type to create
     * @param tier                 the material tier for the tool
     * @param properties           the item properties (durability, repair items, etc.)
     * @param attackDamage         the total attack damage bonus (including material bonus)
     * @param attackSpeed          the attack speed modifier
     * @param additionalAttributes consumer for extra attribute modifiers
     * @return a new {@link ToolItem} instance
     * @throws NullPointerException if any parameter is {@code null}
     */
    @Override
    public ToolItem create(ToolType type, Tier tier, Item.Properties properties, float attackDamage, float attackSpeed, Consumer<ItemAttributeModifiers.Builder> additionalAttributes) {
        return new ToolItem(
                type,
                tier,
                properties,
                attackDamage,
                attackSpeed,
                additionalAttributes
        );
    }
}
