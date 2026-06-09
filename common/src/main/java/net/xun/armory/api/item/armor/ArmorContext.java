package net.xun.armory.api.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.xun.armory.api.item.tools.ToolPieceType;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public record ArmorContext(
        String setName,
        Holder<ArmorMaterial> material,
        int durabilityFactor,
        UnaryOperator<Item.Properties> propertiesModifier,
        Consumer<ItemAttributeModifiers.Builder> additionalAttributes,
        ArmorCustomizer customizer
) {
    public ArmorContext {
        Objects.requireNonNull(setName, "setName");
        Objects.requireNonNull(material, "material");
        Objects.requireNonNull(propertiesModifier, "propertiesModifier");
        Objects.requireNonNull(additionalAttributes, "additionalAttributes");
        Objects.requireNonNull(customizer, "defaultCustomizer");
    }

    public Item.Properties applyProperties(ArmorPieceType piece, Item.Properties base) {
        Item.Properties props = propertiesModifier.apply(base);
        return piece.propertiesModifier().apply(props);
    }

    public Consumer<ItemAttributeModifiers.Builder> combinedAttributes(ArmorPieceType piece) {
        return builder -> {
            additionalAttributes.accept(builder);
            piece.additionalAttributes().accept(builder);
        };
    }
}
