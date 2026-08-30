package net.xun.armory.impl.item.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.xun.armory.api.item.armor.ArmorContext;
import net.xun.armory.api.item.armor.ArmorCustomizer;
import net.xun.armory.api.item.armor.ArmorPieceType;
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
 * access the default itemFactory implementation.
 * </p>
 *
 * @see ArmorCustomizer
 * @see ArmorSet.Builder
 * @since 2.0.0
 */
public enum DefaultArmorCustomizer implements ArmorCustomizer {
    INSTANCE;

    @Override
    public Item create(ArmorPieceType piece, ArmorContext context, Item.Properties properties) {
        ArmorItem.Type vanillaType = piece.vanillaType();
        Item.Properties finalProps = context.applyProperties(piece, properties.durability(vanillaType.getDurability(context.durabilityFactor())));

        return new ArmorItem(
                context.material(),
                vanillaType,
                finalProps
        );
    }
}
