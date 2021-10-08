package fictioncraft.wintersteve25.fclib.api.events;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class PlayerInventoryChangedEvent extends Event {
    private final ServerPlayerEntity serverPlayer;
    private final PlayerInventory inventory;
    private final ItemStack stack;
    private final int full;
    private final int empty;
    private final int occupied;

    public PlayerInventoryChangedEvent(ServerPlayerEntity serverPlayer, PlayerInventory inventory, ItemStack stack, int full, int empty, int occupied) {
        this.serverPlayer = serverPlayer;
        this.inventory = inventory;
        this.stack = stack;
        this.full = full;
        this.empty = empty;
        this.occupied = occupied;
    }

    public ServerPlayerEntity getServerPlayer() {
        return serverPlayer;
    }

    public PlayerInventory getInventory() {
        return inventory;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getFull() {
        return full;
    }

    public int getEmpty() {
        return empty;
    }

    public int getOccupied() {
        return occupied;
    }
}