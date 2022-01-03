package fictioncraft.wintersteve25.fclib.common.base;

import fictioncraft.wintersteve25.fclib.common.interfaces.IHasProgress;
import fictioncraft.wintersteve25.fclib.common.interfaces.IHasValidItems;
import fictioncraft.wintersteve25.fclib.common.interfaces.IWorkable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiPredicate;

public class FCLibContainer extends Container {

    protected FCLibTE tileEntity;
    protected PlayerEntity playerEntity;
    protected IItemHandler playerInventory;

    protected FCLibContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, ContainerType container) {
        super(container, windowId);

        tileEntity = (FCLibTE) (world.getTileEntity(pos));
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.getSlot(index);
        if (slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();

            int startPlayerInvIndex = getInvSize();
            int startPlayerHBIndex = getInvSize() + 27;
            int endPlayerInvIndex = inventorySlots.size();
            int startMachineIndex = 0;

            if (slot instanceof FCLibMachineSlotHandler) {
                if (!this.mergeItemStack(stack, startPlayerInvIndex, endPlayerInvIndex, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack, itemstack);
            } else {
                if (index >= startPlayerHBIndex) {
                    if (!this.mergeItemStack(stack, startMachineIndex, startPlayerHBIndex, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                if (index >= startPlayerInvIndex && index < endPlayerInvIndex) {
                    if (!this.mergeItemStack(stack, startMachineIndex, startPlayerInvIndex, false)) {
                        if (!this.mergeItemStack(stack, startPlayerHBIndex, endPlayerInvIndex, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity p_75145_1_) {
        if (tileEntity == null) {
            return false;
        }

        if (tileEntity.getWorld() == null) {
            return false;
        }

        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, tileEntity.getWorld().getBlockState(tileEntity.getPos()).getBlock());
    }

    protected void trackProgress() {
        if (tileEntity instanceof IHasProgress) {
            IHasProgress hasProgress = (IHasProgress) tileEntity;
            trackInt(new IntReferenceHolder() {
                @Override
                public int get() {
                    return getProgress() & 0xffff;
                }

                @Override
                public void set(int value) {
                    int progressStored = getProgress() & 0xffff0000;
                    hasProgress.setProgress(progressStored + (value & 0xffff));
                }
            });
            trackInt(new IntReferenceHolder() {
                @Override
                public int get() {
                    return (getProgress() >> 16) & 0xffff;
                }

                @Override
                public void set(int value) {
                    int progressStored = getProgress() & 0x0000ffff;
                    hasProgress.setProgress(progressStored | (value << 16));
                }
            });
        } else {
            throw new UnsupportedOperationException("Trying to track progress on a machine that does not support progress!");
        }
    }

    protected void trackTotalProgress() {
        if (tileEntity instanceof IHasProgress) {
            IHasProgress hasProgress = (IHasProgress) tileEntity;
            trackInt(new IntReferenceHolder() {
                @Override
                public int get() {
                    return getTotalProgress() & 0xffff;
                }

                @Override
                public void set(int value) {
                    int progressStored = getTotalProgress() & 0xffff0000;
                    hasProgress.setTotalProgress(progressStored + (value & 0xffff));
                }
            });
            trackInt(new IntReferenceHolder() {
                @Override
                public int get() {
                    return (getTotalProgress() >> 16) & 0xffff;
                }

                @Override
                public void set(int value) {
                    int progressStored = getTotalProgress() & 0x0000ffff;
                    hasProgress.setTotalProgress(progressStored | (value << 16));
                }
            });
        } else {
            throw new UnsupportedOperationException("Trying to track total progress on a machine that does not support progress!");
        }
    }

    protected void trackWorking() {
        if (tileEntity instanceof IWorkable) {
            IWorkable workable = (IWorkable) tileEntity;
            trackInt(new IntReferenceHolder() {
                @Override
                public int get() {
                    return getWorking() & 0xffff;
                }

                @Override
                public void set(int value) {
                    int workingStored = getWorking() & 0xffff0000;
                    int cache = workingStored + (value & 0xffff);

                    workable.setWorking(cache == 1);
                }
            });
        } else {
            throw new UnsupportedOperationException("Trying to track working on a machine that does not support progress!");
        }
    }

    public int getProgress() {
        if (tileEntity instanceof IHasProgress) {
            IHasProgress hasProgress = (IHasProgress) tileEntity;
            return hasProgress.getProgress();
        }
        throw new UnsupportedOperationException("trying to get progress on an tile that does not support progress");
    }

    public int getTotalProgress() {
        if (tileEntity instanceof IHasProgress) {
            IHasProgress hasProgress = (IHasProgress) tileEntity;
            return hasProgress.getTotalProgress();
        }
        throw new UnsupportedOperationException("trying to get total progress on an tile that does not support progress");
    }

    public byte getWorking() {
        if (tileEntity instanceof IWorkable) {
            IWorkable workable = (IWorkable) tileEntity;
            return (byte) (workable.getWorking() ? 1 : 0);
        }
        throw new UnsupportedOperationException("trying to get working on an tile that does not support progress");
    }

    public FCLibTE getTileEntity() {
        return tileEntity;
    }

    protected int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    protected int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    protected void addPlayerSlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    protected void addMachineSlot(IItemHandler itemHandler, int index, Tuple<Integer, Integer> tuple) {
        addSlot(new FCLibMachineSlotHandler(itemHandler, index, tuple.getA(), tuple.getB()));
    }

    public int getInvSize() {
        if (getTileEntity() instanceof FCLibTEInv) {
            FCLibTEInv invTE = (FCLibTEInv) getTileEntity();
            return invTE.getInvSize();
        }
        return 0;
    }

    public BiPredicate<ItemStack, Integer> validItems() {
        if (tileEntity instanceof IHasValidItems) {
            IHasValidItems hasValidItems = (IHasValidItems) tileEntity;
            return hasValidItems.validItems();
        }
        return null;
    }

    public static class FCLibMachineSlotHandler extends SlotItemHandler {
        public FCLibMachineSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return getItemHandler().isItemValid(this.getSlotIndex(), stack);
        }
    }
}
