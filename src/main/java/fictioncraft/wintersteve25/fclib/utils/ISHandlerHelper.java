package fictioncraft.wintersteve25.fclib.utils;

import fictioncraft.wintersteve25.fclib.content.base.blocks.FCLibInvBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;

public class ISHandlerHelper {
    public static void dropInventory(FCLibInvBE invTE, Level world, BlockState state, BlockPos pos, int inventorySize) {
        if(invTE != null) {
            for(int i = 0; i < inventorySize; i++) {
                ItemStack itemstack = invTE.getItemHandler().getStackInSlot(i);

                if(!itemstack.isEmpty()) {
                    Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
                }
            }

            world.updateNeighborsAt(pos, state.getBlock());
        }
    }

    public static void dropInventory(IItemHandler inv, Level world, BlockState state, BlockPos pos, int inventorySize) {
        if(inv != null) {
            for(int i = 0; i < inventorySize; i++) {
                ItemStack itemstack = inv.getStackInSlot(i);

                if(!itemstack.isEmpty()) {
                    Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
                }
            }

            world.updateNeighborsAt(pos, state.getBlock());
        }
    }
}
