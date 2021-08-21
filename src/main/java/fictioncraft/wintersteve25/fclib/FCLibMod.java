package fictioncraft.wintersteve25.fclib;

import fictioncraft.wintersteve25.fclib.common.json.base.JsonConfigBuilder;
import fictioncraft.wintersteve25.fclib.common.json.utils.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("fclib")
public class FCLibMod {

    static Logger logger = LogManager.getLogger("FCLib");

    public FCLibMod() {
        final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        logger.info("o/ Hi! I hope you are having a wonderful day :)");
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, FCLibConfig.SERVER_CONFIG);

        logger.info("Creating Jsons...");
        JsonUtils.createJson();

        forgeEventBus.addListener(FCLibMod::serverStarted);
        forgeEventBus.register(this);
    }

    public static void serverStarted(final FMLServerStartedEvent event) {
        logger.info("Loading Jsons...");
        JsonUtils.loadJson();
    }
}
