package fictioncraft.wintersteve25.fclib.api.events;

import fictioncraft.wintersteve25.fclib.api.json.base.IJsonConfig;
import net.minecraftforge.common.MinecraftForge;

public class Hooks {
    public static boolean onJsonLoad(IJsonConfig config, boolean example, JsonConfigEvent.JsonConfigLoadStages stage) {
        JsonConfigEvent event = new JsonConfigEvent(config, example, stage);
        MinecraftForge.EVENT_BUS.post(event);
        return !event.isCanceled();
    }
}
