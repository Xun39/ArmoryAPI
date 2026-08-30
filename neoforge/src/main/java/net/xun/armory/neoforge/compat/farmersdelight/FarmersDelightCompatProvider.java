package net.xun.armory.neoforge.compat.farmersdelight;

import net.xun.armory.platform.IToolCompatModule;
import net.xun.armory.platform.services.IToolCompatProvider;

public class FarmersDelightCompatProvider implements IToolCompatProvider {
    private static final String MOD_ID = "farmersdelight";

    @Override
    public String targetModId() {
        return MOD_ID;
    }

    @Override
    public IToolCompatModule create() {
        return new FarmersDelightCompat();
    }
}
