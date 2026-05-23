package net.xun.armory.impl.item.armor;

import net.minecraft.world.item.ArmorItem;
import net.xun.armory.api.item.armor.ArmorCustomizer;
import net.xun.armory.api.item.armor.ArmorSet;

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
public final class DefaultArmorCustomizer {

    /**
     * Private constructor to prevent instantiation.
     */
    private DefaultArmorCustomizer() {}

    /**
     * The singleton instance of the default armor customizer.
     * <p>
     * This implementation creates standard {@link ArmorItem} instances with durability
     * calculated as: {@code armorType.getDurability(durabilityFactor)}.
     * </p>
     * <p>
     * <strong>Thread Safety:</strong> This instance is thread-safe and stateless.
     * </p>
     */
    public static ArmorCustomizer INSTANCE = ((type, material, durabilityFactor, props) -> {
        int durability = type.getArmorType().getDurability(durabilityFactor);
        return new ArmorItem(
                material.value(),
                type.getArmorType(),
                props.durability(durability)
        );
    });
}