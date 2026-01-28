package net.xun.armory.api.item.tools;

import net.minecraft.world.item.*;

/**
 * Enumerates tool types with their registration suffixes and factory methods.
 * <p>
 * This enum defines the five standard Minecraft tool types, each with a specific
 * registration suffix and factory method for creating tool instances.
 * The order of values is [SWORD, AXE, PICKAXE, HOE, SHOVEL].
 * </p>
 *
 * @see #create(Tier, Item.Properties)
 * @see ToolFactory
 */
public enum ToolType {

    /** Sword tool type */
    SWORD("_sword", SwordItem::new),
    /** Axe tool type */
    AXE("_axe", AxeItem::new),
    /** Pickaxe tool type */
    PICKAXE("_pickaxe", PickaxeItem::new),
    /** Hoe tool type */
    HOE("_hoe", HoeItem::new),
    /** Shovel tool type */
    SHOVEL("_shovel", ShovelItem::new);

    /** Suffix appended to base name for registration */
    private final String nameSuffix;
    private final ToolFactory factory;

    /**
     * Constructs a new ToolType with the specified registration suffix and factory.
     *
     * @param registrationSuffix The suffix to append to base names for registry IDs
     * @param factory The factory method for creating tool instances of this type
     */
    ToolType(String registrationSuffix, ToolFactory factory) {
        this.nameSuffix = registrationSuffix;
        this.factory = factory;
    }

    /**
     * Creates a tool item instance of this type.
     * <p>
     * Delegates to the factory method associated with this tool type.
     * </p>
     *
     * @param tier Material tier for tool durability and mining level
     * @param properties Base item properties (durability, enchantability, etc.)
     * @return Configured tool item instance
     * @throws NullPointerException if tier or properties is null
     */
    public Item create(Tier tier, Item.Properties properties) {
        return factory.create(tier, properties);
    }

    /**
     * Gets the registration suffix for this tool type.
     * <p>
     * The suffix is appended to the base name to form the full registry ID.
     * For example, a base name "diamond" with suffix "_sword" becomes "diamond_sword".
     * </p>
     *
     * @return The registration suffix for this tool type
     */
    public String getNameSuffix() {
        return nameSuffix;
    }

    /**
     * Functional interface for tool item construction.
     * <p>
     * Each implementation creates a specific type of tool item with the given
     * tier and properties.
     * </p>
     */
    @FunctionalInterface
    interface ToolFactory {
        /**
         * Creates a tool item instance.
         *
         * @param tier Material tier for tool durability and mining level
         * @param props Item properties (durability, enchantability, etc.)
         * @return New tool item instance
         */
        Item create(Tier tier, Item.Properties props);
    }
}