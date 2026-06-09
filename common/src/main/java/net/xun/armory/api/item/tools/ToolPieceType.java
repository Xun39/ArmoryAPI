package net.xun.armory.api.item.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.xun.armory.impl.item.PieceType;
import net.xun.armory.impl.item.tools.DefaultToolCustomizer;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public record ToolPieceType(
        String nameSuffix,
        ToolStats defaultStats,
        ToolCustomizer customizer,
        UnaryOperator<Item.Properties> propertiesModifier,
        Consumer<ItemAttributeModifiers.Builder> additionalAttributes
) implements PieceType {

    public ToolPieceType {
        nameSuffix = Objects.requireNonNull(nameSuffix, "nameSuffix");
        Objects.requireNonNull(defaultStats, "defaultStats");
        customizer = customizer == null ? DefaultToolCustomizer.INSTANCE : customizer;
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

    public TieredItem createItem(ToolContext context, Item.Properties properties) {
        ToolCustomizer effective = customizer != null ? customizer : context.customizer();
        return effective.create(this, context, properties);
    }

    public static Builder builder(String suffix) {
        return new Builder(suffix);
    }

    public static final class Builder {
        private final String suffix;
        private ToolStats defaultStats = ToolStats.ZERO;
        private ToolCustomizer customizer = DefaultToolCustomizer.INSTANCE;
        private UnaryOperator<Item.Properties> propertiesModifier = UnaryOperator.identity();
        private Consumer<net.minecraft.world.item.component.ItemAttributeModifiers.Builder> additionalAttributes = builder -> {};

        private Builder(String suffix) {
            this.suffix = suffix;
        }

        public Builder attackStats(ToolStats stats) {
            this.defaultStats = Objects.requireNonNull(stats, "stats");
            return this;
        }

        public Builder customizer(ToolCustomizer customizer) {
            this.customizer = customizer;
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
                    customizer,
                    propertiesModifier,
                    additionalAttributes
            );
        }
    }
}
