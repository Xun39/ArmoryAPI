package net.xun.armory.api.item.tools;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.function.Consumer;

/**
 * Abstract base class for tool customizers that apply hit effects depending on the tool type.
 * <p>
 * This class implements {@link ToolCustomizer} and provides a template for creating Minecraft
 * tools (sword, axe, pickaxe, hoe, shovel) that trigger a custom effect when they successfully
 * hit an enemy. Subclasses only need to implement {@link #handleHitEffect(ToolType, LivingEntity, LivingEntity)}
 * to define the actual effect logic.
 * </p>
 *
 * <p>The hit effect is only executed on the server side and only when the attack actually
 * damages the target (i.e., {@code hurtEnemy} returns {@code true}).</p>
 *
 * @see ToolCustomizer
 * @see ToolType
 * @since 2.1.0
 */
public abstract class AbstractEffectToolCustomizer implements ToolCustomizer {

    @Override
    public ToolItem create(ToolType type, Tier tier, Item.Properties properties, float attackDamage, float attackSpeed, Consumer<ItemAttributeModifiers.Builder> additionalAttributes) {
        return new ToolItem(type, tier, properties, attackDamage, attackSpeed, additionalAttributes) {
            @Override
            public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
                return onHit(
                        type,
                        super.hurtEnemy(stack, target, attacker),
                        target,
                        attacker
                );
            }
        };
    }

    /**
     * Handles the hit effect for a specific tool type on the target entity.
     * <p>
     * This method is called only on the server side and only when the attack successfully
     * damages the target. Subclasses must implement this method to define the actual effect,
     * such as applying potion effects, dealing additional damage, or playing sounds.
     * </p>
     *
     * @param toolType  the type of tool that caused the hit
     * @param target    the entity that was hit
     * @param attacker  the entity that performed the attack
     */
    protected abstract void handleHitEffect(ToolType toolType, LivingEntity target, LivingEntity attacker);

    /**
     * Processes a hit from a tool, applying the hit effect if the attack succeeded and the
     * game is not on the client side.
     * <p>
     * This method delegates to {@link #handleHitEffect(ToolType, LivingEntity, LivingEntity)}
     * when conditions are met. It is intended to be called from within the overridden
     * {@code hurtEnemy} methods of the created tools.
     * </p>
     *
     * @param toolType  the type of tool used
     * @param flag      the result of the original {@code hurtEnemy} call (true if the target was damaged)
     * @param target    the entity that was hit
     * @param attacker  the entity that performed the attack
     * @return the same {@code flag} value, allowing chaining in {@code hurtEnemy}
     */
    protected boolean onHit(ToolType toolType, boolean flag, LivingEntity target, LivingEntity attacker) {
        if (flag && !target.level().isClientSide) {
            handleHitEffect(toolType, target, attacker);
        }

        return flag;
    }
}
