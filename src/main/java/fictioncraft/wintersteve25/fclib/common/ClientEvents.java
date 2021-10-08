package fictioncraft.wintersteve25.fclib.common;

import fictioncraft.wintersteve25.fclib.api.events.PlayerMovedEvent;
import fictioncraft.wintersteve25.fclib.common.network.FCLibNetworking;
import fictioncraft.wintersteve25.fclib.common.network.TriggerPlayerMovePacket;
import net.minecraft.util.MovementInput;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "fclib", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {
    @SubscribeEvent
    public static void userInput(InputUpdateEvent event) {
        PlayerMovedEvent.MovementTypes type = null;

        MovementInput input = event.getMovementInput();

        if (input.backKeyDown) {
            type = PlayerMovedEvent.MovementTypes.S;
        } else if (input.forwardKeyDown) {
            type = PlayerMovedEvent.MovementTypes.W;
        } else if (input.jump) {
            type = PlayerMovedEvent.MovementTypes.JUMP;
        } else if (input.leftKeyDown) {
            type = PlayerMovedEvent.MovementTypes.A;
        } else if (input.rightKeyDown) {
            type = PlayerMovedEvent.MovementTypes.D;
        } else if (input.sneaking) {
            type = PlayerMovedEvent.MovementTypes.SNEAK;
        }

        if (type == null) return;

        FCLibNetworking.sendToServer(new TriggerPlayerMovePacket(event.getPlayer().getUniqueID(), type));
    }
}
