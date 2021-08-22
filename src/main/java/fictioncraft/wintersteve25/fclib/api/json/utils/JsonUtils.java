package fictioncraft.wintersteve25.fclib.api.json.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fictioncraft.wintersteve25.fclib.FCLibConfig;
import fictioncraft.wintersteve25.fclib.api.events.Hooks;
import fictioncraft.wintersteve25.fclib.api.events.JsonConfigEvent;
import fictioncraft.wintersteve25.fclib.api.json.base.IJsonConfig;
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

    public static void errorOnCreation(Exception e, Logger logger) {
        logger.log(Level.ERROR, "Exception when trying to create config");
        e.printStackTrace();
    }

    public static void fileNotFoundError(Exception e, Logger logger) {
        logger.info("Config json file not found! Creating a new one..");
        e.printStackTrace();
    }

    public static File getConfigFile(String modid, boolean example) {
        return example ? new File(directory.getPath() + File.separator + modid + "_example.json") : new File(directory.getPath() + File.separator + modid + ".json");
    }

    public static void writeTemplateInConfig(PrintWriter writer, Object template, Logger logger) {
        writer.print(gson.toJson(template));
        writer.close();
    }

    public static void createDirectory() {
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public static String jsonStringFromObject(Object obj) {
        return gson.toJson(obj);
    }

    public static void createJson() {
        boolean printExample = FCLibConfig.GENERATE_EXAMPLE.get();

        for (IJsonConfig configs : JsonConfigManager.jsonConfigMap.keySet()) {
            if (configs != null) {
                if (Hooks.onJsonLoad(configs, printExample, JsonConfigEvent.JsonConfigLoadStages.WRITE)) {
                    configs.write();
                }
                if (Hooks.onJsonLoad(configs, printExample, JsonConfigEvent.JsonConfigLoadStages.EXAMPLE) && printExample) {
                    configs.example();
                }
            }
        }
    }

    public static void loadJson() {
        for (IJsonConfig configs : JsonConfigManager.jsonConfigMap.keySet()) {
            if (configs != null) {
                if (Hooks.onJsonLoad(configs, FCLibConfig.GENERATE_EXAMPLE.get(), JsonConfigEvent.JsonConfigLoadStages.READ)) {
                    configs.read();
                }
            }
        }
    }
}
