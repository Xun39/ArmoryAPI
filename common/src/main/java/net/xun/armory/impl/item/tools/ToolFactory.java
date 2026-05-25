package net.xun.armory.impl.item.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.xun.armory.api.item.tools.*;
import net.xun.armory.impl.item.ItemPieceFactory;
import net.xun.armory.impl.item.PieceType;
import org.jetbrains.annotations.ApiStatus;

import java.util.EnumMap;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

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
 * <p>
 * <strong>Note:</strong> This class is part of the internal API and is not intended
 * for use by external mods. It may change or be removed without notice.
 * </p>
 *
 * @see ToolSet
 * @see ToolCustomizer
 * @since 2.0.0
 */
@ApiStatus.Internal
public final class ToolFactory implements ItemPieceFactory<ToolType, ToolItem> {

    private final Tier tier;
    private final EnumMap<ToolType, Float> attackDamage;
    private final EnumMap<ToolType, Float> attackSpeed;
    private final UnaryOperator<Item.Properties> propertiesModifier;
    private final ToolCustomizer customizer;
    private final Consumer<ItemAttributeModifiers.Builder> additionalAttributes;

    /**
     * Constructs a new {@code ToolFactory} with all required parameters.
     *
     * @param tier                 the tool material (durability, speed, etc.); cannot be null
     * @param attackDamage         map of tool type to base attack damage (added to material's bonus); cannot be null
     * @param attackSpeed          map of tool type to attack speed modifier; cannot be null
     * @param propertiesModifier   function to modify item properties before creation; cannot be null
     * @param customizer           customizer that creates the actual {@link ToolItem} instances; cannot be null
     * @param additionalAttributes optional consumer to add extra attribute modifiers (may be null, treated as no-op)
     * @throws NullPointerException if any of the required parameters (toolMaterial, attackDamage, attackSpeed,
     *                              propertiesModifier, customizer) are null
     */
    public ToolFactory(
            Tier tier,
            EnumMap<ToolType, Float> attackDamage,
            EnumMap<ToolType, Float> attackSpeed,
            UnaryOperator<Item.Properties> propertiesModifier,
            ToolCustomizer customizer,
            Consumer<ItemAttributeModifiers.Builder> additionalAttributes
    ) {
        this.tier = Objects.requireNonNull(tier, "tier");
        this.attackDamage = Objects.requireNonNull(attackDamage, "attackDamage");
        this.attackSpeed = Objects.requireNonNull(attackSpeed, "attackSpeed");
        this.propertiesModifier = Objects.requireNonNull(propertiesModifier, "propertiesModifier");
        this.customizer = Objects.requireNonNull(customizer, "customizer");
        this.additionalAttributes = Objects.requireNonNullElse(additionalAttributes, builder -> {
        });
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
     * Creates a {@link ToolItem} for the given tool type using the factory's configuration.
     *
     * <p>The method applies the {@link #propertiesModifier} to the provided base properties,
     * retrieves the appropriate attack damage and speed from the maps (defaulting to 0.0F if missing),
     * and delegates the actual creation to the {@link #customizer}.
     *
     * @param piece      the tool type to create (e.g., {@code ToolType.PICKAXE})
     * @param properties the base item properties (will be modified by {@code propertiesModifier})
     * @return a new {@link ToolItem} instance configured for the given type
     */
    @Override
    public ToolItem create(ToolType piece, Item.Properties properties) {
        Item.Properties effective = propertiesModifier.apply(properties);

        float damage = attackDamage.getOrDefault(piece, 0.0F);
        float speed = attackSpeed.getOrDefault(piece, 0.0F);

        return customizer.create(
                piece,
                tier,
                effective,
                damage,
                speed,
                additionalAttributes
        );
    }
}
