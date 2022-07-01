package fictioncraft.wintersteve25.fclib.content.base.interfaces;

import net.minecraft.world.item.ItemStack;

import java.util.function.BiPredicate;

/**
 * Should be implemented on a {@link fictioncraft.wintersteve25.fclib.content.base.blocks.FCLibInvBE}
 */
public interface IHasValidItems {
    BiPredicate<ItemStack, Integer> validItemsPredicate();
}
