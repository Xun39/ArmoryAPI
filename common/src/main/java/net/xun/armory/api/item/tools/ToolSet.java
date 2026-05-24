package net.xun.armory.api.item.tools;

import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.xun.armory.impl.item.tools.DefaultToolCustomizer;
import net.xun.armory.api.item.ItemSet;
import net.xun.armory.impl.item.tools.ToolFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Represents a complete set of tools including sword, axe, pickaxe, shovel, and hoe.
 * <p>
 * This class provides a convenient abstraction for creating and managing a full
 * tool set with consistent properties and naming across all pieces. Each tool
 * is lazily initialized upon first access and can be retrieved individually or
 * as a complete collection.
 * </p>
 *
 * <h2>Usage Example (with NeoForge):</h2>
 * <pre>{@code
 * // Create a diamond tool set with vanilla balance
 * ToolSet diamondTools = new ToolSet.Builder("diamond", ToolMaterial.DIAMOND)
 *     .withVanillaBalance()
 *     .build();
 *
 * // Register all tool pieces
 * for (Map.Entry<ResourceLocation, Function<Item.Properties, ToolItem>> entry : diamondTools.getPiecesForRegistration(modID).entrySet()) {
 *     ResourceLocation id = entry.getKey();
 *     Function<Item.Properties, ToolItem> factory = entry.getValue();
 *
 *     var holder = ITEMS.registerItem(id.getPath(), factory);
 *     diamondTools.bind(id.getPath(), holder);
 * }
 *
 * // Access individual tools (with proper type casting)
 * Supplier<SwordItem> sword = diamondTools.getSword();
 * Supplier<AxeItem> axe = diamondTools.getAxe();
 * }</pre>
 *
 * @see Builder
 * @see ToolType
 * @see ToolCustomizer
 * @since 1.0.0
 */
public class ToolSet extends ItemSet<ToolType, ToolItem> {

    /**
     * Constructs a new ToolSet with the specified configuration.
     *
     * @param name                 base name for all tools in the set (e.g., "diamond")
     * @param toolMaterial         material tier for all tools, never {@code null}
     * @param attackDamage         map of attack damage bonuses per tool type,
     *                             never {@code null}
     * @param attackSpeed          map of attack speed modifiers per tool type,
     *                             never {@code null}
     * @param propertiesModifier   supplier for item properties applied to all tools,
     *                             never {@code null}
     * @param customizer           strategy for creating individual tool items,
     *                             never {@code null}
     * @param additionalAttributes helper for applying combat attributes,
     *                             never {@code null}
     * @throws NullPointerException     if any required parameter is {@code null}
     * @throws IllegalArgumentException if attack maps are incomplete or invalid
     */
    protected ToolSet(
            String name,
            ToolMaterial toolMaterial,
            EnumMap<ToolType, Float> attackDamage,
            EnumMap<ToolType, Float> attackSpeed,
            UnaryOperator<Item.Properties> propertiesModifier,
            ToolCustomizer customizer,
            Consumer<ItemAttributeModifiers.Builder> additionalAttributes
    ) {

        super(
                name,
                ToolType.class,
                new ToolFactory(toolMaterial, attackDamage, attackSpeed, propertiesModifier, customizer, additionalAttributes)
        );
    }

    /**
     * Gets the sword item supplier from this tool set.
     * <p>
     * The supplier will create the sword item upon first invocation and cache
     * the result for subsequent calls.
     * </p>
     *
     * @return supplier providing the registered {@link SwordItem}, never {@code null}
     */
    public Supplier<SwordItem> getSword() {
        return getTool(ToolType.SWORD);
    }

    /**
     * Gets the axe item supplier from this tool set.
     * <p>
     * The supplier will create the axe item upon first invocation and cache
     * the result for subsequent calls.
     * </p>
     *
     * @return supplier providing the registered {@link AxeItem}, never {@code null}
     */
    public Supplier<AxeItem> getAxe() {
        return getTool(ToolType.AXE);
    }

    /**
     * Gets the pickaxe item supplier from this tool set.
     * <p>
     * The supplier will create the pickaxe item upon first invocation and cache
     * the result for subsequent calls.
     * </p>
     *
     * @return supplier providing the registered {@link PickaxeItem}, never {@code null}
     */
    public Supplier<PickaxeItem> getPickaxe() {
        return getTool(ToolType.PICKAXE);
    }

