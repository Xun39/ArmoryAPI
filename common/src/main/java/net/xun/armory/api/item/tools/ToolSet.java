package net.xun.armory.api.item.tools;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.xun.armory.internal.util.LazyReference;

import java.util.*;
import java.util.function.Supplier;

/**
 * Represents a complete set of tools (sword, axe, pickaxe, shovel, hoe) with configurable attributes.
 * Use the nested {@link Builder} to configure and create tool sets with consistent properties.
 * <p>
 * This class provides a convenient way to create and manage related tool items with shared properties
 * and consistent naming conventions. Each tool in the set is lazily initialized and can be accessed
 * individually or as a complete collection.
 * </p>
 *
 * @see Builder
 * @see ToolType
 * @see ToolConfigurator
 */
public class ToolSet {

    private final String name;
    private final Map<ToolType, LazyReference<? extends Item>> tools = new EnumMap<>(ToolType.class);

    /**
     * Constructs a new ToolSet with the specified properties and attributes.
     *
     * @param name               The base name for all tools in the set
     * @param tier               The material tier for all tools
     * @param attackDamage       Map of attack damage bonuses for each tool type
     * @param attackSpeed        Map of attack speed modifiers for each tool type
     * @param propertiesSupplier Supplier for item properties applied to all tools
     * @param configuration      Tool creation strategy implementation
     * @param attributeHelper    Helper for applying attributes to item properties
     */
    protected ToolSet(String name,
                      Tier tier,
                      EnumMap<ToolType, Float> attackDamage,
                      EnumMap<ToolType, Float> attackSpeed,
                      Supplier<Item.Properties> propertiesSupplier,
                      ToolConfigurator configuration,
                      AttributeHelper attributeHelper) {

        for (ToolType type : ToolType.values()) {
            String fullName = name + type.getNameSuffix();

            Item.Properties toolProperties = propertiesSupplier.get();

            Item.Properties finalProperties = attributeHelper.applyAttributes(
                    toolProperties,
                    tier.getAttackDamageBonus() + attackDamage.get(type),
                    attackSpeed.get(type)
            );

            tools.put(type, new LazyReference<>(fullName,
                    () -> configuration.createTool(type, tier, finalProperties))
            );
        }
        this.name = name;
    }

    /**
     * Retrieves all tool items in this set for registration purposes.
     * <p>
     * This method returns a map of ResourceLocation to Supplier pairs that can be used
     * to register the tools with the game registry. Each tool is identified by a
     * ResourceLocation constructed from the provided modId and the tool's full name.
     * </p>
     *
     * @param modId Your mod ID used to construct ResourceLocation identifiers
     * @return Map of {@link ResourceLocation} keys to {@link Supplier} providers for tool items
     */
    public Map<ResourceLocation, Supplier<? extends Item>> getItemsForRegistration(String modId) {
        Map<ResourceLocation, Supplier<? extends Item>> items = new LinkedHashMap<>();

        for (Map.Entry<ToolType, LazyReference<? extends Item>> entry : tools.entrySet()) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(modId, entry.getValue().getName());
            items.put(id, entry.getValue());
        }

