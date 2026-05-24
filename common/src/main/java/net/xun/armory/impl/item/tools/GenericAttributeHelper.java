package net.xun.armory.impl.item.tools;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.xun.armory.api.item.tools.AttributeHelper;
import net.xun.armory.api.item.tools.ToolType;

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

    @Override
    public Item.Properties apply(Item.Properties properties, ToolType type, ToolMaterial material, float attackDamage, float attackSpeed) {
        return properties.attributes(createAttributeModifiers(attackDamage, attackSpeed));
    }

    private static ItemAttributeModifiers createAttributeModifiers(float attackDamage, float attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                Item.BASE_ATTACK_DAMAGE_ID,
                                attackDamage,
                                AttributeModifier.Operation.ADD_VALUE
                        ), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(
                                Item.BASE_ATTACK_SPEED_ID,
                                attackSpeed - 4,
                                AttributeModifier.Operation.ADD_VALUE
                        ), EquipmentSlotGroup.MAINHAND)
                .build();
    }
}