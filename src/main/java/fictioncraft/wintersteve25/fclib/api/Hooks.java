package fictioncraft.wintersteve25.fclib.api;

import fictioncraft.wintersteve25.fclib.common.json.base.IJsonConfig;
import net.minecraftforge.common.MinecraftForge;

public class Hooks {
    public static boolean onJsonLoad(IJsonConfig config, boolean example, JsonConfigEvent.JsonConfigLoadStages stage) {
        JsonConfigEvent event = new JsonConfigEvent(config, example, stage);
        MinecraftForge.EVENT_BUS.post(event);
        return !event.isCanceled();
    }
}
