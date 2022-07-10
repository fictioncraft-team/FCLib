package fictioncraft.wintersteve25.fclib.api.json.base;

import fictioncraft.wintersteve25.fclib.FCLibMod;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleBlockProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleEntityProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleFluidProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleItemProvider;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import fictioncraft.wintersteve25.fclib.api.json.objects.SimpleConfigObject;
import fictioncraft.wintersteve25.fclib.api.json.objects.SimpleObjectMap;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonConfigBuilder<T extends SimpleObjectMap> {

    private final ResourceLocation UID;
    private final JsonConfig<T> config;

    private static final Map<String, List<SimpleConfigObject>> mapCacheT = new HashMap<>();
    private static final Map<String, List<SimpleConfigObject>> mapCacheE = new HashMap<>();

    public JsonConfigBuilder(Class<T> type, ResourceLocation UID) {
        this.UID = UID;

        config = new JsonConfig<T>() {
            @Override
            public Class<T> getOutputType() {
                return type;
            }

            @Override
            public ResourceLocation UID() {
                return UID;
            }
        };
    }

    public ResourceLocation getUID() {
        return UID;
    }

    public JsonConfigBuilder<T> addListToMap(String listID, List<SimpleConfigObject> list, boolean isTemplate) {
        if (isTemplate) {
            mapCacheT.putIfAbsent(listID, list);
        } else {
            mapCacheE.putIfAbsent(listID, list);
        }
        return this;
    }

    public JsonConfigBuilder<T> addConfigObjectToList(String listID, SimpleConfigObject configObject, boolean isTemplate) {
        if (isTemplate) {
            if (MiscHelper.isListValid(mapCacheT.get(listID))) {
                mapCacheT.get(listID).add(configObject);
            } else {
                List<SimpleConfigObject> list = new ArrayList<>();
                list.add(configObject);
                mapCacheT.putIfAbsent(listID, list);
            }
        } else {
            if (MiscHelper.isListValid(mapCacheE.get(listID))) {
                mapCacheE.get(listID).add(configObject);
            } else {
                List<SimpleConfigObject> list = new ArrayList<>();
                list.add(configObject);
                mapCacheE.putIfAbsent(listID, list);
            }
        }
        return this;
    }

    public JsonConfigBuilder<T> addItemTarget(boolean isTemplate, String name, int amount, String nbt, boolean isTag) {
        return addConfigObjectToList("item", new SimpleConfigObject(new SimpleItemProvider(name, amount, nbt, isTag)), isTemplate);
    }

    public JsonConfigBuilder<T> addFluidTarget(boolean isTemplate, String name, int amount, String nbt, boolean isTag) {
        return addConfigObjectToList("fluid", new SimpleConfigObject(new SimpleFluidProvider(name, amount, nbt, isTag)), isTemplate);
    }

    public JsonConfigBuilder<T> addEntityTarget(boolean isTemplate, String name, boolean isTag) {
        return addConfigObjectToList("entity", new SimpleConfigObject(new SimpleEntityProvider(name, isTag)), isTemplate);
    }

    public JsonConfigBuilder<T> addBlockTarget(boolean isTemplate, String name, boolean hasTE, String nbt, boolean isTag) {
        return addConfigObjectToList("block", new SimpleConfigObject(new SimpleBlockProvider(name, hasTE, nbt, isTag)), isTemplate);
    }

    @SuppressWarnings("unchecked")
    public JsonConfig<T> build() {
        config.setTemplate((T) new SimpleObjectMap(mapCacheT));
        config.setExample((T) new SimpleObjectMap(mapCacheE));
        FCLibMod.configManager.registerConfig(config);
        return config;
    }
}