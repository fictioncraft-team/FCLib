package fictioncraft.wintersteve25.fclib.content.base;

import fictioncraft.wintersteve25.fclib.content.base.blocks.FCLibInvBE;
import fictioncraft.wintersteve25.fclib.content.base.blocks.FCLibBE;
import fictioncraft.wintersteve25.fclib.content.base.interfaces.IForceStoppable;
import fictioncraft.wintersteve25.fclib.content.base.interfaces.IHasProgress;
import fictioncraft.wintersteve25.fclib.content.base.interfaces.IWorkable;
import fictioncraft.wintersteve25.fclib.utils.SlotArrangement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;

public abstract class FCLibContainer extends AbstractContainerMenu {

    protected FCLibBE tileEntity;
    protected Player playerEntity;
    protected IItemHandler playerInventory;

    protected FCLibContainer(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, MenuType container) {
        super(container, windowId);

        tileEntity = (FCLibBE) (world.getBlockEntity(pos));
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.getSlot(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();

            int startPlayerInvIndex = getInvSize();
            int startPlayerHBIndex = getInvSize() + 27;
            int endPlayerInvIndex = slots.size();
            int startMachineIndex = 0;

            if (slot instanceof ONIMachineSlotHandler) {
                if (!this.moveItemStackTo(stack, startPlayerInvIndex, endPlayerInvIndex, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemstack);
            } else {
                if (index >= startPlayerHBIndex) {
                    if (!this.moveItemStackTo(stack, startMachineIndex, startPlayerHBIndex, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                if (index >= startPlayerInvIndex && index < endPlayerInvIndex) {
                    if (!this.moveItemStackTo(stack, startMachineIndex, startPlayerInvIndex, false)) {
                        if (!this.moveItemStackTo(stack, startPlayerHBIndex, endPlayerInvIndex, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player p_75145_1_) {
        if (tileEntity == null) {
            return false;
        }

        if (tileEntity.getLevel() == null) {
            return false;
        }

        return stillValid(ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()), playerEntity, tileEntity.getLevel().getBlockState(tileEntity.getBlockPos()).getBlock());
    }
    
    protected void trackProgress() {
        if (tileEntity instanceof IHasProgress hasProgress) {
            addDataSlot(new DataSlot() {
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
            addDataSlot(new DataSlot() {
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
        if (tileEntity instanceof IHasProgress hasProgress) {
            addDataSlot(new DataSlot() {
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
            addDataSlot(new DataSlot() {
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
        if (tileEntity instanceof IWorkable workable) {
            addDataSlot(new DataSlot() {
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

    public void trackRedstoneInverted() {
        if (tileEntity instanceof IForceStoppable forceStoppable) {
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return getForceStopped() & 0xffff;
                }

                @Override
                public void set(int value) {
                    int stored = getForceStopped() & 0xffff0000;
                    int cache = stored + (value & 0xffff);

                    forceStoppable.setForceStopped(cache == 1);
                }
            });
        } else {
            throw new UnsupportedOperationException("Trying to track working on a machine that does not support progress!");
        }
    }

    public int getProgress() {
        if (tileEntity instanceof IHasProgress hasProgress) {
            return hasProgress.getProgress();
        }
        throw new UnsupportedOperationException("trying to get progress on an tile that does not support progress");
    }

    public int getTotalProgress() {
        if (tileEntity instanceof IHasProgress hasProgress) {
            return hasProgress.getTotalProgress();
        }
        throw new UnsupportedOperationException("trying to get total progress on an tile that does not support progress");
    }

    public byte getWorking() {
        if (tileEntity instanceof IWorkable workable) {
            return (byte) (workable.getWorking() ? 1 : 0);
        }
        throw new UnsupportedOperationException("trying to get working on an tile that does not support progress");
    }

    public byte getForceStopped() {
        if (tileEntity instanceof IForceStoppable forceStoppable) {
            return (byte) (forceStoppable.getForceStopped() ? 1 : 0);
        }
        throw new UnsupportedOperationException("trying to get force stopped on an tile that does not support progress");
    }

    public FCLibBE getTileEntity() {
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

    protected void addMachineSlot(IItemHandler itemHandler, int index, SlotArrangement tuple) {
        addSlot(new ONIMachineSlotHandler(itemHandler, index, tuple.getPixelX(), tuple.getPixelY()));
    }
    
    public int getInvSize() {
        if (getTileEntity() instanceof FCLibInvBE invTE) {
            return invTE.getInvSize();
        }
        return 0;
    }

    public static class ONIMachineSlotHandler extends SlotItemHandler {
        public ONIMachineSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return getItemHandler().isItemValid(this.getSlotIndex(), stack);
        }
    }
}
