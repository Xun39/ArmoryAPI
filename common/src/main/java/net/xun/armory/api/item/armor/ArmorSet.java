package net.xun.armory.api.item.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.xun.armory.impl.item.armor.ArmorFactory;
import net.xun.armory.impl.item.armor.DefaultArmorCustomizer;
import net.xun.armory.api.item.ItemSet;

import java.util.Objects;
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
 * <h2>Usage Example (with NeoForge):</h2>
 * <pre>{@code
 * // Create a basic armor set with default properties
 * ArmorMaterial diamondMaterial = ...;
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
 * @see ArmoryArmorType
 * @see ArmorCustomizer
 * @since 1.0.0
 */
public class ArmorSet extends ItemSet<ArmoryArmorType, ArmorItem> {

    /**
     * Constructs a new ArmorSet with the specified configuration.
     *
     * @param name base name for all armor pieces in the set (e.g., "diamond")
     * @param material holder for the armor material defining protection and toughness
     * @param durabilityFactor multiplier for the material's base durability
     * @param propertiesModifier supplier for item properties applied to all pieces
     * @param customizer strategy for creating individual armor items
     * @throws NullPointerException if any required parameter is {@code null}
     * @throws IllegalArgumentException if durabilityFactor is negative
     */
    protected ArmorSet(String name,
                       ArmorMaterial material,
                       int durabilityFactor,
                       UnaryOperator<Item.Properties> propertiesModifier,
                       ArmorCustomizer customizer
    ) {
        super(
                name,
                ArmoryArmorType.class,
                new ArmorFactory(material, durabilityFactor, propertiesModifier, customizer)
        );
    }

    /**
     * Gets the helmet item supplier from this armor set.
     * <p>
     * The supplier will create the helmet item upon first invocation and cache
     * the result for subsequent calls.
     * </p>
     *
     * @return supplier providing the registered helmet, never {@code null}
     */
    public Supplier<ArmorItem> getHelmet() {
        return get(ArmoryArmorType.HELMET);
    }

    /**
     * Gets the chestplate item supplier from this armor set.
     * <p>
     * The supplier will create the chestplate item upon first invocation and cache
     * the result for subsequent calls.
     * </p>
     *
     * @return supplier providing the registered chestplate, never {@code null}
     */
    public Supplier<ArmorItem> getChestplate() {
        return get(ArmoryArmorType.CHESTPLATE);
    }

    /**
     * Gets the leggings item supplier from this armor set.
     * <p>
     * The supplier will create the leggings item upon first invocation and cache
     * the result for subsequent calls.
     * </p>
     *
     * @return supplier providing the registered leggings, never {@code null}
     */
    public Supplier<ArmorItem> getLeggings() {
        return get(ArmoryArmorType.LEGGINGS);
    }

    /**
     * Gets the boots item supplier from this armor set.
     * <p>
     * The supplier will create the boots item upon first invocation and cache
     * the result for subsequent calls.
     * </p>
     *
     * @return supplier providing the registered boots, never {@code null}
     */
    public Supplier<ArmorItem> getBoots() {
        return get(ArmoryArmorType.BOOTS);
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
     * <pre>{@code
     * // Create netherite armor with fire resistance
     * ArmorSet netheriteArmor = new ArmorSet.Builder("netherite", NETHERITE_MATERIAL)
     *     .withDurabilityFactor(37)  // Standard netherite multiplier
     *     .withItemProperties(properties -> properties.fireResistant().rarity(Rarity.EPIC))
     *     .build();
     * }</pre>
     *
     * @see ArmorSet
     * @see ArmorCustomizer
     * @since 1.0.0
     */
    public static class Builder {
        private final String name;
        private final ArmorMaterial material;
        private int durabilityFactor;
        private UnaryOperator<Item.Properties> propertiesModifier = UnaryOperator.identity();
        private ArmorCustomizer customizer = DefaultArmorCustomizer.INSTANCE;

        /**
         * Constructs a new builder for an armor set with the specified base name and material.
         *
         * @param name base name for armor pieces (e.g., "iron"), will be appended with armor-specific suffixes
         * @param material armor material holder defining protection values and toughness
         * @throws NullPointerException if {@code name} or {@code material} is {@code null}
         * @throws IllegalArgumentException if {@code name} is empty or contains invalid characters
         */
        public Builder(String name, ArmorMaterial material) {
            this.name = Objects.requireNonNull(name, "name");
            this.material = Objects.requireNonNull(material, "material");
        }

        /**
         * Sets the durability multiplier for all armor pieces in the set.
         * <p>
         * The final durability for each piece is calculated as:
         * <code>armor type base durability × durabilityFactor</code>
         * </p>
         * <strong>Vanilla Reference Values:</strong>
         * <table border="1">
         *   <caption>Vanilla Durability Factors</caption>
         *   <thead>
         *     <tr><th>Material</th><th>Durability Factor</th></tr>
         *   </thead>
         *   <tbody>
         *     <tr><td>Leather</td><td>5</td></tr>
         *     <tr><td>Chain</td><td>15</td></tr>
         *     <tr><td>Iron</td><td>15</td></tr>
         *     <tr><td>Gold</td><td>7</td></tr>
         *     <tr><td>Diamond</td><td>33</td></tr>
         *     <tr><td>Netherite</td><td>37</td></tr>
         *   </tbody>
         * </table>
         * <table border="1">
         *   <caption>Vanilla Armor Type Base Durability</caption>
         *   <thead>
         *     <tr><th>Armor type</th><th>Base Durability</th></tr>
         *   </thead>
         *   <tbody>
         *     <tr><td>Helmet</td><td>11</td></tr>
         *     <tr><td>Chestplate</td><td>16</td></tr>
         *     <tr><td>Leggings</td><td>15</td></tr>
         *     <tr><td>Boots</td><td>13</td></tr>
         *   </tbody>
         * </table>
         *
         * @param durabilityFactor multiplier for base armor type durability
         * @return this builder for method chaining
         * @throws IllegalArgumentException if {@code durabilityFactor} is negative
         */
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
         * Sets a custom armor creation strategy for specialized armor types.
         * <p>
         * This allows overriding the default armor creation behavior to implement
         * custom armor classes, modified durability calculations, or additional
         * properties.
         * </p>
         *
         * @param customizer armor creation strategy implementation
         * @return this builder for method chaining
         * @throws NullPointerException if {@code customizer} is {@code null}
         * @see ArmorCustomizer
         */
        public Builder withCustomizer(ArmorCustomizer customizer) {
            this.customizer = Objects.requireNonNull(customizer, "customizer");
            return this;
        }

        /**
         * Constructs the configured {@link ArmorSet}.
         * <p>
         * Validates all configuration and creates a new ArmorSet instance with
         * the specified properties. The returned ArmorSet is immutable.
         * </p>
         *
         * @return new armor set instance
         * @throws IllegalStateException if required configuration is invalid (currently none)
         */
        public ArmorSet build() {
            return new ArmorSet(this.name, this.material, this.durabilityFactor, this.propertiesModifier, this.customizer);
        }
    }
}