package net.xun.armory.impl.item.tools;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.xun.armory.api.item.tools.AttributeHelper;

/**
 * Default implementation of {@link AttributeHelper} applying standard
 * Minecraft attack attributes using vanilla's attribute modifier system.
 * <p>
 * This implementation follows vanilla Minecraft conventions for applying
 * combat attributes to tools, ensuring compatibility with the game's
 * attribute system and consistent behavior across different tool types.
 * </p>
 * <p>
 * <strong>Attack Speed Convention:</strong>
 * </p>
 * Minecraft internally represents attack speed as an offset from a base
 * value of 4.0. This implementation automatically
 * converts the provided attack speed value to the appropriate modifier
 * offset. For example:
 * <ul>
 *   <li>An attack speed of 1.6 becomes: 1.6 - 4.0 = -2.4 modifier</li>
 *   <li>An attack speed of 3.0 becomes: 3.0 - 4.0 = -1.0 modifier</li>
 *   <li>An attack speed of 4.0 becomes: 4.0 - 4.0 = 0.0 modifier</li>
 * </ul>
 *
 * @see AttributeHelper
 * @see ItemAttributeModifiers
 * @see Attributes
 * @see AttributeModifier
 * @since 1.0.0
 */
public class GenericAttributeHelper implements AttributeHelper {

    /**
     * Applies standard combat attributes to item properties using Minecraft's
     * attribute modifier system.
     * <p>
     * This method enhances the provided item properties by adding attribute
     * modifiers for attack damage and attack speed. Both modifiers are applied
     * exclusively to the {@link EquipmentSlotGroup#MAINHAND} equipment slot.
     * </p>
     *
     * @param properties initial item properties to modify, never {@code null}
     * @param damage total attack damage value (tier base damage + tool bonus),
     *               typically between 0.0F and 15.0F for balanced tools
     * @param speed attack speed value in attacks per second (not the modifier offset),
     *              typically between 0.5F and 4.0F for balanced tools
     * @return modified properties with attack damage and speed attribute modifiers,
     *         never {@code null}
     * @throws NullPointerException if {@code properties} is {@code null}
     * @throws IllegalArgumentException if attribute values would result in
     *         nonsensical modifiers (e.g., negative attack damage for weapons)
     *
     * @see #createAttributeModifiers(float, float)
     */
    @Override
    public Item.Properties applyAttributes(Item.Properties properties, float damage, float speed) {
        return properties.attributes(createAttributeModifiers(damage, speed));
    }

    /**
     * Creates attribute modifiers for attack damage and speed following
     * vanilla Minecraft conventions.
     * <p>
     * This method constructs an {@link ItemAttributeModifiers} instance containing
     * two attribute modifiers:
     * <ol>
     *   <li>Attack damage modifier with ID {@link Item#BASE_ATTACK_DAMAGE_ID}</li>
     *   <li>Attack speed modifier with ID {@link Item#BASE_ATTACK_SPEED_ID},
     *       converting the speed value to an offset from the base 4.0</li>
     * </ol>
     * Both modifiers are additive and apply only to the main hand equipment slot.
     * </p>
     *
     * </p>
     *
     * @param damage total attack damage value to apply,
     *               directly used as the modifier amount
     * @param speed attack speed value in attacks per second,
     *              converted to modifier offset internally
     * @return configured attribute modifiers container with both damage and
     *         speed modifiers, never {@code null}
     *
     * @see AttributeModifier
     * @see ItemAttributeModifiers.Builder
     */
    private static ItemAttributeModifiers createAttributeModifiers(float damage, float speed) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                Item.BASE_ATTACK_DAMAGE_ID,
                                damage,
                                AttributeModifier.Operation.ADD_VALUE
                        ), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(
                                Item.BASE_ATTACK_SPEED_ID,
                                speed - 4,
                                AttributeModifier.Operation.ADD_VALUE
                        ),EquipmentSlotGroup.MAINHAND)
                .build();
    }
}
