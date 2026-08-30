package net.xun.armory;

import net.xun.armory.api.item.tools.ToolPieceType;
import net.xun.armory.platform.Services;
import net.xun.armory.platform.IToolCompatModule;

import java.util.stream.Stream;

public class ArmoryCompat {
    private ArmoryCompat() {
    }

    public static Stream<ToolPieceType> toolPieces() {
        return Services.ACTIVE_COMPAT_MODULES.stream()
                .flatMap(IToolCompatModule::toolPieces);
    }
}
