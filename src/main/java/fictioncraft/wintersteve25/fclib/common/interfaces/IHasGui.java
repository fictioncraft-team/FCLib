package fictioncraft.wintersteve25.fclib.common.interfaces;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Should be implemented in a {@link net.minecraft.block.Block}
 */
public interface IHasGui {
    Container container(int i, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity);

    String machineName();
}
