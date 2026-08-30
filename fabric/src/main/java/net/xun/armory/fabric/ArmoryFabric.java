package net.xun.armory.fabric;

import net.fabricmc.api.ModInitializer;
import net.xun.armory.ArmoryCommon;
import net.xun.armory.fabric.event.ArmoryCombatEvents;

public class ArmoryFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        ArmoryCommon.init();

        ArmoryCombatEvents.register();
    }
}
