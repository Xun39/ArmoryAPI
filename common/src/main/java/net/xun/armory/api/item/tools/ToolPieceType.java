package net.xun.armory.api.item.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.xun.armory.impl.item.PieceType;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public record ToolPieceType(
        String nameSuffix,
        ToolStats defaultStats,
        ToolItemFactory itemFactory,
        UnaryOperator<Item.Properties> propertiesModifier,
        Consumer<ItemAttributeModifiers.Builder> additionalAttributes
) implements PieceType {

    public ToolPieceType {
        nameSuffix = Objects.requireNonNull(nameSuffix, "nameSuffix");
        Objects.requireNonNull(defaultStats, "defaultStats");
        Objects.requireNonNull(itemFactory, "itemFactory");
        propertiesModifier = propertiesModifier == null ? UnaryOperator.identity() : propertiesModifier;
        additionalAttributes = additionalAttributes == null ? builder -> {} : additionalAttributes;

        if (nameSuffix.isBlank()) {
            throw new IllegalArgumentException("nameSuffix cannot be blank");
        }
        if (!nameSuffix.startsWith("_")) {
            throw new IllegalArgumentException("nameSuffix must start with '_'");
        }
    }

    @Override
    public String getNameSuffix() {
        return nameSuffix;
    }

    /**
     * Creates an item using this piece's factory and the provided context/properties.
     * This method does NOT apply any properties modifiers; it expects the properties
     * to already be final.
     */
    public Item createItem(ToolContext context, Item.Properties properties) {
        return itemFactory.create(this, context, properties);
    }

    public static Builder builder(String suffix) {
        return new Builder(suffix);
    }

    public static final class Builder {
        private final String suffix;
        private ToolStats defaultStats = ToolStats.ZERO;
        private ToolItemFactory factory;
        private UnaryOperator<Item.Properties> propertiesModifier = UnaryOperator.identity();
        private Consumer<net.minecraft.world.item.component.ItemAttributeModifiers.Builder> additionalAttributes = builder -> {};
        private Class<? extends Item> itemClass;

        private Builder(String suffix) {
            this.suffix = suffix;
        }

        public Builder attackStats(ToolStats stats) {
            this.defaultStats = Objects.requireNonNull(stats, "stats");
            return this;
        }

        public Builder factory(ToolItemFactory factory) {
            this.factory = factory;
            return this;
        }

        public Builder properties(UnaryOperator<Item.Properties> propertiesModifier) {
            this.propertiesModifier = Objects.requireNonNull(propertiesModifier, "propertiesModifier");
            return this;
        }

        public Builder additionalAttributes(Consumer<net.minecraft.world.item.component.ItemAttributeModifiers.Builder> additionalAttributes) {
            this.additionalAttributes = Objects.requireNonNull(additionalAttributes, "additionalAttributes");
            return this;
        }

        public ToolPieceType build() {
            return new ToolPieceType(
                    suffix,
                    defaultStats,
                    factory,
                    propertiesModifier,
                    additionalAttributes
            );
        }
    }
}
