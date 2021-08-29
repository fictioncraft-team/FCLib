package fictioncraft.wintersteve25.example;

import fictioncraft.wintersteve25.fclib.api.json.objects.SimpleConfigObject;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.SimpleObjProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.templates.SimpleBlockProvider;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonSerializer;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;

import java.util.List;

public class EventTest {
    public static void inventoryChange(BlockEvent.EntityPlaceEvent event) {
        if (JsonExample.mapdata == null || !MiscHelper.isMapValid(JsonExample.mapdata.getConfigs())) return;

        List<SimpleConfigObject> itemUse = JsonExample.mapdata.getConfigs().get("test");

        if (MiscHelper.isListValid(itemUse)) {
            Entity entity = event.getEntity();
            if (entity instanceof ServerPlayerEntity) {
                World world = (World) event.getWorld();
                BlockPos pos = event.getPos();
                for (SimpleConfigObject cfg : itemUse) {
                    SimpleObjProvider objProvider = cfg.getTarget();
                    if (objProvider instanceof SimpleBlockProvider) {
                        SimpleBlockProvider bProvider = (SimpleBlockProvider) objProvider;

                        System.out.println(JsonSerializer.BlockSerializer.doesBlockMatch(JsonSerializer.BlockSerializer.getPair(world, pos), bProvider));
                    }
                }
            }
        }
    }
}
