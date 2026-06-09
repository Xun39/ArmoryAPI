package net.xun.armory.api.item.tools;

public record ToolStats(float attackDamage, float attackSpeed) {
    public static final ToolStats ZERO = new ToolStats(0.0F, 0.0F);

    // Vanilla
    public static final ToolStats DEFAULT_SWORD = new ToolStats(3.0F, 1.6F);
    public static final ToolStats DEFAULT_AXE = new ToolStats(6.0F, 0.9F);
    public static final ToolStats DEFAULT_PICKAXE = new ToolStats(1.0F, 1.2F);
    public static final ToolStats DEFAULT_SHOVEL = new ToolStats(1.5F, 1.0F);
    public static final ToolStats DEFAULT_HOE = new ToolStats(-2.0F, 3.0F);

    // Modded

    // Knife from Farmer's Delight
    public static final ToolStats DEFAULT_KNIFE = new ToolStats(0.5F, 2.0F);
}
