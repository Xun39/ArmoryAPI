package net.xun.armory.api.item.armor;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.equipment.ArmorType;
import net.xun.armory.impl.item.PieceType;

/**
 * Enumerates the four standard armor piece types in Minecraft with their associated metadata.
 * <p>
 * This enum defines the complete set of wearable armor pieces, each with:
 * </p>
 * <ul>
 *   <li>A registration suffix for automatic naming</li>
 *   <li>An {@link ArmorType} for protection and durability calculations</li>
 *   <li>An {@link EquipmentSlot} for player equipment management</li>
 * </ul>
 * The naming convention follows Minecraft's standard: {@code base_name + suffix}.
 * <p></p>
 * <strong>Example Naming:</strong> For base name "diamond":
 * <table border="1">
 *   <caption>Armor Piece Naming</caption>
 *   <tr><th>ArmorType</th><th>Full Name</th></tr>
 *   <tr><td>HELMET</td><td>diamond_helmet</td></tr>
 *   <tr><td>CHESTPLATE</td><td>diamond_chestplate</td></tr>
 *   <tr><td>LEGGINGS</td><td>diamond_leggings</td></tr>
 *   <tr><td>BOOTS</td><td>diamond_boots</td></tr>
 * </table>
 *
 * @see ArmorSet
 * @see ArmorType
 * @since 1.0.0
 */
public enum ArmoryArmorType implements PieceType {

    HELMET("_helmet", ArmorType.HELMET, EquipmentSlot.HEAD),
    CHESTPLATE("_chestplate", ArmorType.CHESTPLATE, EquipmentSlot.CHEST),
    LEGGINGS("_leggings", ArmorType.LEGGINGS, EquipmentSlot.LEGS),
    BOOTS("_boots", ArmorType.BOOTS, EquipmentSlot.FEET);

    private final String nameSuffix;
    private final ArmorType type;
    private final EquipmentSlot slot;

    /**
     * Constructs a new ArmorType with the specified metadata.
     *
     * @param suffix the suffix appended to base names for registry IDs
     * @param type the Minecraft armor type used for protection calculations
     * @param slot the equipment slot this armor piece occupies
     */
    ArmoryArmorType(String suffix, ArmorType type, EquipmentSlot slot) {
        this.nameSuffix = suffix;
        this.type = type;
        this.slot = slot;
    }

    /**
     * Gets the registration suffix for this armor type.
     * <p>
     * This suffix is concatenated with the base name to form the complete
     * registry ID for the armor piece.
     * </p>
     *
     * @return the registration suffix, never {@code null}
     */
    @Override
    public String getNameSuffix() {
        return nameSuffix;
    }

    /**
     * Gets the Minecraft armor type for protection and durability calculations.
     *
     * @return the Minecraft armor type, never {@code null}
     */
    public ArmorType getArmorType() {
        return type;
    }

    /**
     * Gets the equipment slot where this armor piece is worn.
     * <p>
     * This determines the player inventory slot and equipment rendering
     * position for the armor piece.
     * </p>
     *
     * @return the equipment slot for this armor type, never {@code null}
     */
    public EquipmentSlot getEquipmentSlot() {
        return slot;
    }
}
