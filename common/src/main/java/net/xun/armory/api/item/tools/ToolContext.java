package net.xun.armory.api.item.tools;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
        Consumer<ItemAttributeModifiers.Builder> additionalAttributes,
        ToolCustomizer customizer
) {
    public ToolContext {
        Objects.requireNonNull(setName, "setName");
        Objects.requireNonNull(tier, "tier");
        Objects.requireNonNull(statsByPiece, "statsByPiece");
        Objects.requireNonNull(propertiesModifier, "propertiesModifier");
        Objects.requireNonNull(additionalAttributes, "additionalAttributes");
        Objects.requireNonNull(customizer, "defaultCustomizer");
    }

    public ToolStats statsFor(ToolPieceType piece) {
        ToolStats stats = statsByPiece.get(piece);
        if (stats == null) {
            throw new IllegalArgumentException("No stats found for piece: " + piece.getNameSuffix());
        }
        return stats;
    }

    public Item.Properties applyProperties(ToolPieceType piece, Item.Properties base) {
        Item.Properties props = propertiesModifier.apply(base);

        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        createToolAttributes(tier, statsFor(piece), builder);

        if (additionalAttributes != null)
            combinedAttributes(piece);

        return piece.propertiesModifier().apply(props)
                .attributes(builder.build());
    }

    public Consumer<ItemAttributeModifiers.Builder> combinedAttributes(ToolPieceType piece) {
        return builder -> {
            additionalAttributes.accept(builder);
            piece.additionalAttributes().accept(builder);
        };
    }

    private static void createToolAttributes(Tier tier, ToolStats stats, ItemAttributeModifiers.Builder builder) {
        builder.add(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                        Item.BASE_ATTACK_DAMAGE_ID,
                        stats.attackDamage() + tier.getAttackDamageBonus(),
                        AttributeModifier.Operation.ADD_VALUE
                ), EquipmentSlotGroup.MAINHAND
        );
        builder.add(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(
                        Item.BASE_ATTACK_SPEED_ID,
                        stats.attackSpeed() - 4,
                        AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        );
    }
}
