package net.xun.armory;

import net.xun.armory.api.item.tools.ToolPieceType;
import net.xun.armory.platform.Services;
import net.xun.armory.platform.services.IToolCompatModule;

import java.util.stream.Stream;

public class ArmoryCompat {
    private ArmoryCompat() {

    }

    public static Stream<ToolPieceType> toolPieces() {
        return Services.ACTIVE_COMPAT_MODULES.stream()
                .filter(module -> Services.PLATFORM.isModLoaded(module.targetModId()))
                .flatMap(IToolCompatModule::toolPieces);
    }
}
