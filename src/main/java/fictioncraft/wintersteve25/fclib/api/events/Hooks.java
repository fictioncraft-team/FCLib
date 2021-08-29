package fictioncraft.wintersteve25.fclib.api.events;

import fictioncraft.wintersteve25.fclib.api.json.base.IJsonConfig;
import net.minecraftforge.common.MinecraftForge;

public class Hooks {
    public static boolean onJsonLoadPre(IJsonConfig config, boolean example, JsonConfigEvent.JsonConfigLoadStages stage) {
        JsonConfigEvent.Pre event = new JsonConfigEvent.Pre(config, example, stage);
        MinecraftForge.EVENT_BUS.post(event);
        return !event.isCanceled();
    }

    public static void onJsonLoadPost(IJsonConfig config, boolean example, JsonConfigEvent.JsonConfigLoadStages stage) {
        JsonConfigEvent.Post event = new JsonConfigEvent.Post(config, example, stage);
        MinecraftForge.EVENT_BUS.post(event);
    }
}