        return items;
    }

    /**
     * Gets the base name of this tool set.
     * <p>
     * This is the name without tool-specific suffixes (e.g., "diamond" for "diamond_sword").
     * </p>
     *
     * @return The base name of this tool set
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the sword item from this tool set.
     *
     * @return Supplier providing the registered {@link SwordItem}
     */
    public Supplier<SwordItem> getSword() {
        return getTool(ToolType.SWORD);
    }

    /**
     * Gets the axe item from this tool set.
     *
     * @return Supplier providing the registered {@link AxeItem}
     */
    public Supplier<AxeItem> getAxe() {
        return getTool(ToolType.AXE);
    }

    /**
     * Gets the pickaxe item from this tool set.
     *
     * @return Supplier providing the registered {@link PickaxeItem}
     */
    public Supplier<PickaxeItem> getPickaxe() {
        return getTool(ToolType.PICKAXE);
    }

    /**
     * Gets the hoe item from this tool set.
     *
     * @return Supplier providing the registered {@link HoeItem}
     */
    public Supplier<HoeItem> getHoe() {
        return getTool(ToolType.HOE);
    }

    /**
     * Gets the shovel item from this tool set.
     *
     * @return Supplier providing the registered {@link ShovelItem}
     */
    public Supplier<ShovelItem> getShovel() {
        return getTool(ToolType.SHOVEL);
    }

    @SuppressWarnings("unchecked")
    private <T extends Item> Supplier<T> getTool(ToolType type) {
        return (Supplier<T>) tools.get(type);
    }

    /**
     * Retrieves all registered tool items in this set.
     * <p>
     * This method returns a list containing all five tool items (sword, axe, pickaxe, shovel, hoe)
     * in the order defined by {@link ToolType#values()}. All items are initialized when this
     * method is called.
     * </p>
     *
     * @return List containing all tool items in this set
     */
    public List<Item> getAll() {
        return tools.values().stream()
                .map(supplier -> (Item) supplier.get())
                .toList();
    }


    /**
     * Builder for constructing {@link ToolSet} instances with flexible configuration.
     * <p>
     * Allows setting per-tool attributes, item properties, and creation behavior.
     * The builder provides methods to configure attack damage, attack speed, item properties,
     * and tool creation logic. Default values are initialized for all tool types.
     * </p>
     * <p>
     * Example usage:
     * <pre>{@code
     * ToolSet diamondTools = new ToolSet.Builder("diamond", Tiers.DIAMOND, new GenericAttributeHelper())
     *     .withVanillaBalance()
     *     .withItemPropertiesSupplier(() -> new Item.Properties().rarity(Rarity.RARE))
     *     .build();
     * }</pre>
     *
     * @see ToolSet
     * @see ToolConfigurator
     */
    public static class Builder {
        private final String name;
        private final Tier tier;
        private final EnumMap<ToolType, Float> attackDamage = new EnumMap<>(ToolType.class);
        private final EnumMap<ToolType, Float> attackSpeed = new EnumMap<>(ToolType.class);
        private Supplier<Item.Properties> propertiesSupplier = Item.Properties::new;
        private ToolConfigurator configuration = ToolConfigurator.DEFAULT;
        private final AttributeHelper attributeHelper;

        /**
         * Constructs a new builder for a tool set.
         * <p>
         * Initializes default stats (0 attack damage bonus, 0 attack speed) for all tool types.
         * </p>
         *
         * @param name            Base name for tools (appended with tool-specific suffixes)
         * @param tier            Material tier for all tools
         * @param attributeHelper Helper for applying item attributes to properties
         * @throws NullPointerException if name, tier, or attributeHelper is null
         */
        public Builder(String name, Tier tier, AttributeHelper attributeHelper) {
            this.name = name;
            this.tier = tier;
            this.attributeHelper = attributeHelper;
            initializeDefaultStats();
        }

        private void initializeDefaultStats() {
            Arrays.stream(ToolType.values()).forEach(type -> {
                attackDamage.put(type, 0f);
                attackSpeed.put(type, 0f);
            });
        }

        /**
         * Configures attack stats for all tools using arrays.
         * <p>
         * Array order must match {@link ToolType#values()}:
         * [SWORD, AXE, PICKAXE, SHOVEL, HOE]
         * </p>
         * <p>
         * Attack damage values are added to the tier's base damage.
         * Attack speed values are modifiers applied to the base speed.
         * </p>
         *
         * @param damages Attack damage bonuses (added to tier's base damage)
         * @param speeds  Attack speed modifiers
         * @return This builder for chaining
         * @throws IllegalArgumentException If array lengths don't match tool types
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
         * Configures attack stats for a specific tool type.
         * <p>
         * Attack damage value is added to the tier's base damage.
         * Attack speed value is a modifier applied to the base speed.
         * </p>
         *
         * @param type   Target tool type
         * @param damage Attack damage bonus (added to tier's base damage)
         * @param speed  Attack speed modifier
         * @return This builder for chaining
         * @throws NullPointerException if type is null
         */
        public Builder withToolStats(ToolType type, float damage, float speed) {
            attackDamage.put(type, damage);
            attackSpeed.put(type, speed);
            return this;
        }

        /**
         * Applies vanilla Minecraft balance values to all tools.
         * <p>
         * Uses standard damage bonuses and attack speeds from vanilla Minecraft.
         * The values correspond to iron tools stats.
         * </p>
         * <p>
         * Stats applied:
         * <ul>
         *   <li>Sword: +3 damage, 1.6 attack speed</li>
         *   <li>Axe: +6 damage, 0.9 attack speed</li>
         *   <li>Pickaxe: +1 damage, 1.2 attack speed</li>
         *   <li>Shovel: -2 damage, 3.0 attack speed</li>
         *   <li>Hoe: +1.5 damage, 1.0 attack speed</li>
         * </ul>
         *
         * @return This builder for chaining
         */
        public Builder withVanillaBalance() {
            return withToolStats(
                    new float[] { 3, 6, 1, -2.0F, 1.5F },
                    new float[] { 1.6F, 0.9F, 1.2F, 3.0F, 1.0F }
            );
        }

        /**
         * <b>Caution:</b> Sets shared item properties for all tools.
         * <p>
         * May cause attribute conflicts if properties are mutated internally.
         * Prefer {@link #withItemPropertiesSupplier}.
         * </p>
         *
         * @param properties Base properties for all tools
         * @return This builder for chaining
         * @throws NullPointerException if properties is null
         * @see #withItemPropertiesSupplier(Supplier)
         */
        public Builder withItemProperties(Item.Properties properties) {
            this.propertiesSupplier = () -> properties;
            return this;
        }

        /**
         * Sets item properties using a supplier (called per-tool during construction).
         * <p>
         * This method is safer than {@link #withItemProperties} for tool sets,
         * as it ensures each tool gets a fresh instance of properties if needed.
         * </p>
         *
         * @param propertiesSupplier Supplier providing base properties for each tool
         * @return This builder for chaining
         * @throws NullPointerException if propertiesSupplier is null
         * @see #withItemProperties(Item.Properties)
         */
        public Builder withItemPropertiesSupplier(Supplier<Item.Properties> propertiesSupplier) {
            this.propertiesSupplier = propertiesSupplier;
            return this;
        }

        /**
         * Sets custom tool creation logic.
         * <p>
         * Allows overriding the default tool creation behavior for specialized tool types
         * or custom tool implementations.
         * </p>
         *
         * @param configurator Tool creation strategy implementation
         * @return This builder for chaining
         * @throws NullPointerException if configurator is null
         * @see ToolConfigurator
         */
        public Builder withConfiguration(ToolConfigurator configurator) {
            this.configuration = configurator;
            return this;
        }

        /**
         * Constructs the configured {@link ToolSet}.
         * <p>
         * Validates all configuration and creates a new ToolSet instance with
         * the specified properties. The returned ToolSet is immutable.
         * </p>
         *
         * @return New tool set instance
         * @throws IllegalStateException if required configuration is invalid
         */
        public ToolSet build() {
            return new ToolSet(this.name, this.tier, this.attackDamage, this.attackSpeed, this.propertiesSupplier, this.configuration, this.attributeHelper);
        }

        private void validateArrayStats(float[] damages, float[] speeds) {
            int expected = ToolType.values().length;
            if (damages.length != expected || speeds.length != expected) {
                throw new IllegalArgumentException("Invalid stats array lengths. Expected " + expected + " elements. Tool order: " + Arrays.toString(ToolType.values()));
            }
        }
    }
}
