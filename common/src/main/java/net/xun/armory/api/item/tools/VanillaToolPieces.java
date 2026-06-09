package net.xun.armory.api.item.tools;

import net.minecraft.world.item.*;

import java.util.List;

public final class VanillaToolPieces {
    private VanillaToolPieces() {

    }

    public static final ToolPieceType SWORD = ToolPieceType.builder("_sword")
            .attackStats(ToolStats.DEFAULT_SWORD)
            .customizer((piece, context, properties) -> new SwordItem(context.tier(), context.applyProperties(piece, properties)))
            .build();

    public static final ToolPieceType AXE = ToolPieceType.builder("_axe")
            .attackStats(ToolStats.DEFAULT_AXE)
            .customizer((piece, context, properties) -> new AxeItem(context.tier(), context.applyProperties(piece, properties)))
            .build();

    public static final ToolPieceType PICKAXE = ToolPieceType.builder("_pickaxe")
            .attackStats(ToolStats.DEFAULT_PICKAXE)
            .customizer((piece, context, properties) -> new PickaxeItem(context.tier(), context.applyProperties(piece, properties)))
            .build();

    public static final ToolPieceType SHOVEL = ToolPieceType.builder("_shovel")
            .attackStats(ToolStats.DEFAULT_SHOVEL)
            .customizer((piece, context, properties) -> new ShovelItem(context.tier(), context.applyProperties(piece, properties)))
            .build();

    public static final ToolPieceType HOE = ToolPieceType.builder("_hoe")
            .attackStats(ToolStats.DEFAULT_HOE)
            .customizer((piece, context, properties) -> new HoeItem(context.tier(), context.applyProperties(piece, properties)))
            .build();

    public static final List<ToolPieceType> STANDARD = List.of(SWORD, AXE, PICKAXE, SHOVEL, HOE);
}
