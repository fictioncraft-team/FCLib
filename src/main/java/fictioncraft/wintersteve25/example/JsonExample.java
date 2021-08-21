package fictioncraft.wintersteve25.example;

import fictioncraft.wintersteve25.fclib.common.json.base.IJsonConfig;
import fictioncraft.wintersteve25.fclib.common.json.objects.SimpleConfigObject;
import fictioncraft.wintersteve25.fclib.common.json.objects.SimpleObjectMap;
import fictioncraft.wintersteve25.fclib.common.json.objects.providers.SimpleItemProvider;
import fictioncraft.wintersteve25.fclib.common.json.utils.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//the code that generated output.json
//registered by new JsonExample().registerConfig() in mod constructor
public class JsonExample implements IJsonConfig {
    @Override
    public void write() {
        File file = JsonUtils.getConfigFile("example", false);
        Logger logger = LogManager.getLogger("FCLibExampleConfig");

        JsonUtils.createDirectory();

        if (!file.exists()) {
            PrintWriter writer = JsonUtils.createWriter(file, logger);

            HashMap<String, List<SimpleConfigObject>> map = new HashMap<>();
            List<SimpleConfigObject> list = new ArrayList<>();
            list.add(new SimpleConfigObject(new SimpleItemProvider("minecraft:carrot", 1, "", false)));
            map.putIfAbsent("test", list);
            SimpleObjectMap a = new SimpleObjectMap(map);

            JsonUtils.writeTemplateInConfig(writer, a, logger);
        }
    }

    @Override
    public void example() {

    }

    @Override
    public void read() {

    }

    @Override
    public ResourceLocation UID() {
        return new ResourceLocation("fclib", "example_config");
    }
}