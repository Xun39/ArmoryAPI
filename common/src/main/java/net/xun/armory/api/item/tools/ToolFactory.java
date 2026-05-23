package net.xun.armory.api.item.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.xun.armory.impl.item.ItemPieceFactory;
import net.xun.armory.impl.item.PieceType;

import java.util.EnumMap;
import java.util.function.Supplier;

/**
 * Factory implementation that creates tool items for a complete tool set.
 * <p>
 * This class bridges the {@link ToolSet} configuration with the piece-wise
 * creation pattern required by {@link ItemPieceFactory}. It applies shared
 * configuration (tier, attack stats, properties) to each tool while delegating
 * the actual item creation to a {@link ToolCustomizer}.
 * </p>
 * <p>
 * <strong>Lifecycle:</strong> Instances are created by {@link ToolSet} and
 * used during registration to lazily create individual tool pieces.
 * </p>
 *
 * @see ToolSet
 * @see ToolCustomizer
 * @see AttributeHelper
 * @since 1.0.0
 */
public class ToolFactory implements ItemPieceFactory<ToolType, Item> {

    private final Tier tier;
    private final EnumMap<ToolType, Float> attackDamage;
    private final EnumMap<ToolType, Float> attackSpeed;
    private final Supplier<Item.Properties> propertiesSupplier;
    private final ToolCustomizer configurator;
    private final AttributeHelper attributeHelper;

    /**
     * Constructs a new tool factory with the specified configuration.
     *
     * @param tier material tier for all tools, never {@code null}
     * @param attackDamage map of attack damage bonuses per tool type,
     *                     never {@code null}
     * @param attackSpeed map of attack speed modifiers per tool type,
     *                    never {@code null}
     * @param propertiesSupplier supplier for item properties
     *                           (called once per tool), never {@code null}
     * @param configurator customizer for tool item creation, never {@code null}
     * @param attributeHelper helper for applying combat attributes,
     *                        never {@code null}
     * @throws NullPointerException if any required parameter is {@code null}
     * @throws IllegalArgumentException if attack maps are missing entries
     *         for any {@link ToolType}
     */
    public ToolFactory(
            Tier tier,
            EnumMap<ToolType, Float> attackDamage,
            EnumMap<ToolType, Float> attackSpeed,
            Supplier<Item.Properties> propertiesSupplier,
            ToolCustomizer configurator,
            AttributeHelper attributeHelper
    ) {
        this.tier = tier;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.propertiesSupplier = propertiesSupplier;
        this.configurator = configurator;
        this.attributeHelper = attributeHelper;
    }

    /**
     * Returns the piece type, which for tools is the {@link ToolType} itself.
     *
     * @param piece the tool piece type
     * @return the piece type unchanged
     */
    @Override
    public PieceType getPieceType(ToolType piece) {
        return piece;
    }

    /**
     * Creates a tool item for the specified tool type.
     * <p>
     * This method:
     * </p>
     * <ol>
     *   <li>Obtains fresh item properties from the supplier</li>
     *   <li>Calculates total attack damage (tier base + bonus)</li>
     *   <li>Applies combat attributes via {@link AttributeHelper}</li>
     *   <li>Delegates to the {@link ToolCustomizer} for item creation</li>
     * </ol>
     *
     * @param piece the type of tool to create
     * @return a configured tool item for the specified piece
     * @throws NullPointerException if {@code piece} is {@code null}
     * @throws IllegalStateException if attack stats are missing for the tool type
     * @throws IllegalArgumentException if the customizer fails to create the item
     */
    @Override
    public Item create(ToolType piece) {
        Item.Properties properties = propertiesSupplier.get();

        return configurator.createTool(
                piece,
                tier,
                attributeHelper.applyAttributes(
                        properties,
                        tier.getAttackDamageBonus() + attackDamage.get(piece),
                        attackSpeed.get(piece)
                )
        );
    }
}
