package fictioncraft.wintersteve25.fclib.common.network;

import fictioncraft.wintersteve25.fclib.api.events.Hooks;
import fictioncraft.wintersteve25.fclib.api.events.PlayerMovedEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class TriggerPlayerMovePacket {
    private final UUID player;
    private final PlayerMovedEvent.MovementTypes movementTypes;

    public TriggerPlayerMovePacket(UUID player, PlayerMovedEvent.MovementTypes movementTypes) {
        this.player = player;
        this.movementTypes = movementTypes;
    }

    public TriggerPlayerMovePacket(PacketBuffer buffer) {
        this.player = buffer.readUniqueId();
        this.movementTypes = Enum.valueOf(PlayerMovedEvent.MovementTypes.class, buffer.readString());
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeUniqueId(player);
        buffer.writeString(movementTypes.toString());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity playerEntity = ctx.get().getSender().getEntityWorld().getPlayerByUuid(player);
            Hooks.onPlayerMove(playerEntity, movementTypes);
        });
        ctx.get().setPacketHandled(true);
    }
}
