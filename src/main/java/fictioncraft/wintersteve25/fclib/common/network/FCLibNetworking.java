package fictioncraft.wintersteve25.fclib.common.network;

import fictioncraft.wintersteve25.fclib.FCLibMod;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class FCLibNetworking {
    private static SimpleChannel INSTANCE;
    private static int ID = 0;

    private static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        FCLibMod.logger.info("Registering FCLib networkings");

        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("fclib", "fclib_network"),
                () -> "1.0",
                s -> true,
                s -> true);
        INSTANCE.messageBuilder(TriggerPlayerMovePacket.class, nextID())
                .encoder(TriggerPlayerMovePacket::encode)
                .decoder(TriggerPlayerMovePacket::new)
                .consumer(TriggerPlayerMovePacket::handle)
                .add();
    }

    public static void sendToClient(Object packet, ServerPlayerEntity player) {
        INSTANCE.sendTo(packet, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }
}
