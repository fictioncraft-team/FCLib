package fictioncraft.wintersteve25.fclib;

import com.mojang.brigadier.CommandDispatcher;
import fictioncraft.wintersteve25.fclib.api.events.Hooks;
import fictioncraft.wintersteve25.fclib.api.json.ErrorUtils;
import fictioncraft.wintersteve25.fclib.api.json.commands.DumpInfoCommand;
import fictioncraft.wintersteve25.fclib.api.json.commands.SimpleCommands;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.ArgProviderType;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.ObjProviderType;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonConfigManager;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

//import fictioncraft.wintersteve25.example.EventTest;

@Mod("fclib")
public class FCLibMod {

    public static Logger logger = LogManager.getLogger("FCLib");
    public static final List<ObjProviderType> OBJ_PROVIDER_TYPES = new ArrayList<>();
    public static final List<ArgProviderType> ARG_PROVIDER_TYPES = new ArrayList<>();
    public static JsonConfigManager configManager = JsonConfigManager.getInstance();

    public FCLibMod() {
        logger.info("o/ Hi! I hope you are having a wonderful day :)");
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, FCLibConfig.SERVER_CONFIG);
        MinecraftForge.EVENT_BUS.addListener(FCLibMod::serverStartup);
        MinecraftForge.EVENT_BUS.addListener(FCLibMod::playerLogIn);
        MinecraftForge.EVENT_BUS.addListener(FCLibMod::registerCommands);
//        MinecraftForge.EVENT_BUS.addListener(EventTest::inventoryChange);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(FCLibMod::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void commonSetup(FMLLoadCompleteEvent event) {
        logger.info("Creating directory...");
        JsonUtils.createDirectory();
    }

    public static void serverStartup(final FMLServerAboutToStartEvent event) {
        logger.info("Creating Jsons...");
        Hooks.onJsonRegister(configManager);
        if (configManager.jsonConfigMap.isEmpty()) return;
        JsonUtils.createJson();
        JsonUtils.loadJson();
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
