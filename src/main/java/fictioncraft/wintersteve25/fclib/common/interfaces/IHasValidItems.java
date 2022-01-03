package fictioncraft.wintersteve25.fclib.common.interfaces;

import net.minecraft.item.ItemStack;

import java.util.function.BiPredicate;

/**
 * Should be implemented in a {@link fictioncraft.wintersteve25.fclib.common.base.FCLibTEInv}
 */
public interface IHasValidItems {
    BiPredicate<ItemStack, Integer> validItems();
}