    /**
     * Gets the hoe item supplier from this tool set.
     * <p>
     * The supplier will create the hoe item upon first invocation and cache
     * the result for subsequent calls.
     * </p>
     *
     * @return supplier providing the registered {@link HoeItem}, never {@code null}
     */
    public Supplier<HoeItem> getHoe() {
        return getTool(ToolType.HOE);
    }

    /**
     * Gets the shovel item supplier from this tool set.
     * <p>
     * The supplier will create the shovel item upon first invocation and cache
     * the result for subsequent calls.
     * </p>
     *
     * @return supplier providing the registered {@link ShovelItem}, never {@code null}
     */
    public Supplier<ShovelItem> getShovel() {
        return getTool(ToolType.SHOVEL);
    }

    /**
     * Internal helper method for type-safe tool retrieval.
     *
     * @param <T>  expected tool item type
     * @param type the tool type to retrieve
     * @return supplier for the specified tool type
     * @throws ClassCastException if the stored item is not of the expected type
     */
    @SuppressWarnings("unchecked")
    private <T extends Item> Supplier<T> getTool(ToolType type) {
        return (Supplier<T>) get(type);
    }


    /**
     * Builder for constructing {@link ToolSet} instances with a fluent API.
     * <p>
     * This builder enables detailed configuration of tool sets with per-tool
     * attack statistics, item properties, and custom creation logic.
     * </p>
     * <strong>Default Values:</strong>
     * <ul>
     *   <li>Attack damage: 0.0F for all tools</li>
     *   <li>Attack speed: 0.0F for all tools</li>
     *   <li>Properties supplier: {@code Item.Properties::new}</li>
     *   <li>Customizer: {@link DefaultToolCustomizer#INSTANCE}</li>
     * </ul>
     *
     * <h2>Example Usage:</h2>
     * <pre>{@code
     * // Create netherite tools with custom stats
     * ToolSet netheriteTools = new ToolSet.Builder("netherite", Tiers.NETHERITE)
     *     .withToolStats(ToolType.AXE, 5.0F, 1.0F)   // More damage, faster axe
     *     .withToolStats(ToolType.HOE, -4.0F, 4.0F) // Same as other hoes (tweaked because of attackDamageBonus)
     *     .withItemProperties(properties -> properties.fireResistant().rarity(Rarity.RARE))
     *     .build();
     * }</pre>
     *
     * @see ToolSet
     * @see ToolCustomizer
     * @since 1.0.0
     */
    public static class Builder {
        private final String name;
        private final ToolMaterial toolMaterial;
        private final EnumMap<ToolType, Float> attackDamage = new EnumMap<>(ToolType.class);
        private final EnumMap<ToolType, Float> attackSpeed = new EnumMap<>(ToolType.class);
        private UnaryOperator<Item.Properties> propertiesModifier = UnaryOperator.identity();
        private ToolCustomizer customizer = DefaultToolCustomizer.INSTANCE;
        private Consumer<ItemAttributeModifiers.Builder> additionalAttributes = builder -> {
        };

        /**
         * Constructs a new builder for a tool set with the specified base name and material.
         *
         * @param name         base name for tools (e.g., "iron"), will be appended with tool‑specific suffixes
         * @param toolMaterial material tier for all tools, defines base durability, mining level, and base damage
         * @throws NullPointerException     if {@code name} or {@code toolMaterial} is {@code null}
         * @throws IllegalArgumentException if {@code name} is empty or contains invalid characters (validation is deferred to registration)
         */
        public Builder(String name, ToolMaterial toolMaterial) {
            this.name = Objects.requireNonNull(name, "name");
            this.toolMaterial = Objects.requireNonNull(toolMaterial, "toolMaterial");
            initializeDefaultStats();
        }

        /**
         * Initializes default attack stats (0.0F) for all tool types.
         */
        private void initializeDefaultStats() {
            Arrays.stream(ToolType.values()).forEach(type -> {
                attackDamage.put(type, 0f);
                attackSpeed.put(type, 0f);
            });
        }

