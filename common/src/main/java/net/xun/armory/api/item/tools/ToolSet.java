package net.xun.armory.api.item.tools;

import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.xun.armory.api.item.ItemSet;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * @since 1.0.0
 */
public class ToolSet extends ItemSet<ToolPieceType, Item> {

    private final Tier tier;
    private final Map<ToolPieceType, ToolStats> statsByPiece;
    private final ToolContext context;

    protected ToolSet(
            String name,
            Tier tier,
            List<ToolPieceType> pieces,
            Map<ToolPieceType, ToolStats> statsByPiece,
            UnaryOperator<Item.Properties> propertiesModifier,
            Consumer<ItemAttributeModifiers.Builder> additionalAttributes,
            ToolCustomizer customizer
    ) {
        super(name, pieces, makeFactory(name, tier, statsByPiece, propertiesModifier, additionalAttributes, customizer));

        this.tier = Objects.requireNonNull(tier, "tier");
        this.statsByPiece = Collections.unmodifiableMap(new LinkedHashMap<>(statsByPiece));
        this.context = new ToolContext(name, tier, statsByPiece, propertiesModifier, additionalAttributes);
    }

    private static BiFunction<ToolPieceType, Item.Properties, Item> makeFactory(
            String name,
            Tier tier,
            Map<ToolPieceType, ToolStats> statsByPiece,
            UnaryOperator<Item.Properties> propertiesModifier,
            Consumer<ItemAttributeModifiers.Builder> additionalAttributes,
            ToolCustomizer customizer
    ) {
        ToolContext context = new ToolContext(
                name,
                tier,
                Collections.unmodifiableMap(new LinkedHashMap<>(statsByPiece)),
                propertiesModifier,
                additionalAttributes
        );

        return (piece, properties) -> {
            Item.Properties finalProperties = context.applyProperties(piece, properties);
            return customizer.create(piece, context, finalProperties);
        };
    }

    public Tier getTier() {
        return tier;
    }

    public ToolStats getStats(ToolPieceType piece) {
        ToolStats stats = statsByPiece.get(piece);
        if (stats == null) {
            throw new IllegalArgumentException("Unknown piece: " + piece.getNameSuffix());
        }
        return stats;
    }

    public ToolContext getContext() {
        return context;
    }

    public Supplier<Item> getSword() {
        return super.get(VanillaToolPieces.SWORD);
    }
    public Supplier<Item> getAxe() {
        return super.get(VanillaToolPieces.AXE);
    }
    public Supplier<Item> getPickaxe() {
        return super.get(VanillaToolPieces.PICKAXE);
    }
    public Supplier<Item> getShovel() {
        return super.get(VanillaToolPieces.SHOVEL);
    }
    public Supplier<Item> getHoe() {
        return super.get(VanillaToolPieces.HOE);
    }

    public static Builder builder(String name, Tier tier) {
        return new Builder(name, tier);
    }

    /**
     * @since 1.0.0
     */
    public static class Builder {
        private final String name;
        private final Tier tier;
        private final List<ToolPieceType> pieces = new ArrayList<>();
        private final Map<ToolPieceType, ToolStats> statsByPiece = new LinkedHashMap<>();
        private UnaryOperator<Item.Properties> propertiesModifier = UnaryOperator.identity();
        private Consumer<ItemAttributeModifiers.Builder> additionalAttributes = builder -> {};
        private ToolCustomizer customizer = ToolCustomizer.DEFAULT;
        private AttributeHelper attributeHelper = null;

        /**
         * Constructs a new builder for a tool set with the specified base name and tier.
         *
         * @param name base name for tools (e.g., "iron"), will be appended with
         *             tool-specific suffixes
         * @param tier material tier for all tools, defines base durability,
         *             mining level, and base damage
         * @throws NullPointerException     if {@code name}, {@code tier}, or
         *                                  {@code attributeHelper} is {@code null}
         * @throws IllegalArgumentException if {@code name} is empty or contains
         *                                  invalid characters
         */
        private Builder(String name, Tier tier) {
            this.name = Objects.requireNonNull(name, "name");
            this.tier = Objects.requireNonNull(tier, "tier");
        }

        public Builder piece(ToolPieceType piece) {
            Objects.requireNonNull(piece, "piece");
            if (statsByPiece.containsKey(piece)) {
                throw new IllegalArgumentException("Duplicate piece: " + piece.getNameSuffix());
            }
            pieces.add(piece);
            statsByPiece.put(piece, piece.defaultStats());
            return this;
        }

        public Builder pieces(Collection<ToolPieceType> pieces) {
            Objects.requireNonNull(pieces, "pieces");
            for (ToolPieceType piece : pieces) {
                piece(piece);
            }
            return this;
        }

        /**
         * @deprecated
         */
        @Deprecated(forRemoval = true, since = "3.0.0")
        private void initializeDefaultStats() {
        }

        // too difficult to hardcode stats into an array
        @Deprecated(since = "3.0.0")
        public Builder withToolStats(float[] damages, float[] speeds) {
            validateArrayStats(damages, speeds);
            for (int i = 0; i < pieces.size(); i++) {
                statsByPiece.put(pieces.get(i), new ToolStats(damages[i], speeds[i]));
            }
            return this;
        }

        public Builder withToolStats(ToolPieceType piece, ToolStats stats) {
            if (!statsByPiece.containsKey(piece)) {
                throw new IllegalArgumentException("Unknown piece: " + piece.getNameSuffix());
            }
            statsByPiece.put(piece, new ToolStats(stats.attackDamage(), stats.attackSpeed()));
            return this;
        }

        /**
         * @deprecated now handled by ToolPieceType.defaultStats
         */
        @Deprecated(since = "3.0.0")
        public Builder withVanillaBalance() {
            if (pieces.size() != 5) {
                throw new IllegalStateException(
                        "withVanillaBalance() expects exactly 5 pieces in vanilla order"
                );
            }
            return withToolStats(
                    new float[] { 3.0F, 6.0F, 1.0F, 1.5F, -2.0F },
                    new float[] { 1.6F, 0.9F, 1.2F, 1.0F, 3.0F }
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
         * Sets a custom tool creation strategy for specialized tool implementations.
         * <p>
         * This allows overriding the default tool creation behavior to implement
         * custom tool classes, modified attribute handling, or additional properties.
         * </p>
         *
         * @param customizer tool creation strategy implementation
         * @return this builder for method chaining
         * @throws NullPointerException if {@code itemFactory} is {@code null}
         * @see ToolCustomizer
         */
        public Builder withCustomizer(ToolCustomizer customizer) {
            this.customizer = Objects.requireNonNull(customizer, "customizer");
            return this;
        }

        /**
         * @deprecated Use {@link #withAdditionalAttributes(Consumer)}
         */
        @Deprecated(forRemoval = true, since = "3.0.0")
        public Builder withAttributeHelper(AttributeHelper attributeHelper) {
            this.attributeHelper = attributeHelper;
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
         * @throws IllegalStateException if required configuration is missing or invalid
         */
        public ToolSet build() {
            if (pieces.isEmpty()) {
                throw new IllegalStateException("ToolSet '" + name + "' has no pieces");
            }
            return new ToolSet(name, tier, pieces, statsByPiece, propertiesModifier, additionalAttributes, customizer);
        }

        private void validateArrayStats(float[] damages, float[] speeds) {
            int expected = pieces.size();
            if (damages.length != expected || speeds.length != expected) {
                throw new IllegalArgumentException(
                        "Invalid stats array lengths. Expected " + expected + " elements, got damages="
                                + damages.length + ", speeds=" + speeds.length
                );
            }
        }
    }
}
