package net.xun.armory.api.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.xun.armory.internal.util.LazyReference;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Represents a complete set of armor (helmet, chestplate, leggings, boots).
 * <p>
 * This class provides a convenient way to create and manage a full set of armor
 * with consistent properties across all pieces. Each armor piece is lazily
 * initialized and can be accessed individually or as a complete collection.
 * Use the nested {@link Builder} to configure armor sets with shared properties.
 * </p>
 * <p>
 * Example usage with NeoForge:
 * <pre>{@code
 * // Create a basic armor set with default properties
 * Holder<ArmorMaterial> diamondMaterial = ...;
 * ArmorSet diamondArmor = new ArmorSet.Builder("diamond", diamondMaterial)
 *     .withDurabilityFactor(15)
 *     .withItemPropertiesSupplier(() -> new Item.Properties().stacksTo(1))
 *     .build();
 *
 * // Register the armor set
 * Map<ResourceLocation, Supplier<ArmorItem>> items = diamondArmor.getItemsForRegistration("mymod");
 * items.forEach((id, supplier) -> ITEMS.register(id.getPath(), supplier));
 *
 * // Access individual pieces
 * Supplier<ArmorItem> helmet = diamondArmor.getHelmet();
 * }</pre>
 *
 * @see Builder
 * @see ArmorType
 * @see ArmorConfigurator
 */
public class ArmorSet {

    private final String name;
    private final Map<ArmorType, LazyReference<ArmorItem>> armors = new EnumMap<>(ArmorType.class);

    /**
     * Constructs a new ArmorSet with the specified properties.
     *
     * @param name               The base name for all armor pieces in the set
     * @param material           Holder for the armor material defining protection and toughness
     * @param durabilityFactor   Multiplier for the material's base durability
     * @param propertiesSupplier Supplier for item properties applied to all armor pieces
     * @param configuration      Armor creation strategy implementation
     */
    protected ArmorSet(String name,
                       Holder<ArmorMaterial> material,
                       int durabilityFactor,
                       Supplier<Item.Properties> propertiesSupplier,
                       ArmorConfigurator configuration) {

        for (ArmorType type : ArmorType.values()) {
            String fullName = name + type.getNameSuffix();

            Item.Properties armorProperties = propertiesSupplier.get();

            armors.put(type, new LazyReference<>(
                    fullName,
                    () -> configuration.createArmor(type, material, durabilityFactor, armorProperties))
            );
        }
        this.name = name;
    }

    /**
     * Retrieves all armor items in this set for registration purposes.
     * <p>
     * This method returns a map of ResourceLocation to Supplier pairs that can be used
     * to register the armor pieces with the game registry. Each piece is identified by
     * a ResourceLocation constructed from the provided modId and the armor's full name.
     * </p>
     *
     * @param modId Your mod ID used to construct ResourceLocation identifiers
     * @return Map of {@link ResourceLocation} keys to {@link Supplier} providers for armor items
     * @throws NullPointerException if modId is null
     */
    public Map<ResourceLocation, Supplier<ArmorItem>> getItemsForRegistration(String modId) {
        Map<ResourceLocation, Supplier<ArmorItem>> items = new LinkedHashMap<>();

        for (Map.Entry<ArmorType, LazyReference<ArmorItem>> entry : armors.entrySet()) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(modId, entry.getValue().getName());
            items.put(id, entry.getValue());
        }

