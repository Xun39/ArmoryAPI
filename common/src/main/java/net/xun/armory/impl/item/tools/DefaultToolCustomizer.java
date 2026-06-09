package net.xun.armory.impl.item.tools;

import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.xun.armory.api.item.tools.*;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

/**
 * @since 2.0.0
 */
@ApiStatus.Internal
public enum DefaultToolCustomizer implements ToolCustomizer {
    INSTANCE;

    @Override
    public Item create(ToolPieceType piece, ToolContext context, Item.Properties properties) {
        ToolStats stats = context.statsFor(piece);
        Item.Properties finalProps = context.applyProperties(piece, properties);

        return new Item(finalProps);
    }
}
