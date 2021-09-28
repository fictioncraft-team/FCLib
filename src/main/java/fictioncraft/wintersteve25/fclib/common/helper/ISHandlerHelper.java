package fictioncraft.wintersteve25.fclib.common.helper;

import fictioncraft.wintersteve25.fclib.common.base.FCLibTEInv;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class ISHandlerHelper {
    public static void dropInventory(FCLibTEInv invTE, World world, BlockState state, BlockPos pos, int inventorySize) {
        if(invTE != null) {
            for(int i = 0; i < inventorySize; i++) {
                ItemStack itemstack = invTE.getItemHandler().getStackInSlot(i);

                if(!itemstack.isEmpty()) {
                    InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
                }
            }

            world.notifyNeighborsOfStateChange(pos, state.getBlock());
        }
    }

    public static void dropInventory(IItemHandler invTE, World world, BlockState state, BlockPos pos, int inventorySize) {
        if(invTE != null) {
            for(int i = 0; i < inventorySize; i++) {
                ItemStack itemstack = invTE.getStackInSlot(i);

                if(!itemstack.isEmpty()) {
                    InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
                }
            }

            world.notifyNeighborsOfStateChange(pos, state.getBlock());
        }
    }

    public static void dropItem(World world, BlockPos pos, ItemStack itemStack) {
        if (world.isRemote()) return;
        if (itemStack.isEmpty()) return;
        InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
    }
}
