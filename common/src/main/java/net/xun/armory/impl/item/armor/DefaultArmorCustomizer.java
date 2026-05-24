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
 * Minecraft's standard formula: {@code base material durability × durability factor}.
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