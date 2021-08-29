package fictioncraft.wintersteve25.fclib;

import com.mojang.brigadier.CommandDispatcher;
import fictioncraft.wintersteve25.fclib.api.json.ErrorUtils;
import fictioncraft.wintersteve25.fclib.api.json.commands.*;
import fictioncraft.wintersteve25.fclib.api.json.objects.ProviderType;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod("fclib")
public class FCLibMod {

    public static Logger logger = LogManager.getLogger("FCLib");
    public static final List<ProviderType> PROVIDER_TYPES = new ArrayList<>();

    public FCLibMod() {
        final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        logger.info("o/ Hi! I hope you are having a wonderful day :)");
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, FCLibConfig.SERVER_CONFIG);

        logger.info("Creating Jsons...");
        JsonUtils.createJson();

        forgeEventBus.addListener(FCLibMod::serverStarted);
        forgeEventBus.addListener(FCLibMod::playerLogIn);
        forgeEventBus.addListener(FCLibMod::registerCommands);
//        forgeEventBus.addListener(EventTest::inventoryChange);

        forgeEventBus.register(this);
    }

    public static void serverStarted(final FMLServerAboutToStartEvent event) {
        logger.info("Loading Jsons...");
        JsonUtils.loadJson();

//        JsonExample jsonExample = new JsonExample();
//        jsonExample.write();
//        jsonExample.read();
    }

    public static void playerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        ErrorUtils.handle(event.getPlayer());
    }

    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("fclib")
                .then(DumpInfoCommand.register(dispatcher))
                .then(SimpleCommands.registerReloadCommand(dispatcher)));

        logger.info("Registered Commands!");
    }
}
