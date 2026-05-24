package net.xun.armory.api.item.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A custom tool item that integrates with Minecraft's tool system and supports
 * different tool types ({@link ToolType}), custom tool materials, attack damage,
 * attack speed, and additional attribute modifiers.
 *
 * <p>This class overrides several {@link Item} methods to provide specific
 * behavior for different tool types (e.g., swords take less durability damage
 * when hurting enemies). The tool's mining properties are defined via the
 * {@link Tool} data component, and attack attributes are applied through
 * {@link ItemAttributeModifiers}.
 *
 * @since 2.3.0
 */
public class ToolItem extends Item {
    private final ToolType type;

    /**
     * Constructs a new {@code ToolItem} with the given type, material, base properties,
     * attack damage, attack speed, and an optional consumer for additional attributes.
     *
     * @param type                the tool type (e.g., {@code SWORD}, {@code PICKAXE}); cannot be null
     * @param material            the tool material providing durability, speed, enchantment value, and repair items
     * @param properties          the base item properties (e.g., stack size, fire resistance)
     * @param attackDamage        the base attack damage to add (the material's damage bonus is added automatically)
     * @param attackSpeed         the attack speed modifier (usually a negative value for slower tools)
     * @param additionalAttributes a consumer to add extra attribute modifiers to the item (may be null)
     * @throws NullPointerException if {@code type} is null
     */
    public ToolItem(ToolType type, ToolMaterial material, Properties properties, float attackDamage, float attackSpeed, Consumer<ItemAttributeModifiers.Builder> additionalAttributes) {
        super(applyProperties(type, material, properties, attackDamage, attackSpeed, additionalAttributes));
        this.type = Objects.requireNonNull(type, "type");
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        if (type != ToolType.SWORD) return true;

        return !player.isCreative();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (type == ToolType.SWORD)
            stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
        else
            stack.hurtAndBreak(2, attacker, EquipmentSlot.MAINHAND);
    }

    /**
     * Applies the tool material and attack stats to the item properties,
     * building the required {@link Tool} and {@link ItemAttributeModifiers} components.
     *
     * @param type                the tool type
     * @param material            the tool material
     * @param base                the base item properties
     * @param attackDamage        the base attack damage (added to material's bonus)
     * @param attackSpeed         the attack speed modifier
     * @param additionalAttributes optional consumer for extra attributes
     * @return the configured item properties
     */
    private static Item.Properties applyProperties(ToolType type, ToolMaterial material, Item.Properties base, float attackDamage, float attackSpeed, Consumer<ItemAttributeModifiers.Builder> additionalAttributes) {
        Item.Properties properties = base
                .durability(material.durability())
                .repairable(material.repairItems())
                .enchantable(material.enchantmentValue());

        HolderGetter<Block> holdergetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);

        Tool tool = switch (type) {
            case SWORD -> createSword(holdergetter);
            case AXE -> createTool(holdergetter, material, BlockTags.MINEABLE_WITH_AXE);
            case PICKAXE -> createTool(holdergetter, material, BlockTags.MINEABLE_WITH_PICKAXE);
            case SHOVEL -> createTool(holdergetter, material, BlockTags.MINEABLE_WITH_SHOVEL);
            case HOE -> createTool(holdergetter, material, BlockTags.MINEABLE_WITH_HOE);
        };

        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        createToolAttributes(material, attackDamage, attackSpeed, builder);

        if (additionalAttributes != null) {
            additionalAttributes.accept(builder);
        }

        return properties
                .component(DataComponents.TOOL, tool)
                .attributes(builder.build());
    }

    public static Tool createSword(HolderGetter<Block> holderGetter) {
        return new Tool(
                List.of(
                        Tool.Rule.minesAndDrops(HolderSet.direct(Blocks.COBWEB.builtInRegistryHolder()), 15.0F),
                        Tool.Rule.overrideSpeed(holderGetter.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5F)
                ),
                1.0F,
                2
        );
    }

    public static Tool createTool(HolderGetter<Block> holderGetter, ToolMaterial material, TagKey<Block> mineable) {
        return new Tool(
                List.of(
                        Tool.Rule.deniesDrops(holderGetter.getOrThrow(material.incorrectBlocksForDrops())),
                        Tool.Rule.minesAndDrops(holderGetter.getOrThrow(mineable), material.speed())
                ),
                1.0F,
                1
        );
    }

    private static void createToolAttributes(ToolMaterial material, float attackDamage, float attackSpeed, ItemAttributeModifiers.Builder builder) {
        builder.add(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                        Item.BASE_ATTACK_DAMAGE_ID,
                        attackDamage + material.attackDamageBonus(),
                        AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
        );
        builder.add(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(
                        Item.BASE_ATTACK_SPEED_ID,
                        attackSpeed,
                        AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
        );

    }
}
