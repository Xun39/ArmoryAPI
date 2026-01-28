package net.xun.armory.api.item.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

/**
 * Factory interface for creating custom tool items.
 * <p>
 * This interface allows customization of how tool items are created,
 * enabling the use of custom tool classes or special initialization logic.
 * The {@link #DEFAULT} implementation uses the standard factory methods
 * defined in {@link ToolType}.
 * </p>
 * <p>
 * Example custom configurators:
 * <pre>{@code
 * // Example 1: Custom tool with fire aspect
 * ToolConfigurator FIERY_TOOLS = (type, tier, props) -> {
 *     Item tool = type.create(tier, props);
 *     // Apply custom NBT or components for fire aspect
 *     return tool;
 * };
 *
 * // Example 2: Custom tool class with special behavior
 * ToolConfigurator CUSTOM_CLASS = (type, tier, props) -> {
 *     switch (type) {
 *         case SWORD:
 *             return new CustomSwordItem(tier, props);
 *         case AXE:
 *             return new CustomAxeItem(tier, props);
 *         default:
 *             return type.create(tier, props);
 *     }
 * };
 *
 * // Example 3: Different properties for different tools
 * ToolConfigurator VARIED_RARITY = (type, tier, props) -> {
 *     // Swords and axes are uncommon, others are common
 *     Item.Properties modifiedProps = props;
 *     if (type == ToolType.SWORD || type == ToolType.AXE) {
 *         modifiedProps = new Item.Properties()
 *             .rarity(Rarity.UNCOMMON);
 *     }
 *     return type.create(tier, modifiedProps);
 * };
 * }</pre>
 *
 * @see ToolType
 * @see ToolSet.Builder#withConfiguration(ToolConfigurator)
 */
public interface ToolConfigurator {

    /**
     * Default configuration using standard tool constructors.
     * <p>
     * This implementation delegates to {@link ToolType#create(Tier, Item.Properties)},
     * creating standard Minecraft tool items (SwordItem, AxeItem, etc.).
     * </p>
     */
    ToolConfigurator DEFAULT = ToolType::create;

    /**
     * Creates a tool item instance of the specified type.
     * <p>
     * Implementations should return a fully configured tool item with the
     * given tier and properties. The tool type determines which kind of
     * tool to create (sword, axe, etc.).
     * </p>
     *
     * @param type Type of tool to create
     * @param tier Material tier for tool durability and mining level
     * @param properties Item properties with any applied attributes
     * @return Configured tool item
     * @throws NullPointerException if type, tier, or properties is null
     */
    Item createTool(ToolType type, Tier tier, Item.Properties properties);
}
