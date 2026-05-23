package net.xun.armory.impl;

import net.xun.armory.platform.Services;

public class ArmoryCommon {

    public static void init() {
        ArmoryConstants.LOG.info("Loading ArmoryAPI version {} for {}!", ArmoryConstants.VERSION, Services.PLATFORM.getPlatformName());
    }
}