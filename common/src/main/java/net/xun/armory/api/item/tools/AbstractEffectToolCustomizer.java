package net.xun.armory.api.item.tools;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.function.Consumer;

/**
 * @since 2.1.0
 */
public abstract class AbstractEffectToolCustomizer implements ToolCustomizer {

    /**
     * Handles the hit effect for a specific tool type on the target entity.
     * <p>
     * This method is called only on the server side and only when the attack successfully
     * damages the target. Subclasses must implement this method to define the actual effect,
     * such as applying potion effects, dealing additional damage, or playing sounds.
     * </p>
     *
     * @param piece    the type of tool that caused the hit
     * @param target   the entity that was hit
     * @param attacker the entity that performed the attack
     */
    protected abstract void handleHitEffect(ToolPieceType piece, LivingEntity target, LivingEntity attacker);

    /**
     * Invoked internally by the platform combat integration.
     */
    public final void handleHit(ToolPieceType piece, LivingEntity target, LivingEntity attacker) {
        if (!target.level().isClientSide) {
            handleHitEffect(piece, target, attacker);
        }
    }
}
