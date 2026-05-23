package net.xun.armory.api.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.xun.armory.impl.item.PieceType;
import net.xun.armory.impl.item.ItemPieceFactory;

import java.util.function.Supplier;

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
public class ArmorFactory implements ItemPieceFactory<ArmorType, ArmorItem> {

    private final Holder<ArmorMaterial> material;
    private final int durabilityFactor;
    private final Supplier<Item.Properties> propertiesSupplier;
    private final ArmorCustomizer configurator;

    /**
     * Constructs a new armor factory with the specified configuration.
     *
     * @param material holder for the armor material, never {@code null}
     * @param durabilityFactor multiplier for base material durability
     * @param propertiesSupplier supplier for item properties (called once per piece)
     * @param configurator customizer for armor item creation, never {@code null}
     * @throws NullPointerException if any required parameter is {@code null}
     */
    public ArmorFactory(
            Holder<ArmorMaterial> material,
            int durabilityFactor,
            Supplier<Item.Properties> propertiesSupplier,
            ArmorCustomizer configurator
    ) {
        this.material = material;
        this.durabilityFactor = durabilityFactor;
        this.propertiesSupplier = propertiesSupplier;
        this.configurator = configurator;
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
     * Creates an armor item for the specified piece type.
     * <p>
     * This method:
     * </p>
     * <ol>
     *   <li>Obtains fresh item properties from the supplier</li>
     *   <li>Delegates to the configured {@link ArmorCustomizer} for item creation</li>
     *   <li>Returns a fully configured armor item</li>
     * </ol>
     *
     * @param piece the type of armor piece to create
     * @return a configured armor item for the specified piece
     * @throws NullPointerException if {@code piece} is {@code null}
     * @throws IllegalStateException if the customizer fails to create the item
     */
    @Override
    public ArmorItem create(ArmorType piece) {
        Item.Properties properties = propertiesSupplier.get();

        return configurator.createArmorItem(
                piece,
                material,
                durabilityFactor,
                properties
        );
    }
}
