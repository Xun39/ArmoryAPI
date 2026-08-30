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
    public Item create(ToolPieceType piece, ToolContext context, Item.Properties properties) {
        if (!VanillaToolPieces.STANDARD.contains(piece)) return ToolCustomizer.super.create(piece, context, properties);

        if (piece == VanillaToolPieces.SWORD) return createSword(context.tier(), properties);
        else if (piece == VanillaToolPieces.AXE) return createAxe(context.tier(), properties);
        else if (piece == VanillaToolPieces.PICKAXE) return createPickaxe(context.tier(), properties);
        else if (piece == VanillaToolPieces.SHOVEL) return createShovel(context.tier(), properties);
        else if (piece == VanillaToolPieces.HOE) return createHoe(context.tier(), properties);

        return ToolCustomizer.super.create(piece, context, properties);
    }

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
     * Processes a hit from a tool, applying the hit effect if the attack succeeded and the
     * game is not on the client side.
     * <p>
     * This method delegates to {@link #handleHitEffect(ToolPieceType, LivingEntity, LivingEntity)}
     * when conditions are met. It is intended to be called from within the overridden
     * {@code hurtEnemy} methods of the created tools.
     * </p>
     *
     * @param piece    the type of tool used
     * @param flag     the result of the original {@code hurtEnemy} call (true if the target was damaged)
     * @param target   the entity that was hit
     * @param attacker the entity that performed the attack
     * @return the same {@code flag} value, allowing chaining in {@code hurtEnemy}
     */
    protected boolean onHit(ToolPieceType piece, boolean flag, LivingEntity target, LivingEntity attacker) {
        if (flag && !target.level().isClientSide) {
            handleHitEffect(piece, target, attacker);
        }

        return flag;
    }

    /**
     * Creates a sword item that applies hit effects.
     *
     * @param tier       the material tier
     * @param properties the item properties
     * @return a new {@link SwordItem} with hit effect support
     */
    protected Item createSword(Tier tier, Item.Properties properties) {
        return new SwordItem(tier, properties) {
            @Override
            public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
                return onHit(
                        VanillaToolPieces.SWORD,
                        super.hurtEnemy(stack, target, attacker),
                        target,
                        attacker
                );
            }
        };
    }

    /**
     * Creates an axe item that applies hit effects.
     *
     * @param tier       the material tier
     * @param properties the item properties
     * @return a new {@link AxeItem} with hit effect support
     */
    protected Item createAxe(Tier tier, Item.Properties properties) {
        return new AxeItem(tier, properties) {
            @Override
            public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
                return onHit(
                        VanillaToolPieces.AXE,
                        super.hurtEnemy(stack, target, attacker),
                        target,
                        attacker
                );
            }
        };
    }

    /**
     * Creates a pickaxe item that applies hit effects.
     *
     * @param tier       the material tier
     * @param properties the item properties
     * @return a new {@link AxeItem} with hit effect support
     */
    protected Item createPickaxe(Tier tier, Item.Properties properties) {
        return new PickaxeItem(tier, properties) {
            @Override
            public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
                return onHit(
                        VanillaToolPieces.PICKAXE,
                        super.hurtEnemy(stack, target, attacker),
                        target,
                        attacker
                );
            }
        };
    }

    /**
     * Creates a shovel item that applies hit effects.
     *
     * @param tier       the material tier
     * @param properties the item properties
     * @return a new {@link AxeItem} with hit effect support
     */
    protected Item createShovel(Tier tier, Item.Properties properties) {
        return new ShovelItem(tier, properties) {
            @Override
            public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
                return onHit(
                        VanillaToolPieces.SHOVEL,
                        super.hurtEnemy(stack, target, attacker),
                        target,
                        attacker
                );
            }
        };
    }

    /**
     * Creates a hoe item that applies hit effects.
     *
     * @param tier       the material tier
     * @param properties the item properties
     * @return a new {@link AxeItem} with hit effect support
     */
    protected Item createHoe(Tier tier, Item.Properties properties) {
        return new HoeItem(tier, properties) {
            @Override
            public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
                return onHit(
                        VanillaToolPieces.HOE,
                        super.hurtEnemy(stack, target, attacker),
                        target,
                        attacker
                );
            }
        };
    }
}
