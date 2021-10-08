package fictioncraft.wintersteve25.fclib;

import com.mojang.brigadier.CommandDispatcher;
import fictioncraft.wintersteve25.example.EventTest;
import fictioncraft.wintersteve25.fclib.api.events.Hooks;
import fictioncraft.wintersteve25.fclib.api.json.ErrorUtils;
import fictioncraft.wintersteve25.fclib.api.json.commands.DumpInfoCommand;
import fictioncraft.wintersteve25.fclib.api.json.commands.SimpleCommands;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.ArgProviderType;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.condition.*;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects.*;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.ObjProviderType;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonConfigManager;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonUtils;
import fictioncraft.wintersteve25.fclib.common.network.FCLibNetworking;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.DamageSource;
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
    public static DamageSource GENERIC_DAMAGE = new DamageSource("generic_nobypass");

    public FCLibMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, FCLibConfig.SERVER_CONFIG);
        MinecraftForge.EVENT_BUS.addListener(FCLibMod::serverStartup);
        MinecraftForge.EVENT_BUS.addListener(FCLibMod::playerLogIn);
        MinecraftForge.EVENT_BUS.addListener(FCLibMod::registerCommands);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(FCLibMod::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void commonSetup(FMLLoadCompleteEvent event) {
        logger.info("o/ Hi! I hope you are having a wonderful day :)");
        FCLibNetworking.registerMessages();

        logger.info("Creating directory...");
        JsonUtils.createDirectory();

        ArgProviderType.registerProvider("E_Commands", "", SimpleCommandArg.class, null);
        ArgProviderType.registerProvider("E_Effects", "", SimpleEffectArg.class, null);
        ArgProviderType.registerProvider("E_SwingHand", "", SimpleSwingHandArg.class, null);
        ArgProviderType.registerProvider("E_Kill", "", SimpleKillArg.class, null);
        ArgProviderType.registerProvider("E_Transform", "", SimpleTransformArg.class, null);
        ArgProviderType.registerProvider("E_XP", "", SimpleExperienceArg.class, null);
        ArgProviderType.registerProvider("E_Hunger", "", SimpleHungerArg.class, null);
        ArgProviderType.registerProvider("E_Shrink", "", SimpleShrinkArg.class, null);
        ArgProviderType.registerProvider("E_Particle", "", SimpleParticleArg.class, null);
        ArgProviderType.registerProvider("E_Summon", "", SimpleSummonArg.class, null);
        ArgProviderType.registerProvider("E_Sound", "", SimpleSoundArg.class, null);
        ArgProviderType.registerProvider("E_ItemStack", "", SimpleGiveItemArg.class, null);
        ArgProviderType.registerProvider("E_Hurt", "", SimpleHurtArg.class, null);
        ArgProviderType.registerProvider("E_Damage", "", SimpleDamageItemArg.class, null);
        ArgProviderType.registerProvider("E_Cooldown", "", SimpleCooldownArg.class, null);

        ArgProviderType.registerProvider("C_ItemStack", "", SimpleItemCondition.class, null);
        ArgProviderType.registerProvider("C_Cooldown", "", SimpleCooldownCondition.class, null);
        ArgProviderType.registerProvider("C_XP", "", SimpleExperienceCondition.class, null);
        ArgProviderType.registerProvider("C_Hunger", "", SimpleHungerCondition.class, null);
        ArgProviderType.registerProvider("C_Effects", "", SimpleEffectCondition.class, null);
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
