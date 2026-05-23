package net.xun.armory.api.item.tools;

import net.minecraft.world.item.*;
import net.xun.armory.impl.item.tools.DefaultToolCustomizer;
import net.xun.armory.impl.item.PieceType;

/**
 * Enumerates the five standard tool types in Minecraft with associated metadata.
 * <p>
 * </p>
 * This enum defines the complete set of craftable tools, each with:
 * <ul>
 *   <li>A registration suffix for automatic naming</li>
 *   <li>The corresponding vanilla Minecraft tool class</li>
 * </ul>
 * The naming convention follows Minecraft's standard: {@code base_name + suffix}.
 * <p>
 * <strong>Important:</strong> The iteration order of values is fixed as:
 * [SWORD, AXE, PICKAXE, HOE, SHOVEL]. This order must be respected when using
 * array-based configuration methods like
 * {@link ToolSet.Builder#withToolStats(float[], float[])}.
 * </p>
 * <p>
 * </p>
 * <strong>Example Naming:</strong> For base name "iron":
 * <table border="1">
 *   <caption>Tool Piece Naming</caption>
 *   <tr><th>ToolType</th><th>Full Name</th><th>Vanilla Class</th></tr>
 *   <tr><td>SWORD</td><td>iron_sword</td><td>{@link SwordItem}</td></tr>
 *   <tr><td>AXE</td><td>iron_axe</td><td>{@link AxeItem}</td></tr>
 *   <tr><td>PICKAXE</td><td>iron_pickaxe</td><td>{@link PickaxeItem}</td></tr>
 *   <tr><td>HOE</td><td>iron_hoe</td><td>{@link HoeItem}</td></tr>
 *   <tr><td>SHOVEL</td><td>iron_shovel</td><td>{@link ShovelItem}</td></tr>
 * </table>
 *
 * @see ToolSet
 * @see DefaultToolCustomizer
 * @since 1.0.0
 */
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
     * This class is used by {@link DefaultToolCustomizer} to create standard
     * tool instances and can be used for type checking or reflection.
     * </p>
     *
     * @return the vanilla tool class for this type, never {@code null}
     */
    public Class<? extends Item> getVanillaClass() {
        return vanillaClass;
    }
}