package fictioncraft.wintersteve25.fclib.content.base;

import fictioncraft.wintersteve25.fclib.utils.SlotArrangement;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.List;

public class FCLibContainerImpl extends FCLibContainer {
    public FCLibContainerImpl(
            int windowId,
            Level world,
            BlockPos pos,
            Inventory playerInventory,
            Player player,
            MenuType container,
            boolean shouldAddPlayerSlots,
            boolean shouldTrackWorking,
            boolean shouldTrackProgress,
            boolean shouldTrackTotalProgress,
            boolean shouldAddInternalInventory,
            List<SlotArrangement> internalSlotArrangement,
            Tuple<Integer, Integer> playerSlotStart
    ) {
        super(windowId, world, pos, playerInventory, player, container);

        if (shouldAddInternalInventory) {
            if (tileEntity != null) {
                tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    int index = 0;

                    for (SlotArrangement arrangement : internalSlotArrangement) {
                        addMachineSlot(h, index, arrangement);
                        index++;
                    }
                });
            }
        }

        if (shouldAddPlayerSlots) {
            addPlayerSlots(playerSlotStart.getA(), playerSlotStart.getB());
        }

        if (shouldTrackWorking) {
            trackWorking();
        }

        if (shouldTrackProgress) {
            trackProgress();
        }

        if (shouldTrackTotalProgress) {
            trackTotalProgress();
        }

        trackRedstoneInverted();
    }
}
