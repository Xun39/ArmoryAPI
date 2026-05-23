package net.xun.armory.api.item.tools;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;

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

    /**
     * Creates a tool of the specified type with the given tier and item properties.
     * <p>
     * The returned tool overrides {@link Item#hurtEnemy(ItemStack, LivingEntity, LivingEntity)}
     * to call {@link #onHit(ToolType, boolean, LivingEntity, LivingEntity)} and apply the
     * hit effect if the attack succeeds.
     * </p>
     *
     * @param type       the type of tool to create (SWORD, AXE, PICKAXE, HOE, SHOVEL)
     * @param tier       the material tier of the tool
     * @param properties the item properties (durability, crafting group, etc.)
     * @return a new tool item that applies a hit effect on successful attacks
     * @throws IllegalArgumentException if the tool type is not supported
     */
    @Override
    public Item createTool(ToolType type, Tier tier, Item.Properties properties) {
        return switch (type) {
            case SWORD -> createSword(tier, properties);
            case AXE -> createAxe(tier, properties);
            case PICKAXE -> createPickaxe(tier, properties);
            case HOE -> createHoe(tier, properties);
            case SHOVEL -> createShovel(tier, properties);
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
                        ToolType.SWORD,
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
                        ToolType.AXE,
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
                        ToolType.PICKAXE,
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
                        ToolType.HOE,
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
                        ToolType.SHOVEL,
                        super.hurtEnemy(stack, target, attacker),
                        target,
                        attacker
                );
            }
        };
    }
}
