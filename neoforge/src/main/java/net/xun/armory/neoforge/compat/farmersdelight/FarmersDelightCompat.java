package net.xun.armory.neoforge.compat.farmersdelight;

import net.xun.armory.api.item.tools.behavior.ToolBehaviors;
import net.xun.armory.api.item.tools.ToolPieceType;
import net.xun.armory.platform.services.IToolCompatModule;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.stream.Stream;

public class FarmersDelightCompat implements IToolCompatModule {
    private static final String MOD_ID = "farmersdelight";

    private static ToolPieceType KNIFE;

    @Override
    public String targetModId() {
        return MOD_ID;
    }

    public static ToolPieceType knife() {
        if (KNIFE == null)
            KNIFE = createKnife();

        return KNIFE;
    }

    private static ToolPieceType createKnife() {
        return ToolPieceType.builder("_knife")
                .behavior(ToolBehaviors.mining(ModTags.Blocks.MINEABLE_WITH_KNIFE))
                .customizer((piece, context, properties) ->
                        new KnifeItem(context.tier(), context.applyProperties(piece, properties))
                )
                .build();
    }

    @Override
    public Stream<ToolPieceType> toolPieces() {
        return Stream.of(KNIFE);
    }
}
