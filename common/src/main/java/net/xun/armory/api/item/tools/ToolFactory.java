package net.xun.armory.api.item.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.xun.armory.impl.item.ItemPieceFactory;
import net.xun.armory.impl.item.PieceType;

import java.util.EnumMap;
import java.util.Objects;
import java.util.function.Consumer;
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
 *
 * @see ToolSet
 * @see ToolCustomizer
 * @since 1.0.0
 */
public final class ToolFactory implements ItemPieceFactory<ToolType, ToolItem> {

    private final ToolMaterial toolMaterial;
    private final EnumMap<ToolType, Float> attackDamage;
    private final EnumMap<ToolType, Float> attackSpeed;
    private final UnaryOperator<Item.Properties> propertiesModifier;
    private final ToolCustomizer customizer;
    private final Consumer<ItemAttributeModifiers.Builder> additionalAttributes;

    public ToolFactory(
            ToolMaterial toolMaterial,
            EnumMap<ToolType, Float> attackDamage,
            EnumMap<ToolType, Float> attackSpeed,
            UnaryOperator<Item.Properties> propertiesModifier,
            ToolCustomizer customizer,
            Consumer<ItemAttributeModifiers.Builder> additionalAttributes
    ) {
        this.toolMaterial = Objects.requireNonNull(toolMaterial, "toolMaterial");
        this.attackDamage = Objects.requireNonNull(attackDamage, "attackDamage");
        this.attackSpeed = Objects.requireNonNull(attackSpeed, "attackSpeed");
        this.propertiesModifier = Objects.requireNonNull(propertiesModifier, "propertiesModifier");
        this.customizer = Objects.requireNonNull(customizer, "customizer");
        this.additionalAttributes = Objects.requireNonNullElse(additionalAttributes, builder -> {});
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

    @Override
    public ToolItem create(ToolType piece, Item.Properties properties) {
        Item.Properties effective = propertiesModifier.apply(properties);

        float damage = attackDamage.getOrDefault(piece, 0.0F);
        float speed = attackSpeed.getOrDefault(piece, 0.0F);

        return customizer.create(
                piece,
                toolMaterial,
                effective,
                damage,
                speed,
                additionalAttributes
        );
    }
}
