package fictioncraft.wintersteve25.fclib.common.json.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fictioncraft.wintersteve25.fclib.FCLibConfig;
import fictioncraft.wintersteve25.fclib.api.Hooks;
import fictioncraft.wintersteve25.fclib.api.JsonConfigEvent;
import fictioncraft.wintersteve25.fclib.common.json.base.IJsonConfig;
import fictioncraft.wintersteve25.fclib.common.json.objects.providers.SimpleObjProvider;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.PrintWriter;

public class JsonUtils {
    public static final File directory = new File(FMLPaths.CONFIGDIR.get() + File.separator + "fclib_configs");

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
        if (template instanceof SimpleObjProvider) {
            SimpleObjProvider provider = (SimpleObjProvider) template;
            try {
                logger.info("Attempting to write template in file");
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.excluder().excludeField(provider.getClass().getField("type"), false);
                writer.print(gson.toJson(template));
                writer.close();
                return;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        writer.print(gson.toJson(template));
        writer.close();
    }

    public static void createDirectory() {
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public static void createJson() {
        boolean printExample = FCLibConfig.GENERATE_EXAMPLE.get();

        for (IJsonConfig configs : JsonConfigManager.jsonConfigMap.keySet()) {
            if (Hooks.onJsonLoad(configs, printExample, JsonConfigEvent.JsonConfigLoadStages.WRITE)) {
                configs.write();
            }
            if (Hooks.onJsonLoad(configs, printExample, JsonConfigEvent.JsonConfigLoadStages.EXAMPLE) && printExample) {
                configs.example();
            }
        }
    }

    public static void loadJson() {
        for (IJsonConfig configs : JsonConfigManager.jsonConfigMap.keySet()) {
            if (Hooks.onJsonLoad(configs, FCLibConfig.GENERATE_EXAMPLE.get(), JsonConfigEvent.JsonConfigLoadStages.READ)) {
                configs.read();
            }
        }
    }
}
