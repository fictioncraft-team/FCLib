package fictioncraft.wintersteve25.fclib.common.helper;

import fictioncraft.wintersteve25.fclib.common.base.FCLibTEInv;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ISHandlerHelper {
    public static void dropInventory(FCLibTEInv invTE, World world, BlockState state, BlockPos pos, int inventorySize) {
        if(invTE != null) {
            for(int i = 0; i < inventorySize; i++) {
                ItemStack itemstack = invTE.getItemHandler().getStackInSlot(i);

                if(!itemstack.isEmpty()) {
                    net.minecraft.inventory.InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
                }
            }

            world.notifyNeighborsOfStateChange(pos, state.getBlock());
        }
    }
}
