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

public class ToolItem extends Item {
    private final ToolType type;

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
