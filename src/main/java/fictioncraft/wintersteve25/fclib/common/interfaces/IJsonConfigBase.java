package fictioncraft.wintersteve25.fclib.common.interfaces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.PrintWriter;

public interface IJsonConfigBase {
    File directory = new File(FMLPaths.CONFIGDIR.get() + File.separator + "fclib_configs");

    static void createWriter(PrintWriter writer, File file, Logger logger) {
        try {
            writer = new PrintWriter(file);
        } catch (Exception e) {
            errorOnCreation(e, logger);
        }
    }

    static void errorOnCreation(Exception e, Logger logger) {
        logger.log(Level.ERROR, "Exception when trying to create config");
        e.printStackTrace();
    }

    static void fileNotFoundError(Exception e, Logger logger) {
        logger.info("Config json file not found! Creating a new one..");
        e.printStackTrace();
    }

    static File getConfigFile(String modid) {
        return new File(directory.getPath() + File.separator + modid + ".json");
    }

    static File getConfigExampleFile(String modid) {
        return new File(directory.getPath() + File.separator + modid + "_example.json");
    }

    static void writeTemplateInConfig(PrintWriter writer, Object template) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        writer.print(gson.toJson(template));
        writer.close();
    }

    default void createDirectory() {
        if (!directory.exists()) {
            directory.mkdir();
        }
    }
}
