package net.xun.armory.api.item.tools;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.function.Consumer;

/**
 * @since 2.1.0
 */
public abstract class AbstractEffectToolCustomizer implements ToolCustomizer {

    @Override
    public TieredItem create(ToolPieceType piece, ToolContext context, Item.Properties properties) {
        ToolStats stats = context.statsFor(piece);
        Item.Properties finalProps = context.applyProperties(piece, properties);

        return new ToolItem(piece, context.tier(), stats, finalProps, context.combinedAttributes(piece)) {
            @Override
            public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
                return onHit(
                        piece,
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
     * @param piece  the type of tool that caused the hit
     * @param target    the entity that was hit
     * @param attacker  the entity that performed the attack
     */
    protected abstract void handleHitEffect(ToolPieceType piece, LivingEntity target, LivingEntity attacker);

    /**
     * Processes a hit from a tool, applying the hit effect if the attack succeeded and the
     * game is not on the client side.
     * <p>
     * This method delegates to {@link #handleHitEffect(ToolPieceType, LivingEntity, LivingEntity)}
     * when conditions are met. It is intended to be called from within the overridden
     * {@code hurtEnemy} methods of the created tools.
     * </p>
     *
     * @param piece  the type of tool used
     * @param flag      the result of the original {@code hurtEnemy} call (true if the target was damaged)
     * @param target    the entity that was hit
     * @param attacker  the entity that performed the attack
     * @return the same {@code flag} value, allowing chaining in {@code hurtEnemy}
     */
    protected boolean onHit(ToolPieceType piece, boolean flag, LivingEntity target, LivingEntity attacker) {
        if (flag && !target.level().isClientSide) {
            handleHitEffect(piece, target, attacker);
        }

        return flag;
    }
}
