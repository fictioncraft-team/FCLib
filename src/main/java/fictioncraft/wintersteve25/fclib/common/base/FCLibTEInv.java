package fictioncraft.wintersteve25.fclib.common.base;

import fictioncraft.wintersteve25.fclib.common.interfaces.IHasValidItems;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public abstract class FCLibTEInv extends FCLibTE {

    protected ItemStackHandler itemHandler = new FCLibInventoryHandler(this);
    protected LazyOptional<IItemHandler> itemLazyOptional = LazyOptional.of(() -> itemHandler);

    public FCLibTEInv(TileEntityType<?> te) {
        super(te);
    }

    public void onItemAdded() {
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public abstract int getInvSize();

    public boolean hasItem() {
        for (int i = 0; i < getInvSize(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        itemHandler.deserializeNBT(tag.getCompound("inv"));
        super.read(state, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("inv", itemHandler.serializeNBT());
        return super.write(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    public static class FCLibInventoryHandler extends ItemStackHandler {
        protected final FCLibTEInv tile;

        public FCLibInventoryHandler(FCLibTEInv inv) {
            this(inv.getInvSize(), inv);
        }

        public FCLibInventoryHandler(int size, FCLibTEInv inv) {
            super(size);
            tile = inv;
        }

        @Override
        public void onContentsChanged(int slot) {
            tile.updateBlock();
            tile.onItemAdded();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            if (!(tile instanceof IHasValidItems)) {
                return true;
            }
            IHasValidItems validItems = (IHasValidItems) tile;
            HashMap<Item, Integer> valids = validItems.validItems();
            if (valids == null || valids.isEmpty()) return true;
            if (valids.containsKey(stack.getItem())) {
                return slot == valids.get(stack.getItem()) || valids.get(stack.getItem()) < 0;
            }
            return false;
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (!(tile instanceof IHasValidItems)) {
                return super.insertItem(slot, stack, simulate);
            }
            IHasValidItems validItems = (IHasValidItems) tile;
            HashMap<Item, Integer> valids = validItems.validItems();
            if (valids == null || valids.isEmpty()) {
                return super.insertItem(slot, stack, simulate);
            }
            if (!valids.containsKey(stack.getItem())) {
                return stack;
            }
            if (valids.get(stack.getItem()) != slot || valids.get(stack.getItem()) == -1) {
                return stack;
            }
            return super.insertItem(slot, stack, simulate);
        }
    }
}