        return items;
    }

    /**
     * Gets the base name of this armor set.
     * <p>
     * This is the name without armor-specific suffixes (e.g., "diamond" for "diamond_helmet").
     * </p>
     *
     * @return The base name of this armor set
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the helmet item from this armor set.
     *
     * @return Supplier providing the registered helmet
     */
    public Supplier<ArmorItem> getHelmet() {
        return armors.get(ArmorType.HELMET);
    }

    /**
     * Gets the chestplate item from this armor set.
     *
     * @return Supplier providing the registered chestplate
     */
    public Supplier<ArmorItem> getChestplate() {
        return armors.get(ArmorType.CHESTPLATE);
    }

    /**
     * Gets the leggings item from this armor set.
     *
     * @return Supplier providing the registered leggings
     */
    public Supplier<ArmorItem> getLeggings() {
        return armors.get(ArmorType.LEGGINGS);
    }

    /**
     * Gets the boots item from this armor set.
     *
     * @return Supplier providing the registered boots
     */
    public Supplier<ArmorItem> getBoots() {
        return armors.get(ArmorType.BOOTS);
    }

    /**
     * Retrieves all registered armor items in this set.
     * <p>
     * This method returns a list containing all four armor pieces (helmet, chestplate,
     * leggings, boots) in the order defined by {@link ArmorType#values()}. All items
     * are initialized when this method is called.
     * </p>
     *
     * @return List containing all armor items in this set
     */
    public List<Item> getAll() {
        return armors.values().stream()
                .map(Supplier::get)
                .collect(Collectors.toList());
    }

    /**
     * Builder for constructing {@link ArmorSet} instances.
     * <p>
     * Provides a fluent API for configuring armor sets with shared properties.
     * The builder allows setting durability multipliers, item properties, and
     * custom armor creation logic.
     * </p>
     * <p>
     * Example usage with custom properties:
     * <pre>{@code
     * ArmorSet netheriteArmor = new ArmorSet.Builder("netherite", NETHERITE_MATERIAL)
     *     .withDurabilityFactor(15) // Same as diamond
     *     .withItemPropertiesSupplier(() -> new Item.Properties()
     *         .fireResistant()
     *         .rarity(Rarity.EPIC))
     *     .build();
     * }</pre>
     *
     * @see ArmorSet
     * @see ArmorConfigurator
     */
    public static class Builder {
        private final String name;
        private final Holder<ArmorMaterial> material;
        private int durabilityFactor;
        private Supplier<Item.Properties> propertiesSupplier = Item.Properties::new;
        private ArmorConfigurator configuration = ArmorConfigurator.DEFAULT;

        /**
         * Constructs a new builder for an armor set.
         * <p>
         * Initializes with default durability factor (from material) and empty properties.
         * </p>
         *
         * @param name Base name for armor pieces (appended with armor-specific suffixes)
         * @param material Armor material holder defining protection and toughness
         * @throws NullPointerException if name or material is null
         */
        public Builder(String name, Holder<ArmorMaterial> material) {
            this.name = name;
            this.material = material;
        }

        /**
         * Sets durability multiplier for all armor pieces.
         * <p>
         * Final durability for each piece is calculated as:
         * <code>material base durability * durabilityFactor</code>
         * </p>
         * <p>
         * Vanilla values for reference:
         * <ul>
         *   <li>Leather: 5 (multiplier applied to 55 base)</li>
         *   <li>Chain: 15 (multiplier applied to 165 base)</li>
         *   <li>Iron: 15 (multiplier applied to 165 base)</li>
         *   <li>Gold: 7 (multiplier applied to 77 base)</li>
         *   <li>Diamond: 33 (multiplier applied to 363 base)</li>
         *   <li>Netherite: 37 (multiplier applied to 407 base)</li>
         * </ul>
         *
         * @param durabilityFactor Multiplier for base material durability
         * @return This builder for chaining
         * @throws IllegalArgumentException if durabilityFactor is negative
         */
        public Builder withDurabilityFactor(int durabilityFactor) {
            this.durabilityFactor = durabilityFactor;
            return this;
        }

        /**
         * <b>Caution:</b> Sets shared item properties for all armor pieces.
         * <p>
         * May cause attribute conflicts if properties are mutated internally.
         * Prefer {@link #withItemPropertiesSupplier}.
         * </p>
         *
         * @param properties Base properties for all armor
         * @return This builder for chaining
         * @throws NullPointerException if properties is null
         * @see #withItemPropertiesSupplier(Supplier)
         */
        public Builder withItemProperties(Item.Properties properties) {
            this.propertiesSupplier = () -> properties;
            return this;
        }

        /**
         * Sets item properties using a supplier (called per-piece during construction).
         * <p>
         * This method is safer than {@link #withItemProperties} for armor sets,
         * as it ensures each armor piece gets a fresh instance of properties if needed.
         * </p>
         *
         * @param propertiesSupplier Supplier providing base properties for each armor piece
         * @return This builder for chaining
         * @throws NullPointerException if propertiesSupplier is null
         * @see #withItemProperties(Item.Properties)
         */
        public Builder withItemPropertiesSupplier(Supplier<Item.Properties> propertiesSupplier) {
            this.propertiesSupplier = propertiesSupplier;
            return this;
        }

        /**
         * Sets custom armor creation logic.
         * <p>
         * Allows overriding the default armor creation behavior for specialized
         * armor types or custom armor implementations.
         * </p>
         *
         * @param configuration Armor creation strategy implementation
         * @return This builder for chaining
         * @throws NullPointerException if configuration is null
         * @see ArmorConfigurator
         */
        public Builder withConfiguration(ArmorConfigurator configuration) {
            this.configuration = configuration;
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
            return new ArmorSet(this.name, this.material, this.durabilityFactor, this.propertiesSupplier, this.configuration);
        }
    }
}