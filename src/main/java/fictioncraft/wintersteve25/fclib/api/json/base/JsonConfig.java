package fictioncraft.wintersteve25.fclib.api.json.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fictioncraft.wintersteve25.fclib.api.json.objects.SimpleObjectMap;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * This is an default implementation of IJsonConfig. It is used by JsonConfigBuilder if you just want simple json config. You can create your custom implementation of IJsonConfig if you need to do more advanced stuff
 */
public abstract class JsonConfig implements IJsonConfig {
    //this is where the loaded data be stored in, do NOObject modify data in this
    public SimpleObjectMap configData;

    private SimpleObjectMap template;
    private SimpleObjectMap example;

    Logger logger = LogManager.getLogger("FCLibJsonConfigBuilder");
    File configFile = JsonUtils.getConfigFile(UID().getNamespace(), false);
    File exampleFile = JsonUtils.getConfigFile(UID().getNamespace(), true);

    @Override
    public void write() {
        JsonUtils.createDirectory();
        if (!configFile.exists()) {
            PrintWriter writer = JsonUtils.createWriter(configFile, logger);
            JsonUtils.writeTemplateInConfig(writer, template, logger);
        }
    }

    @Override
    public void example() {
        JsonUtils.createDirectory();
        PrintWriter writer = JsonUtils.createWriter(exampleFile, logger);
        JsonUtils.writeTemplateInConfig(writer, example, logger);
    }

    @Override
    public void read() {
        if (!configFile.exists()) {
            logger.warn("{} Config json not found! Creating a new one..", UID().getNamespace());
            write();
        } else {
            try {
                logger.info("Attempting to read {}", configFile.getName());
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                SimpleObjectMap c = gson.fromJson(new FileReader(configFile), SimpleObjectMap.class);
                if (c != null) {
                    if (c.getConfigs() != null && !c.getConfigs().isEmpty()) {
                        configData = c;
                    }
                }
            } catch (FileNotFoundException e) {
                logger.warn("{} Config json configFile not found! Creating a new one..", UID().getNamespace());
                e.printStackTrace();
                write();
            }
        }
    }

    @Override
    public SimpleObjectMap finishedConfig() {
        return configData;
    }

    public SimpleObjectMap getTemplate() {
        return template;
    }

    public void setTemplate(SimpleObjectMap template) {
        this.template = template;
    }

    public SimpleObjectMap getExample() {
        return example;
    }

    public void setExample(SimpleObjectMap example) {
        this.example = example;
    }
}
