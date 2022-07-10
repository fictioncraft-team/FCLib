package fictioncraft.wintersteve25.fclib.api.json.utils;

import fictioncraft.wintersteve25.fclib.api.json.base.IJsonConfig;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JsonConfigManager {

    private final Logger logger = LogManager.getLogger("FCLibJsonConfigManager");
    public final Map<IJsonConfig, ResourceLocation> jsonConfigMap = new HashMap<>();

    private JsonConfigManager() {
    }

    public static JsonConfigManager getInstance() {
        return new JsonConfigManager();
    }

    public Collection<IJsonConfig> getConfigsFromUID(ResourceLocation rlIn) {
        if (this.jsonConfigMap.containsValue(rlIn)) {
            return MiscHelper.getKeysWithValue(this.jsonConfigMap, rlIn);
        }
        logger.warn("Can not find valid config with the UID: {}", rlIn);
        return null;
    }

    public ResourceLocation getUIDFromConfig(IJsonConfig config) {
        if (this.jsonConfigMap.get(config) != null) {
            return this.jsonConfigMap.get(config);
        }
        logger.warn("Can not find valid UID for config");
        return null;
    }

    public Collection<ResourceLocation> getUIDs() {
        if (!this.jsonConfigMap.isEmpty()) {
            return this.jsonConfigMap.values();
        }
        logger.warn("Can not find valid configs");
        return null;
    }

    public void registerConfig(IJsonConfig config) {
        if (jsonConfigMap.containsKey(config) || jsonConfigMap.containsValue(config.UID())) {
            logger.warn("Trying to register already registered config with ID {}!", config.UID());
        }
        this.jsonConfigMap.putIfAbsent(config, config.UID());
    }
}
