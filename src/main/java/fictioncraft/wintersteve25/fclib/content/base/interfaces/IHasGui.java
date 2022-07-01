package fictioncraft.wintersteve25.fclib.content.base.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;

/**
 * Should be implemented on a {@link fictioncraft.wintersteve25.fclib.content.base.blocks.FCLibMachineBlock}
 */
public interface IHasGui {
    AbstractContainerMenu container(int i, Level world, BlockPos pos, Inventory playerInventory, Player playerEntity);

    Component machineName();
}
