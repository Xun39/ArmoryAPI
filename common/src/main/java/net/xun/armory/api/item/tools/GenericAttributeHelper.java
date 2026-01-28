package net.xun.armory.api.item.tools;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;

/**
 * Default implementation applying standard Minecraft attack attributes.
 * <p>
 * This implementation uses Minecraft's attribute system to apply attack damage
 * and attack speed to tools. It follows vanilla conventions where attack speed
 * is expressed as an offset from the base value of 4.0.
 * </p>
 * <p>
 * For example, an attack speed of 1.6 results in a modifier of -2.4
 * (1.6 - 4.0 = -2.4), which is the standard way Minecraft represents
 * attack speed in attributes.
 * </p>
 *
 * @see AttributeHelper
 * @see ItemAttributeModifiers
 */
public class GenericAttributeHelper implements AttributeHelper {

    /**
     * Applies standard combat attributes using Minecraft's attribute system.
     * <p>
     * This implementation adds attribute modifiers for {@link Attributes#ATTACK_DAMAGE}
     * and {@link Attributes#ATTACK_SPEED} to the item properties. The modifiers
     * are applied to the MAINHAND equipment slot.
     * </p>
     *
     * @param properties Initial item properties to modify
     * @param damage Total attack damage value (tier base + bonus)
     * @param speed Attack speed value (this is the actual attack speed, not an offset)
     * @return Modified properties with attack attributes
     * @throws NullPointerException if properties is null
     */
    @Override
    public Item.Properties applyAttributes(Item.Properties properties, float damage, float speed) {
        return properties.attributes(createAttributeModifiers(damage, speed));
    }

    /**
     * Creates attribute modifiers following vanilla Minecraft conventions.
     * <p>
     * The attack damage modifier uses {@link Item#BASE_ATTACK_DAMAGE_ID} and
     * adds the specified damage value. The attack speed modifier uses
     * {@link Item#BASE_ATTACK_SPEED_ID} and adds an offset from the base
     * speed of 4.0 (i.e., speed - 4.0).
     * </p>
     * <p>
     * Both modifiers are additive ({@link AttributeModifier.Operation#ADD_VALUE})
     * and apply only to the {@link EquipmentSlotGroup#MAINHAND}.
     * </p>
     *
     * @param damage Final attack damage value
     * @param speed Attack speed value (converted to offset from base 4.0)
     * @return Configured attribute modifiers container
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
