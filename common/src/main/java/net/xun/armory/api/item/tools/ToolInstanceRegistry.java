package net.xun.armory.api.item.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.IdentityHashMap;
import java.util.Map;

public final class ToolInstanceRegistry {

    private static final Map<Item, ToolInstance> INSTANCES = new IdentityHashMap<>();

    private ToolInstanceRegistry() {
    }

    public static void register(Item item, ToolInstance instance) {
        INSTANCES.put(item, instance);
    }

    public static ToolInstance get(Item item) {
        return INSTANCES.get(item);
    }

    public static ToolInstance get(ItemStack stack) {
        return get(stack.getItem());
    }

    public static boolean contains(Item item) {
        return INSTANCES.containsKey(item);
    }

    public static boolean contains(ItemStack stack) {
        return contains(stack.getItem());
    }
}
