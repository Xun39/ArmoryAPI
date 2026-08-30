package net.xun.armory.fabric.event;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.xun.armory.api.item.tools.AbstractEffectToolCustomizer;
import net.xun.armory.api.item.tools.ToolInstance;
import net.xun.armory.api.item.tools.ToolInstanceRegistry;

public final class ArmoryCombatEvents {
    private ArmoryCombatEvents() {
    }

    public static void register() {
        AttackEntityCallback.EVENT.register(
                (player, level, hand, target, hitResult) -> {
                    if (!(target instanceof LivingEntity livingTarget))
                        return InteractionResult.PASS;

                    ItemStack stack = player.getItemInHand(hand);
                    ToolInstance instance = ToolInstanceRegistry.get(stack);

                    if (instance == null) return InteractionResult.PASS;
                    if (!(instance.customizer() instanceof AbstractEffectToolCustomizer customizer))
                        return InteractionResult.PASS;

                    customizer.handleHit(
                            instance.piece(),
                            livingTarget,
                            player
                    );

                    return InteractionResult.PASS;
                }
        );
    }
}
