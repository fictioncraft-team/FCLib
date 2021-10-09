package fictioncraft.wintersteve25.fclib.api.json.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.teamacronymcoders.packmode.PackModeAPIImpl;
import fictioncraft.wintersteve25.fclib.FCLibConfig;
import fictioncraft.wintersteve25.fclib.FCLibMod;
import fictioncraft.wintersteve25.fclib.api.events.Hooks;
import fictioncraft.wintersteve25.fclib.api.events.JsonConfigEvent;
import fictioncraft.wintersteve25.fclib.api.json.ErrorUtils;
import fictioncraft.wintersteve25.fclib.api.json.base.IJsonConfig;
import fictioncraft.wintersteve25.fclib.api.json.compat.PackModeCompat;
import fictioncraft.wintersteve25.fclib.common.helper.ModListHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.PrintWriter;

public class JsonUtils {
    public static final File directory = new File("fclib_configs");
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static PrintWriter createWriter(File file, Logger logger) {
        try {
            return new PrintWriter(file);
        } catch (Exception e) {
            errorOnCreation(e, logger);
        }

        return null;
    }

    public static Gson getGson() {
        return gson;
    }

    public static void errorOnCreation(Exception e, Logger logger) {
        logger.log(Level.ERROR, "Exception when trying to create config");
        e.printStackTrace();
    }

    public static void fileNotFoundError(Exception e, Logger logger) {
        logger.info("Config json file not found! Creating a new one..");
        e.printStackTrace();
    }

    public static File getConfigFile(String modid, boolean example) {
        if (ModListHelper.isPackModeLoaded()) {
            return PackModeCompat.getPackModeConfigFile(modid, example);
        }
        return getConfigFile(modid, example, "Normal");
    }

    public static File getConfigFile(String modid, boolean example, String packmode) {
        String mode = packmode;
        return example ? new File(directory.getPath() + File.separator + packmode + File.separator + modid + "_example.json") : new File(directory.getPath() + File.separator + packmode + File.separator + modid + ".json");
    }

    public static void writeTemplateInConfig(PrintWriter writer, Object template, Logger logger) {
        logger.info("Attemping to write object into file");
        if (writer == null) {
            logger.warn("writer is null. This shouldn't be happening.");
            return;
        }
        writer.print(gson.toJson(template));
        writer.close();
    }

    public static void createDirectory() {
        if (!directory.exists()) {
            directory.mkdir();
        }

        if (ModListHelper.isPackModeLoaded()) {
            createPackModeDirectory(PackModeCompat.getPackMode());
        } else {
            createPackModeDirectory("Normal");
        }
    }

    public static void createPackModeDirectory(String packmode) {
        File packModeDirectory = new File(directory.getPath() + File.separator + packmode);
        if (!packModeDirectory.exists()) {
            packModeDirectory.mkdir();
        }
    }

    public static String jsonStringFromObject(Object obj) {
        return gson.toJson(obj);
    }

    public static void createJson() {
        boolean printExample = FCLibConfig.GENERATE_EXAMPLE.get();

        for (IJsonConfig configs : FCLibMod.configManager.jsonConfigMap.keySet()) {
            if (configs != null) {
                if (Hooks.onJsonLoadPre(configs, printExample, JsonConfigEvent.JsonConfigLoadStages.WRITE)) {
                    configs.write();
                    Hooks.onJsonLoadPost(configs, FCLibConfig.GENERATE_EXAMPLE.get(), JsonConfigEvent.JsonConfigLoadStages.WRITE);
                    FCLibMod.logger.info("Created {} config", configs.UID());
                }
                if (Hooks.onJsonLoadPre(configs, printExample, JsonConfigEvent.JsonConfigLoadStages.EXAMPLE) && printExample) {
                    configs.example();
                    Hooks.onJsonLoadPost(configs, FCLibConfig.GENERATE_EXAMPLE.get(), JsonConfigEvent.JsonConfigLoadStages.EXAMPLE);
                    FCLibMod.logger.info("Created {} config example", configs.UID());
                }
            }
        }
    }

    public static void loadJson() {
        for (IJsonConfig configs : FCLibMod.configManager.jsonConfigMap.keySet()) {
            if (configs != null) {
                if (Hooks.onJsonLoadPre(configs, FCLibConfig.GENERATE_EXAMPLE.get(), JsonConfigEvent.JsonConfigLoadStages.READ)) {
                    try {
                        configs.read();
                    } catch (JsonSyntaxException e){
                        ErrorUtils.cacheError(e.getMessage(), configs);
                    }
                    Hooks.onJsonLoadPost(configs, FCLibConfig.GENERATE_EXAMPLE.get(), JsonConfigEvent.JsonConfigLoadStages.READ);
                    FCLibMod.logger.info("Read {} config", configs.UID());
                }
            }
        }
    }

    public enum JsonHandTypes {
        MAIN,
        OFF,
        DEFAULT
    }
}
