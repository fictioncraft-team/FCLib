package fictioncraft.wintersteve25.fclib.common.interfaces;

import net.minecraft.item.Item;

import java.util.HashMap;

/**
 * Should be implemented in a {@link fictioncraft.wintersteve25.fclib.common.base.FCLibTEInv}
 */
public interface IHasValidItems {
    HashMap<Item, Integer> validItems();
}
