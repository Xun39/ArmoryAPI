package net.xun.armory.impl.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.xun.armory.api.item.armor.ArmorCustomizer;
import net.xun.armory.api.item.armor.ArmorSet;
import net.xun.armory.api.item.armor.ArmorType;
import net.xun.armory.impl.item.PieceType;
import net.xun.armory.impl.item.ItemPieceFactory;
import org.jetbrains.annotations.ApiStatus;

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
 * <p>
 * <strong>Note:</strong> This class is part of the internal API and is not intended
 * for use by external mods. It may change or be removed without notice.
 * </p>
 *
 * @see ArmorSet
 * @see ArmorCustomizer
 * @since 2.0.0
 */
@ApiStatus.Internal
public final class ArmorFactory implements ItemPieceFactory<ArmorType, ArmorItem> {

    private final Holder<ArmorMaterial> material;
    private final int durabilityFactor;
    private final UnaryOperator<Item.Properties> propertiesModifier;
    private final ArmorCustomizer customizer;

    /**
     * Constructs a new {@code ArmorFactory} with the given configuration.
     *
     * @param material           the armor material (cannot be null)
     * @param durabilityFactor   multiplier for the material's base durability
     * @param propertiesModifier function to modify item properties before creation (cannot be null)
     * @param customizer         customizer that creates the actual {@link ArmorItem} instances (cannot be null)
     * @throws NullPointerException if {@code material}, {@code propertiesModifier}, or {@code customizer} is null
     */
    public ArmorFactory(
            Holder<ArmorMaterial> material,
            int durabilityFactor,
            UnaryOperator<Item.Properties> propertiesModifier,
            ArmorCustomizer customizer
    ) {
        this.material = material;
        this.durabilityFactor = durabilityFactor;
        this.propertiesModifier = propertiesModifier;
        this.customizer = customizer;
    }

    /**
     * Returns the piece type, which for armor is the {@link ArmorType} itself.
     *
     * @param piece the armor piece type
     * @return the piece type unchanged
     */
    @Override
    public PieceType getPieceType(ArmorType piece) {
        return piece;
    }

    /**
     * Creates an {@link ArmorItem} for the given armor type using the factory's configuration.
     *
     * <p>The method applies the {@link #propertiesModifier} to the provided base properties,
     * then delegates the actual creation to the {@link #customizer}.
     *
     * @param piece      the armor type to create (e.g., {@code ArmoryArmorType.HELMET})
     * @param properties the base item properties (will be modified by {@code propertiesModifier})
     * @return a new {@link ArmorItem} instance configured for the given type
     */
    @Override
    public ArmorItem create(ArmorType piece, Item.Properties properties) {
        Item.Properties effective = propertiesModifier.apply(properties);

        return customizer.create(
                piece,
                material,
                effective,
                durabilityFactor
        );
    }
}
