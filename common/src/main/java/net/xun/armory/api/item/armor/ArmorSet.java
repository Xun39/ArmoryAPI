package net.xun.armory.api.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.xun.armory.api.item.tools.ToolContext;
import net.xun.armory.api.item.tools.ToolCustomizer;
import net.xun.armory.api.item.tools.ToolPieceType;
import net.xun.armory.api.item.tools.ToolStats;
import net.xun.armory.impl.item.armor.DefaultArmorCustomizer;
import net.xun.armory.api.item.ItemSet;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Represents a complete set of armor consisting of helmet, chestplate, leggings, and boots.
 * <p>
 * This class provides a convenient abstraction for creating and managing a full armor set
 * with consistent properties across all pieces. Each armor piece is lazily initialized
 * upon first access and can be retrieved individually or as a complete collection.
 * </p>
 *
 * <h2>Usage Example (with NeoForge) :</h2>
 *
 * <pre>{@code
 * // Create a basic armor set with default properties
 * Holder<ArmorMaterial> diamondMaterial = ...;
 * ArmorSet diamondArmor = new ArmorSet.Builder("diamond", diamondMaterial)
 *     .withDurabilityFactor(33)  // Standard diamond multiplier
 *     .build();
 *
 * // Register all armor pieces
 * Map<ResourceLocation, Supplier<ArmorItem>> items = diamondArmor.getItemsForRegistration("mymod");
 * items.forEach((id, supplier) -> ITEMS.register(id.getPath(), supplier));
 *
 * // Access individual pieces
 * Supplier<ArmorItem> helmet = diamondArmor.getHelmet();
 * Supplier<ArmorItem> chestplate = diamondArmor.getChestplate();
 * }</pre>
 *
 * @see Builder
 * @see ArmorCustomizer
 * @since 1.0.0
 */
public class ArmorSet extends ItemSet<ArmorPieceType, Item> {

    private final Holder<ArmorMaterial> material;
    private final List<ArmorPieceType> pieces;
    private final int durabilityFactor;
    private final ArmorContext context;

    /**
     * Constructs a new ArmorSet with the specified configuration.
     *
     * @param name               base name for all armor pieces in the set (e.g., "diamond")
     * @param material           holder for the armor material defining protection and toughness
     * @param propertiesModifier supplier for item properties applied to all pieces
     * @param customizer         strategy for creating individual armor items
     * @throws NullPointerException     if any required parameter is {@code null}
     * @throws IllegalArgumentException if durabilityFactor is negative
     */
    protected ArmorSet(
            String name,
            Holder<ArmorMaterial> material,
            List<ArmorPieceType> pieces,
            int durabilityFactor,
            UnaryOperator<Item.Properties> propertiesModifier,
            Consumer<ItemAttributeModifiers.Builder> additionalAttributes,
            ArmorCustomizer customizer
    ) {
        super(name, pieces, makeFactory(name, material, durabilityFactor, propertiesModifier, additionalAttributes, customizer));

        this.material = Objects.requireNonNull(material, "material");
        this.pieces = List.copyOf(pieces);
        this.durabilityFactor = durabilityFactor;
        this.context = new ArmorContext(name, material, durabilityFactor, propertiesModifier, additionalAttributes, customizer);
    }

    private static BiFunction<ArmorPieceType, Item.Properties, Item> makeFactory(
            String name,
            Holder<ArmorMaterial> material,
            int durabilityFactor,
            UnaryOperator<Item.Properties> propertiesModifier,
            Consumer<ItemAttributeModifiers.Builder> additionalAttributes,
            ArmorCustomizer customizer
    ) {
        ArmorContext context = new ArmorContext(
                name,
                material,
                durabilityFactor,
                propertiesModifier,
                additionalAttributes,
                customizer
        );

        return (piece, properties) -> piece.createItem(context, properties);
    }

    public Holder<ArmorMaterial> getMaterial() {
        return material;
    }

    public int getDurabilityFactor() {
        return durabilityFactor;
    }

    public ArmorContext getContext() {
        return context;
    }

    public Supplier<Item> getHelmet() {
        return super.get(VanillaArmorPieces.HELMET);
    }
    public Supplier<Item> getChestplate() {
        return super.get(VanillaArmorPieces.CHESTPLATE);
    }
    public Supplier<Item> getLeggings() {
        return super.get(VanillaArmorPieces.LEGGINGS);
    }
    public Supplier<Item> getBoots() {
        return super.get(VanillaArmorPieces.BOOTS);
    }
    public Supplier<Item> getBody() {
        return super.get(VanillaArmorPieces.BODY);
    }

    public static Builder builder(String name, Holder<ArmorMaterial> material) {
        return new Builder(name, material);
    }