        /**
         * Configures attack statistics for all tools using arrays.
         * <p>
         * Array order must exactly match the {@link ToolType#values()} order:
         * [SWORD, AXE, PICKAXE, HOE, SHOVEL]
         * </p>
         * <p>
         * <strong>Note:</strong> Attack damage values are <em>bonuses</em> added
         * to the tier's base damage. Attack speed values are modifiers applied
         * to the base attack speed.
         * </p>
         *
         * @param damages attack damage bonuses for each tool type (added to tier base damage)
         * @param speeds  attack speed modifiers for each tool type
         * @return this builder for method chaining
         * @throws IllegalArgumentException if array lengths don't match the number of tool types (expected: 5)
         * @see #withToolStats(ToolType, float, float)
         */
        public Builder withToolStats(float[] damages, float[] speeds) {
            validateArrayStats(damages, speeds);
            ToolType[] types = ToolType.values();
            for (int i = 0; i < types.length; i++) {
                attackDamage.put(types[i], damages[i]);
                attackSpeed.put(types[i], speeds[i]);
            }
            return this;
        }

        /**
         * Configures attack statistics for a specific tool type.
         * <p>
         * Attack damage is added to the tier's base damage. Attack speed is a
         * modifier applied to the base attack speed.
         * </p>
         *
         * @param type   the tool type to configure
         * @param damage attack damage bonus (added to tier base damage)
         * @param speed  attack speed modifier
         * @return this builder for method chaining
         * @throws NullPointerException if {@code type} is {@code null}
         */
        public Builder withToolStats(ToolType type, float damage, float speed) {
            attackDamage.put(type, damage);
            attackSpeed.put(type, speed);
            return this;
        }

        /**
         * Applies vanilla Minecraft balance values to all tools.
         * <p>
         * Uses the standard damage bonuses and attack speeds from vanilla
         * Minecraft, specifically matching iron-tier tool statistics.
         * </p>
         * <strong>Applied Stats:</strong>
         * <table border="1">
         *   <caption>Vanilla Iron Tool Statistics</caption>
         *   <thead>
         *     <tr><th>Tool Type</th><th>Damage Bonus</th><th>Attack Speed</th></tr>
         *   </thead>
         *   <tbody>
         *     <tr><td>Sword</td><td>+3.0</td><td>1.6</td></tr>
         *     <tr><td>Axe</td><td>+6.0</td><td>0.9</td></tr>
         *     <tr><td>Pickaxe</td><td>+1.0</td><td>1.2</td></tr>
         *     <tr><td>Shovel</td><td>-2.0</td><td>3.0</td></tr>
         *     <tr><td>Hoe</td><td>+1.5</td><td>1.0</td></tr>
         *   </tbody>
         * </table>
         *
         * @return this builder for method chaining
         */
        public Builder withVanillaBalance() {
            return withToolStats(
                    new float[] { 3, 6, 1, -2.0F, 1.5F },
                    new float[] { 1.6F - 4, 0.9F - 4, 1.2F - 4, 3.0F - 4, 1.0F - 4 }
            );
        }

        /**
         * Sets a modifier for the {@link Item.Properties} used when creating each tool item.
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
         * Sets a custom tool creation strategy for specialized tool implementations.
         * <p>
         * This allows overriding the default tool creation behavior to implement
         * custom tool classes, modified attribute handling, or additional properties.
         * </p>
         *
         * @param customizer tool creation strategy implementation
         * @return this builder for method chaining
         * @throws NullPointerException if {@code customizer} is {@code null}
         * @see ToolCustomizer
         */
        public Builder withCustomizer(ToolCustomizer customizer) {
            this.customizer = Objects.requireNonNull(customizer, "customizer");
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
         * Constructs an immutable {@link ToolSet} with the current builder configuration.
         * <p>
         * Validates all configuration parameters and creates a new {@code ToolSet}
         * instance. The returned set is thread-safe and lazily initializes its tools.
         * </p>
         *
         * @return a new tool set with the configured properties
         * @throws IllegalStateException if required configuration is missing or invalid (currently none)
         */
        public ToolSet build() {
            return new ToolSet(this.name, this.toolMaterial, this.attackDamage, this.attackSpeed, this.propertiesModifier, this.customizer, this.additionalAttributes);
        }

        /**
         * Validates that attack stat arrays have the correct length.
         *
         * @param damages attack damage bonuses array
         * @param speeds  attack speed modifiers array
         * @throws IllegalArgumentException if array lengths don't match the expected number of tool types
         */
        private void validateArrayStats(float[] damages, float[] speeds) {
            int expected = ToolType.values().length;
            if (damages.length != expected || speeds.length != expected) {
                throw new IllegalArgumentException("Invalid stats array lengths. Expected " + expected + " elements. Tool order: " + Arrays.toString(ToolType.values()));
            }
        }
    }
}
