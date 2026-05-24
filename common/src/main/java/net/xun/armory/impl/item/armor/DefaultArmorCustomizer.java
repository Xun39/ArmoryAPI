package net.xun.armory.impl.item.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.xun.armory.api.item.armor.ArmorCustomizer;
import net.xun.armory.api.item.armor.ArmorSet;
import net.xun.armory.api.item.armor.ArmoryArmorType;

/**
 * Provides the default armor customization implementation using standard {@link ArmorItem}
 * with vanilla durability calculation.
 * <p>
 * This implementation serves as the fallback configuration for armor sets when no
 * custom {@link ArmorCustomizer} is specified. It calculates durability according to
 * Minecraft's standard formula: {@code base durability of the armor type × durabilityFactor}.
 * </p>
 * <p>
 * <strong>Note:</strong> This class cannot be instantiated. Use {@link #INSTANCE} to
 * access the default customizer implementation.
 * </p>
 *
 * @see ArmorCustomizer
 * @see ArmorSet.Builder
 * @since 2.0.0
 */
public enum DefaultArmorCustomizer implements ArmorCustomizer {
    INSTANCE;

    /**
     * Creates a standard {@link ArmorItem} with the given configuration.
     * <p>
     * The durability is calculated as:
     * <pre>{@code
     * int durability = type.getArmorType().getDurability(durabilityFactor);
     * }</pre>
     * The resulting item uses the provided {@code properties} with the durability set,
     * and the vanilla {@link ArmorItem} constructor.
     * </p>
     *
     * @param type            the armor piece type (helmet, chestplate, etc.)
     * @param material        the armor material providing defense, toughness, and repair items
     * @param properties      the base item properties (durability will be overridden)
     * @param durabilityFactor multiplier applied to the material's base durability
     * @return a new {@link ArmorItem} instance
     * @throws NullPointerException if {@code type}, {@code material}, or {@code properties} is {@code null}
     * @throws IllegalArgumentException if {@code durabilityFactor} is negative (deferred to {@code getDurability})
     */
    @Override
    public ArmorItem create(ArmoryArmorType type, ArmorMaterial material, Item.Properties properties, int durabilityFactor) {
        int durability = type.getArmorType().getDurability(durabilityFactor);
        return new ArmorItem(
                material,
                type.getArmorType(),
                properties.durability(durability)
        );
    }
}