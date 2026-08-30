package net.xun.armory.neoforge.compat.farmersdelight;

import net.xun.armory.api.item.tools.ToolItemFactories;
import net.xun.armory.api.item.tools.ToolPieceType;
import net.xun.armory.api.item.tools.ToolStats;
import net.xun.armory.platform.IToolCompatModule;
import vectorwing.farmersdelight.common.item.KnifeItem;

import java.util.stream.Stream;

public class FarmersDelightCompat implements IToolCompatModule {
    private static ToolPieceType KNIFE;

    public static ToolPieceType knife() {
        if (KNIFE == null)
            KNIFE = createKnife();

        return KNIFE;
    }

    private static ToolPieceType createKnife() {
        return ToolPieceType.builder("_knife")
                .attackStats(ToolStats.DEFAULT_KNIFE)
                .factory(ToolItemFactories.simple(KnifeItem::new))
                .build();
    }

    @Override
    public Stream<ToolPieceType> toolPieces() {
        return Stream.of(knife());
    }
}
