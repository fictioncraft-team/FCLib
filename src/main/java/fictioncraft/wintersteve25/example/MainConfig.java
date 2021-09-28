package fictioncraft.wintersteve25.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fictioncraft.wintersteve25.fclib.FCLibMod;
import fictioncraft.wintersteve25.fclib.api.json.base.IJsonConfig;
import fictioncraft.wintersteve25.fclib.api.json.objects.SimpleObjectMap;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.condition.SimpleItemCondition;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects.SimpleSwingHandArg;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleEntityProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleItemProvider;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonUtils;
import fictioncraft.wintersteve25.fclib.common.base.FCList;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainConfig implements IJsonConfig {

    public SimpleCILMap configData;

    private static final SimpleCILConfigObject ENTITY_CONFIG_OBJECT = new SimpleCILConfigObject(new SimpleEntityProvider("minecraft:enderman", false), new FCList<SimpleArgProvider>().addContent(new SimpleItemCondition(new SimpleItemProvider("minecraft:iron_ingot", 1, "", false), JsonUtils.JsonHandTypes.DEFAULT, false)), new FCList<SimpleArgProvider>().addContent(new SimpleSwingHandArg(JsonUtils.JsonHandTypes.MAIN)));

    private final File configFile = getDefaultFile();
    private final File exampleFile = getDefaultExample();

    private final Gson gson = JsonUtils.getGson();

    @Override
    public void write() {
        JsonUtils.createDirectory();
        if (!this.configFile.exists()) {
            PrintWriter writer = JsonUtils.createWriter(this.configFile, FCLibMod.logger);

            Map<String, List<SimpleCILConfigObject>> configObjectMap = new HashMap<>();
            List<SimpleCILConfigObject> emptyList = new ArrayList<>();
            configObjectMap.putIfAbsent("left", emptyList);
            configObjectMap.putIfAbsent("right", emptyList);
            SimpleCILMap map = new SimpleCILMap(configObjectMap);

            try {
                gson.excluder().excludeField(SimpleObjectMap.class.getDeclaredField("configs"), false);
            } catch (NoSuchFieldException ignored) {}

            writer.print(gson.toJson(map));
            writer.close();
        }
    }

    @Override
    public void example() {
        JsonUtils.createDirectory();
        PrintWriter writer = JsonUtils.createWriter(this.exampleFile, FCLibMod.logger);

        Map<String, List<SimpleCILConfigObject>> configObjectMap = new HashMap<>();

        List<SimpleCILConfigObject> entityList = new ArrayList<>();
        entityList.add(ENTITY_CONFIG_OBJECT);
        List<SimpleCILConfigObject> emptyList = new ArrayList<>();

        configObjectMap.putIfAbsent("left", entityList);
        configObjectMap.putIfAbsent("right", emptyList);

        try {
            gson.excluder().excludeField(SimpleObjectMap.class.getDeclaredField("configs"), false);
        } catch (NoSuchFieldException ignore) {}

        SimpleCILMap map = new SimpleCILMap(configObjectMap);

        writer.print(gson.toJson(map));
        writer.close();
    }

    @Override
    public void read() {
        if (!this.configFile.exists()) {
            FCLibMod.logger.warn("{} Config json not found! Creating a new one..", this.UID().getNamespace());
            this.write();
        } else {
            try {
                FCLibMod.logger.info("Attempting to read {}", this.configFile.getName());
                Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
                SimpleCILMap c = gson.fromJson(new FileReader(this.configFile), SimpleCILMap.class);
                if (c != null && c.getConfigurations() != null && !c.getConfigurations().isEmpty()) {
                    this.configData = c;
                }
            } catch (FileNotFoundException e) {
                FCLibMod.logger.warn("{} Config json configFile not found! Creating a new one..", this.UID().getNamespace());
                e.printStackTrace();
                this.write();
            }
        }
    }

    @Override
    public ResourceLocation UID() {
        return new ResourceLocation("fclib", "main");
    }

    @Override
    public SimpleObjectMap finishedConfig() {
        return configData;
    }
}
