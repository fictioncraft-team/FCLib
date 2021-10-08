package fictioncraft.wintersteve25.example;

import fictioncraft.wintersteve25.fclib.FCLibMod;
import fictioncraft.wintersteve25.fclib.api.events.PlayerMovedEvent;

public class EventTest {

    public static void ha(PlayerMovedEvent event) {
        FCLibMod.logger.info(event.getPlayer().getName());
    }
}
