package net.xun.armory.api.item.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.xun.armory.impl.item.PieceType;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public record ArmorPieceType(
        String nameSuffix,
        ArmorItem.Type vanillaType,
        ArmorCustomizer customizer,
        UnaryOperator<Item.Properties> propertiesModifier,
        Consumer<ItemAttributeModifiers.Builder> additionalAttributes
) implements PieceType {

    public ArmorPieceType {
        nameSuffix = Objects.requireNonNull(nameSuffix, "nameSuffix");
        Objects.requireNonNull(vanillaType, "vanillaType");
        propertiesModifier = propertiesModifier == null ? UnaryOperator.identity() : propertiesModifier;
        additionalAttributes = additionalAttributes == null ? builder -> {} : additionalAttributes;

        if (nameSuffix.isBlank()) {
            throw new IllegalArgumentException("nameSuffix cannot be blank");
        }
        if (!nameSuffix.startsWith("_")) {
            throw new IllegalArgumentException("nameSuffix must start with '_', got: " + nameSuffix);
        }
    }

    @Override
    public String getNameSuffix() {
        return nameSuffix;
    }

    public Item createItem(ArmorContext context, Item.Properties properties) {
        ArmorCustomizer effective = customizer != null ? customizer : context.customizer();
        return effective.create(this, context, properties);
    }

    public static Builder builder(String suffix) {
        return new Builder(suffix);
    }

    public static final class Builder {
        private final String suffix;
        private ArmorItem.Type vanillaType;
        private ArmorCustomizer customizer = null;
        private UnaryOperator<Item.Properties> propertiesModifier = UnaryOperator.identity();
        private Consumer<net.minecraft.world.item.component.ItemAttributeModifiers.Builder> additionalAttributes = builder -> {};

        private Builder(String suffix) {
            this.suffix = suffix;
        }

        public Builder vanillaType(ArmorItem.Type type) {
            this.vanillaType = Objects.requireNonNull(type, "type");
            return this;
        }

        public Builder customizer(ArmorCustomizer customizer) {
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

        public ArmorPieceType build() {
            return new ArmorPieceType(
                    suffix,
                    vanillaType,
                    customizer,
                    propertiesModifier,
                    additionalAttributes
            );
        }
    }
}
