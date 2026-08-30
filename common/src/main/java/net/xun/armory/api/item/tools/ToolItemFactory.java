package net.xun.armory.api.item.tools;

import net.minecraft.world.item.Item;

@FunctionalInterface
public interface ToolItemFactory {
    Item create(ToolPieceType piece, ToolContext context, Item.Properties properties);
}
