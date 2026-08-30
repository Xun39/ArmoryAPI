package net.xun.armory.api.item.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;

public final class ToolItemFactories {
    private ToolItemFactories() {
    }

    public static ToolItemFactory simple(VanillaItemFactory vanillaFactory) {
        return (piece, context, properties) -> vanillaFactory.create(context.tier(), properties);
    }

    @FunctionalInterface
    public interface VanillaItemFactory {
        TieredItem create(Tier tier, Item.Properties properties);
    }
}
