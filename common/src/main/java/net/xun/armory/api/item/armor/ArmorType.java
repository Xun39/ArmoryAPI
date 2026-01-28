package net.xun.armory.api.item.armor;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;

/**
 * Enumerates armor piece types with their registration suffixes and equipment slots.
 * <p>
 * This enum defines the four standard Minecraft armor types, each with a specific
 * registration suffix, Minecraft armor type, and equipment slot.
 * </p>
 * <p>
 * Example naming: base name "diamond" becomes:
 * <ul>
 *   <li>HELMET: "diamond_helmet"</li>
 *   <li>CHESTPLATE: "diamond_chestplate"</li>
 *   <li>LEGGINGS: "diamond_leggings"</li>
 *   <li>BOOTS: "diamond_boots"</li>
 * </ul>
 */
public enum ArmorType {

    /** Head protection armor piece */
    HELMET("_helmet", ArmorItem.Type.HELMET, EquipmentSlot.HEAD),
    /** Body protection armor piece */
    CHESTPLATE("_chestplate", ArmorItem.Type.CHESTPLATE, EquipmentSlot.CHEST),
    /** Leg protection armor piece */
    LEGGINGS("_leggings", ArmorItem.Type.LEGGINGS, EquipmentSlot.LEGS),
    /** Foot protection armor piece */
    BOOTS("_boots", ArmorItem.Type.BOOTS, EquipmentSlot.FEET);

    private final String nameSuffix;
    private final ArmorItem.Type type;
    private final EquipmentSlot slot;

    /**
     * Constructs a new ArmorType with the specified properties.
     *
     * @param suffix The suffix to append to base names for registry IDs
     * @param type The Minecraft armor type used for protection calculations
     * @param slot The equipment slot this armor piece occupies
     */
    ArmorType(String suffix, ArmorItem.Type type, EquipmentSlot slot) {
        this.nameSuffix = suffix;
        this.type = type;
        this.slot = slot;
    }

    /**
     * Gets the registration suffix for this armor type.
     * <p>
     * The suffix is appended to the base name to form the full registry ID.
     * </p>
     *
     * @return The registration suffix for this armor type
     */
    public String getNameSuffix() {
        return nameSuffix;
    }

    /**
     * Gets the Minecraft armor type for protection calculations.
     * <p>
     * This type determines the base protection value and durability multiplier
     * for the armor piece.
     * </p>
     *
     * @return The Minecraft armor type
     */
    public ArmorItem.Type getType() {
        return type;
    }

    /**
     * Gets the equipment slot this armor piece occupies.
     * <p>
     * This determines where the armor piece is equipped on the player.
     * </p>
     *
     * @return The equipment slot for this armor type
     */
    public EquipmentSlot getSlot() {
        return slot;
    }
}
