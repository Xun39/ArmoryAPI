package net.xun.armory.forge;

import net.minecraftforge.fml.common.Mod;
import net.xun.armory.ArmoryCommon;
import net.xun.armory.ArmoryConstants;

@Mod(ArmoryConstants.MOD_ID)
public class ArmoryForge {

    public ArmoryForge() {
        ArmoryCommon.init();
    }
}