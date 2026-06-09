package net.xun.armory.api.item.armor;

import net.minecraft.world.item.ArmorItem;

import java.util.List;

public final class VanillaArmorPieces {
    private VanillaArmorPieces() {

    }

    public static final ArmorPieceType HELMET = ArmorPieceType.builder("_helmet")
            .vanillaType(ArmorItem.Type.HELMET)
            .build();

    public static final ArmorPieceType CHESTPLATE = ArmorPieceType.builder("_chestplate")
            .vanillaType(ArmorItem.Type.CHESTPLATE)
            .build();

    public static final ArmorPieceType LEGGINGS = ArmorPieceType.builder("_leggings")
            .vanillaType(ArmorItem.Type.LEGGINGS)
            .build();

    public static final ArmorPieceType BOOTS = ArmorPieceType.builder("_boots")
            .vanillaType(ArmorItem.Type.BOOTS)
            .build();

    public static final ArmorPieceType BODY = ArmorPieceType.builder("_body")
            .vanillaType(ArmorItem.Type.BODY)
            .build();

    public static final List<ArmorPieceType> STANDARD = List.of(HELMET, CHESTPLATE, LEGGINGS, BOOTS, BODY);
    public static final List<ArmorPieceType> PLAYER = List.of(HELMET, CHESTPLATE, LEGGINGS, BOOTS);
}
