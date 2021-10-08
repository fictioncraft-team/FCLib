package fictioncraft.wintersteve25.fclib.api.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.Event;

public class PlayerMovedEvent extends Event {
    private final MovementTypes movement;
    private final PlayerEntity player;

    public PlayerMovedEvent(MovementTypes movement, PlayerEntity player) {
        this.movement = movement;
        this.player = player;
    }

    public MovementTypes getMovement() {
        return movement;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public enum MovementTypes {
        JUMP,
        SNEAK,
        W,
        A,
        S,
        D
    }
}
