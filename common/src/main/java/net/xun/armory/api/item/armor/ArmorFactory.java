package net.xun.armory.api.item.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.xun.armory.impl.item.PieceType;
import net.xun.armory.impl.item.ItemPieceFactory;

import java.util.function.UnaryOperator;

/**
 * Factory implementation that creates armor items for a complete armor set.
 * <p>
 * This class bridges the {@link ArmorSet} configuration with the piece-wise creation
 * pattern required by {@link ItemPieceFactory}. It applies shared configuration
 * (material, durability, properties) to each armor piece while delegating the actual
 * item creation to an {@link ArmorCustomizer}.
 * </p>
 * <p>
 * <strong>Lifecycle:</strong> Instances are created by {@link ArmorSet} and used
 * during registration to lazily create individual armor pieces.
 * </p>
 *
 * @see ArmorSet
 * @see ArmorCustomizer
 * @since 2.0.0
 */
public final class ArmorFactory implements ItemPieceFactory<ArmoryArmorType, ArmorItem> {

    private final ArmorMaterial material;
    private final int durabilityFactor;
    private final UnaryOperator<Item.Properties> propertiesModifier;
    private final ArmorCustomizer customizer;

    public ArmorFactory(
            ArmorMaterial material,
            int durabilityFactor,
            UnaryOperator<Item.Properties> propertiesModifier,
            ArmorCustomizer configurator
    ) {
        this.material = material;
        this.durabilityFactor = durabilityFactor;
        this.propertiesModifier = propertiesModifier;
        this.customizer = configurator;
    }

    /**
     * Returns the piece type, which for armor is the {@link ArmoryArmorType} itself.
     *
     * @param piece the armor piece type
     * @return the piece type unchanged
     */
    @Override
    public PieceType getPieceType(ArmoryArmorType piece) {
        return piece;
    }

    @Override
    public ArmorItem create(ArmoryArmorType piece, Item.Properties properties) {
        Item.Properties effective = propertiesModifier.apply(properties);

        return customizer.create(
                piece,
                material,
                effective,
                durabilityFactor
        );
    }
}