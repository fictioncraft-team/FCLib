package fictioncraft.wintersteve25.fclib.api.events;

import fictioncraft.wintersteve25.fclib.api.json.base.IJsonConfig;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonConfigManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
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

    public static void onJsonRegister(JsonConfigManager manager) {
        JsonConfigEvent.Registration event = new JsonConfigEvent.Registration(manager);
        MinecraftForge.EVENT_BUS.post(event);
    }

    public static boolean onEntityTransformPre(Entity entityOld, EntityType<?> entityType) {
        EntityTransformEvent.Pre event = new EntityTransformEvent.Pre(entityOld, entityType);
        MinecraftForge.EVENT_BUS.post(event);
        return !event.isCanceled();
    }

    public static void onEntityTransformPost(Entity entityOld, EntityType<?> entityType) {
        EntityTransformEvent.Post event = new EntityTransformEvent.Post(entityOld, entityType);
        MinecraftForge.EVENT_BUS.post(event);
    }
}
