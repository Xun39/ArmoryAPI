package net.xun.armory.neoforge.compat.farmersdelight;

import net.xun.armory.platform.IToolCompatModule;
import net.xun.armory.platform.services.IToolCompatProvider;

public class FarmersDelightCompatProvider implements IToolCompatProvider {
    @Override
    public String targetModId() {
        return "farmersdelight";
    }

    @Override
    public IToolCompatModule create() {
        return new FarmersDelightCompat();
    }
}
