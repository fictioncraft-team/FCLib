package fictioncraft.wintersteve25.fclib.common.interfaces;

import net.minecraft.item.Item;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;
import java.util.Map;

/**
 * Should be implemented in a {@link net.minecraft.block.Block} or {@link Item}
 */
public interface IFCDataGenObject<I extends ForgeRegistryEntry> {
    String regName();

    I getOg();

    default <T extends Item> void init(Map<IFCDataGenObject<I>, T> list, T blockItem) {
        list.putIfAbsent(this, blockItem);
    }

    default void init(List<IFCDataGenObject<I>> list) {
        list.add(this);
    }
}
