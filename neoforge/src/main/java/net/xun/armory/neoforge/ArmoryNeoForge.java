package net.xun.armory.neoforge;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.xun.armory.impl.ArmoryCommon;
import net.xun.armory.impl.ArmoryConstants;

@Mod(ArmoryConstants.MOD_ID)
public class ArmoryNeoForge {

    public ArmoryNeoForge(IEventBus eventBus) {
        ArmoryCommon.init();
    }
}