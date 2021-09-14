//package fictioncraft.wintersteve25.example;
//
//import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects.SimpleCommandArg;
//import fictioncraft.wintersteve25.fclib.api.json.utils.JsonSerializer;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraftforge.event.world.BlockEvent;
//
//public class EventTest {
//    public static void inventoryChange(BlockEvent.EntityPlaceEvent event) {
//        if (event.getEntity() instanceof PlayerEntity) {
//            PlayerEntity playerEntity = (PlayerEntity) event.getEntity();
//            JsonSerializer.Args.executeCommand(playerEntity, new SimpleCommandArg("/tp @p 1 1 1", true));
//        }
//    }
//}
