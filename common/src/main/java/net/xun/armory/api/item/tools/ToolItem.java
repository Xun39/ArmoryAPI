package net.xun.armory.api.item.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ToolItem extends Item {
    private final ToolType type;

    public ToolItem(ToolType type, Tier tier, Properties properties, float attackDamage, float attackSpeed, Consumer<ItemAttributeModifiers.Builder> additionalAttributes) {
        super(applyProperties(type, tier, properties, attackDamage, attackSpeed, additionalAttributes));
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
    private static Item.Properties applyProperties(ToolType type, Tier material, Item.Properties base, float attackDamage, float attackSpeed, Consumer<ItemAttributeModifiers.Builder> additionalAttributes) {
        Tool tool = switch (type) {
            case SWORD -> createSword();
            case AXE -> createTool(material, BlockTags.MINEABLE_WITH_AXE);
            case PICKAXE -> createTool(material, BlockTags.MINEABLE_WITH_PICKAXE);
            case SHOVEL -> createTool(material, BlockTags.MINEABLE_WITH_SHOVEL);
            case HOE -> createTool(material, BlockTags.MINEABLE_WITH_HOE);
        };

        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        createToolAttributes(material, attackDamage, attackSpeed, builder);

        if (additionalAttributes != null) {
            additionalAttributes.accept(builder);
        }

        return base
                .component(DataComponents.TOOL, tool)
                .attributes(builder.build());
    }

    public static Tool createSword() {
        return new Tool(
                List.of(
                        Tool.Rule.minesAndDrops(List.of(Blocks.COBWEB), 15.0F),
                        Tool.Rule.overrideSpeed(BlockTags.SWORD_EFFICIENT, 1.5F)
                ),
                1.0F,
                2
        );
    }

    public static Tool createTool(Tier tier, TagKey<Block> mineable) {
        return new Tool(
                List.of(
                        Tool.Rule.deniesDrops(tier.getIncorrectBlocksForDrops()),
                        Tool.Rule.minesAndDrops(mineable, tier.getSpeed())
                ),
                1.0F,
                1
        );
    }

    private static void createToolAttributes(Tier tier, float attackDamage, float attackSpeed, ItemAttributeModifiers.Builder builder) {
        builder.add(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                        Item.BASE_ATTACK_DAMAGE_ID,
                        attackDamage + tier.getAttackDamageBonus(),
                        AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
        );
        builder.add(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(
                        Item.BASE_ATTACK_SPEED_ID,
                        attackSpeed - 4,
                        AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
        );

    }
}
