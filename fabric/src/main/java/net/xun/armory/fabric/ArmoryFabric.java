package net.xun.armory.fabric;

import net.fabricmc.api.ModInitializer;
import net.xun.armory.ArmoryCommon;

public class ArmoryFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        ArmoryCommon.init();
    }
}
