package net.xun.armory.neoforge.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.xun.armory.ArmoryConstants;
import net.xun.armory.api.item.tools.AbstractEffectToolCustomizer;
import net.xun.armory.api.item.tools.ToolInstance;
import net.xun.armory.api.item.tools.ToolInstanceRegistry;

@EventBusSubscriber(modid = ArmoryConstants.MOD_ID)
public final class ArmoryCombatEvents {

    private ArmoryCombatEvents() {
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Post event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker))
            return;

        ItemStack stack = attacker.getMainHandItem();
        ToolInstance instance = ToolInstanceRegistry.get(stack);

        if (instance == null) return;
        if (!(instance.customizer() instanceof AbstractEffectToolCustomizer customizer))
            return;

        customizer.handleHit(
                instance.piece(),
                event.getEntity(),
                attacker
        );
    }
}
