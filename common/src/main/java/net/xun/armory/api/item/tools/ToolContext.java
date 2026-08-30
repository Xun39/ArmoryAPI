package net.xun.armory.api.item.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public record ToolContext(
        String setName,
        Tier tier,
        Map<ToolPieceType, ToolStats> statsByPiece,
        UnaryOperator<Item.Properties> propertiesModifier,
        Consumer<ItemAttributeModifiers.Builder> additionalAttributes
) {
    public ToolContext {
        Objects.requireNonNull(setName, "setName");
        Objects.requireNonNull(tier, "tier");
        Objects.requireNonNull(statsByPiece, "statsByPiece");
        Objects.requireNonNull(propertiesModifier, "propertiesModifier");
        Objects.requireNonNull(additionalAttributes, "additionalAttributes");
    }

    public ToolStats statsFor(ToolPieceType piece) {
        ToolStats stats = statsByPiece.get(piece);
        if (stats == null) {
            throw new IllegalArgumentException("Missing ToolStats for piece: " + piece.getNameSuffix());
        }
        return stats;
    }

    /**
     * Applies all property modifiers (set-level and piece-level) and builds the
     * final attribute modifiers. Returns a new {@link Item.Properties} instance.
     */
    public Item.Properties applyProperties(ToolPieceType piece, Item.Properties base) {
        Objects.requireNonNull(piece, "piece");
        Objects.requireNonNull(base, "base");

        Item.Properties props = propertiesModifier.apply(base);
        props = piece.propertiesModifier().apply(props);

        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        statsFor(piece).addBaseAttributes(tier, builder);

        additionalAttributes.accept(builder);
        piece.additionalAttributes().accept(builder);

        return props.attributes(builder.build());
    }
}
