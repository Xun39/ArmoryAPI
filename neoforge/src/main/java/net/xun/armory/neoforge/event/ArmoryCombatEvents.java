package net.xun.armory.neoforge.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.xun.armory.ArmoryConstants;
import net.xun.armory.api.item.tools.ToolMetaData;
import net.xun.armory.api.item.tools.ToolMetaDataLookup;
import net.xun.armory.impl.item.tools.ToolHitEffectDispatcher;

@EventBusSubscriber(modid = ArmoryConstants.MOD_ID)
public final class ArmoryCombatEvents {

    private ArmoryCombatEvents() {
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Post event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker))
            return;

        ItemStack stack = attacker.getMainHandItem();
        ToolMetaData meta = ToolMetaDataLookup.get(stack);
        if (meta == null) {
            stack = attacker.getOffhandItem();
            meta = ToolMetaDataLookup.get(stack);
        }
        if (meta == null) return;

        ToolHitEffectDispatcher.maybeTriggerHitEffect(event.getEntity(), attacker, stack);
    }
}
