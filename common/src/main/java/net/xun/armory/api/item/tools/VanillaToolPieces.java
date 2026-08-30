package net.xun.armory.api.item.tools;

import net.minecraft.world.item.*;

import java.util.List;

public final class VanillaToolPieces {
    private VanillaToolPieces() {

    }

    public static final ToolPieceType SWORD = ToolPieceType.builder("_sword")
            .attackStats(ToolStats.DEFAULT_SWORD)
            .factory(ToolItemFactories.simple(SwordItem::new))
            .build();

    public static final ToolPieceType AXE = ToolPieceType.builder("_axe")
            .attackStats(ToolStats.DEFAULT_AXE)
            .factory(ToolItemFactories.simple(AxeItem::new))
            .build();

    public static final ToolPieceType PICKAXE = ToolPieceType.builder("_pickaxe")
            .attackStats(ToolStats.DEFAULT_PICKAXE)
            .factory(ToolItemFactories.simple(PickaxeItem::new))
            .build();

    public static final ToolPieceType SHOVEL = ToolPieceType.builder("_shovel")
            .attackStats(ToolStats.DEFAULT_SHOVEL)
            .factory(ToolItemFactories.simple(ShovelItem::new))
            .build();

    public static final ToolPieceType HOE = ToolPieceType.builder("_hoe")
            .attackStats(ToolStats.DEFAULT_HOE)
            .factory(ToolItemFactories.simple(HoeItem::new))
            .build();

    public static final List<ToolPieceType> STANDARD = List.of(SWORD, AXE, PICKAXE, SHOVEL, HOE);
}
