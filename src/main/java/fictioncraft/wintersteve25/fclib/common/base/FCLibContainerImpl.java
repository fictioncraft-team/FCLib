package fictioncraft.wintersteve25.fclib.common.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.List;

public abstract class FCLibContainerImpl extends FCLibContainer {
    protected FCLibContainerImpl(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, ContainerType container) {
        super(windowId, world, pos, playerInventory, player, container);

        if (shouldAddInternalInventory()) {
            if (tileEntity != null) {
                tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    int index = 0;

                    for (Tuple<Integer, Integer> tuple : internalInventorySlotsArrangement()) {
                        addMachineSlot(h, index, tuple);
                        index++;
                    }
                });
            }
        }

        if (shouldAddPlaySlots()) {
            addPlayerSlots(playerSlotArrangement().getA(), playerSlotArrangement().getB());
        }

        if (shouldTrackWorking()) {
            trackWorking();
        }

        if (shouldTrackProgress()) {
            trackProgress();
        }

        if (shouldTrackTotalProgress()) {
            trackTotalProgress();
        }
    }

    public abstract boolean shouldAddPlaySlots();

    public abstract boolean shouldTrackWorking();

    public abstract boolean shouldTrackProgress();

    public abstract boolean shouldTrackTotalProgress();

    public abstract boolean shouldAddInternalInventory();

    public abstract List<Tuple<Integer, Integer>> internalInventorySlotsArrangement();

    public Tuple<Integer, Integer> playerSlotArrangement() {
        return new Tuple<>(8, 88);
    }
}
