package net.xun.armory.api.item.tools;

import net.minecraft.world.item.*;
import net.xun.armory.impl.item.PieceType;

/**
 * @since 1.0.0
 */
@Deprecated(forRemoval = true, since = "3.0.0")
public enum ToolType implements PieceType {

    /** Sword tool type */
    SWORD("_sword", SwordItem.class),
    /** Axe tool type */
    AXE("_axe", AxeItem.class),
    /** Pickaxe tool type */
    PICKAXE("_pickaxe", PickaxeItem.class),
    /** Hoe tool type */
    HOE("_hoe", HoeItem.class),
    /** Shovel tool type */
    SHOVEL("_shovel", ShovelItem.class);

    /** Suffix appended to base name for registration */
    private final String nameSuffix;
    private final Class<? extends Item> vanillaClass;

    /**
     * Constructs a new ToolType with the specified metadata.
     *
     * @param registrationSuffix the suffix appended to base names for registry IDs
     * @param vanillaClass the vanilla Minecraft tool class for this tool type
     */
    ToolType(String registrationSuffix, Class<? extends Item> vanillaClass) {
        this.nameSuffix = registrationSuffix;
        this.vanillaClass = vanillaClass;
    }

    /**
     * Gets the registration suffix for this tool type.
     * <p>
     * This suffix is concatenated with the base name to form the complete
     * registry ID for the tool.
     * </p>
     *
     * @return the registration suffix, never {@code null}
     */
    @Override
    public String getNameSuffix() {
        return nameSuffix;
    }

    /**
     * Gets the vanilla Minecraft tool class for this tool type.
     * <p>
     * This class is used by to create standard
     * tool instances and can be used for type checking or reflection.
     * </p>
     *
     * @return the vanilla tool class for this type, never {@code null}
     */
    public Class<? extends Item> getVanillaClass() {
        return vanillaClass;
    }
}