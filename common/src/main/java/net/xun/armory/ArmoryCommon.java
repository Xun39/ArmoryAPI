package net.xun.armory;

import net.xun.armory.platform.Services;

public class ArmoryCommon {

    public static void init() {
        ArmoryConstants.LOG.info("Loading XunLib version {} for {}!", ArmoryConstants.VERSION, Services.PLATFORM.getPlatformName());
    }
}