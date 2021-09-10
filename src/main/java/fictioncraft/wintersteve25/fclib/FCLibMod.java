package fictioncraft.wintersteve25.fclib;

import com.mojang.brigadier.CommandDispatcher;
import fictioncraft.wintersteve25.example.JsonExample;
import fictioncraft.wintersteve25.fclib.api.json.ErrorUtils;
import fictioncraft.wintersteve25.fclib.api.json.commands.*;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.ArgProviderType;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.ObjProviderType;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonUtils;
import fictioncraft.wintersteve25.fclib.common.helper.ModListHelper;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod("fclib")
public class FCLibMod {

    public static Logger logger = LogManager.getLogger("FCLib");
    public static final List<ObjProviderType> OBJ_PROVIDER_TYPES = new ArrayList<>();
    public static final List<ArgProviderType> ARG_PROVIDER_TYPES = new ArrayList<>();

    public FCLibMod() {
        final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        logger.info("o/ Hi! I hope you are having a wonderful day :)");
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, FCLibConfig.SERVER_CONFIG);

        forgeEventBus.addListener(FCLibMod::serverStarted);
        forgeEventBus.addListener(FCLibMod::playerLogIn);
        forgeEventBus.addListener(FCLibMod::registerCommands);
        forgeEventBus.addListener(FCLibMod::commonStartup);
//        forgeEventBus.addListener(EventTest::inventoryChange);

        forgeEventBus.register(this);
    }

    public static void serverStarted(final FMLServerAboutToStartEvent event) {
        logger.info("Loading Jsons...");
        JsonUtils.loadJson();
    }

    public static void commonStartup(FMLCommonSetupEvent event) {
        logger.info("Creating Jsons...");
        JsonUtils.createJson();
    }

    public static void playerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        ErrorUtils.handle(event.getPlayer());
    }

    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("fclib")
                .then(DumpInfoCommand.register(dispatcher))
                .then(SimpleCommands.registerReloadCommand(dispatcher))
                .then(SimpleCommands.registerArgsCommand(dispatcher)));

        logger.info("Registered Commands!");
    }
}