    /**
     * Builder for constructing {@link ArmorSet} instances with a fluent API.
     * <p>
     * This builder enables configuration of shared properties across all armor pieces
     * in a set, including durability multipliers, item properties, and custom creation logic.
     * </p>
     * <strong>Default Values:</strong>
     * <ul>
     *   <li>Durability factor: 0 (uses material default)</li>
     *   <li>Properties supplier: {@code Item.Properties::new}</li>
     *   <li>Customizer: {@link DefaultArmorCustomizer#INSTANCE}</li>
     * </ul>
     *
     * <h2>Example Usage:</h2>
     *
     * <pre>{@code
     * // Create netherite armor with fire resistance
     * ArmorSet netheriteArmor = new ArmorSet.Builder("netherite", NETHERITE_MATERIAL)
     *     .withDurabilityFactor(37)  // Standard netherite multiplier
     *     .withItemPropertiesSupplier(() -> new Item.Properties()
     *         .fireResistant()
     *         .rarity(Rarity.EPIC))
     *     .build();
     * }</pre>
     *
     * @see ArmorSet
     * @see ArmorCustomizer
     * @since 1.0.0
     */
    public static class Builder {
        private final String name;
        private final Holder<ArmorMaterial> material;
        private final List<ArmorPieceType> pieces = new ArrayList<>();
        private int durabilityFactor;
        private UnaryOperator<Item.Properties> propertiesModifier = UnaryOperator.identity();
        private Consumer<ItemAttributeModifiers.Builder> additionalAttributes = builder -> {};
        private ArmorCustomizer customizer = DefaultArmorCustomizer.INSTANCE;

        /**
         * Constructs a new builder for an armor set with the specified base name and material.
         *
         * @param name     base name for armor pieces (e.g., "iron"), will be appended with armor-specific suffixes
         * @param material armor material holder defining protection values and toughness
         * @throws NullPointerException     if {@code name} or {@code material} is {@code null}
         * @throws IllegalArgumentException if {@code name} is empty or contains invalid characters
         */
        private Builder(String name, Holder<ArmorMaterial> material) {
            this.name = Objects.requireNonNull(name, "name");
            this.material = Objects.requireNonNull(material, "material");
        }

        public Builder piece(ArmorPieceType piece) {
            Objects.requireNonNull(piece, "piece");
            if (pieces.contains(piece)) {
                throw new IllegalArgumentException("Duplicate piece: " + piece.getNameSuffix());
            }
            pieces.add(piece);
            return this;
        }

        public Builder pieces(Collection<ArmorPieceType> pieces) {
            Objects.requireNonNull(pieces, "pieces");
            for (ArmorPieceType piece : pieces) {
                piece(piece);
            }
            return this;
        }

        public Builder withDurabilityFactor(int durabilityFactor) {
            this.durabilityFactor = durabilityFactor;
            return this;
        }

        /**
         * Sets a modifier for the {@link Item.Properties} used when creating each armor item.
         * <p>
         * The provided function receives the default properties (initially an empty {@code Properties}
         * instance) and can modify them as needed – for example, to set fire resistance, rarity,
         * or custom durability.
         * </p>
         *
         * @param propertiesModifier a function that transforms the base properties
         * @return this builder for method chaining
         * @throws NullPointerException if {@code propertiesModifier} is {@code null}
         */
        public Builder withItemProperties(UnaryOperator<Item.Properties> propertiesModifier) {
            this.propertiesModifier = Objects.requireNonNull(propertiesModifier, "propertiesModifier");
            return this;
        }

        /**
         * @deprecated Use {{@link #withItemProperties(UnaryOperator)}} instead
         */
        @Deprecated(forRemoval = true, since = "3.0.0")
        public Builder withItemPropertiesSupplier(Supplier<Item.Properties> propertiesSupplier) {
            this.propertiesModifier = properties -> propertiesSupplier.get();
            return this;
        }

        /**
         * Adds a consumer that can further modify the {@link ItemAttributeModifiers.Builder}
         * after the default tool attributes have been applied.
         * <p>
         * This is useful for adding extra attribute modifiers (e.g., movement speed, knockback resistance)
         * to all tools in the set.
         * </p>
         *
         * @param additionalAttributes consumer that receives the attribute builder
         * @return this builder for method chaining
         * @throws NullPointerException if {@code additionalAttributes} is {@code null}
         */
        public Builder withAdditionalAttributes(Consumer<ItemAttributeModifiers.Builder> additionalAttributes) {
            this.additionalAttributes = Objects.requireNonNull(additionalAttributes, "additionalAttributes");
            return this;
        }

        /**
         * Sets a custom armor creation strategy for specialized armor types.
         * <p>
         * This allows overriding the default armor creation behavior to implement
         * custom armor classes, modified durability calculations, or additional
         * properties.
         * </p>
         *
         * @param customizer armor creation strategy implementation
         * @return this builder for method chaining
         * @throws NullPointerException if {@code itemFactory} is {@code null}
         * @see ArmorCustomizer
         */
        public Builder withCustomizer(ArmorCustomizer customizer) {
            this.customizer = customizer;
            return this;
        }

        /**
         * Constructs the configured {@link ArmorSet}.
         * <p>
         * Validates all configuration and creates a new ArmorSet instance with
         * the specified properties. The returned ArmorSet is immutable.
         * </p>
         *
         * @return New armor set instance
         * @throws IllegalStateException if required configuration is invalid
         */
        public ArmorSet build() {
            if (pieces.isEmpty()) {
                throw new IllegalStateException("ArmorSet '" + name + "' has no pieces");
            }
            return new ArmorSet(name, material, pieces, durabilityFactor, propertiesModifier, additionalAttributes, customizer);
        }
    }
}