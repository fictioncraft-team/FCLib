//package fictioncraft.wintersteve25.example;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import fictioncraft.wintersteve25.fclib.api.json.base.IJsonConfig;
//import fictioncraft.wintersteve25.fclib.api.json.objects.SimpleConfigObject;
//import fictioncraft.wintersteve25.fclib.api.json.objects.SimpleObjectMap;
//import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.condition.SimpleItemCondition;
//import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleItemProvider;
//import fictioncraft.wintersteve25.fclib.api.json.utils.JsonUtils;
//import net.minecraft.util.ResourceLocation;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
////the code that generated output.json
////registered by new JsonExample().registerConfig() in mod constructor
//public class JsonExample implements IJsonConfig {
//
//    public static SimpleObjectMap mapdata;
//    private final File file = JsonUtils.getConfigFile("example", false);
//
//    @Override
//    public void write() {
//        Logger logger = LogManager.getLogger("FCLibExampleConfig");
//
//        JsonUtils.createDirectory();
//
//        if (!file.exists()) {
//            PrintWriter writer = JsonUtils.createWriter(file, logger);
//
//            HashMap<String, List<ComplexConfigObject>> map = new HashMap<>();
//            List<ComplexConfigObject> list = new ArrayList<>();
//            list.add(new ComplexConfigObject(new SimpleItemCondition(new SimpleItemProvider("minecraft:iron_ingot", 0, "", false), JsonUtils.JsonHandTypes.DEFAULT));
//            map.putIfAbsent("test", list);
//            SimpleObjectMap a = new SimpleObjectMap(map);
//
//            JsonUtils.writeTemplateInConfig(writer, a, logger);
//        }
//    }
//
//    @Override
//    public void example() {
//
//    }
//
//    @Override
//    public void read() {
//        Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
//        try {
//            mapdata = gson.fromJson(new FileReader(file), SimpleObjectMap.class);
//        } catch (Exception ignore){}
//    }
//
//    @Override
//    public ResourceLocation UID() {
//        return new ResourceLocation("fclib", "example_config");
//    }
//
//    @Override
//    public SimpleObjectMap finishedConfig() {
//        return null;
//    }
//}
