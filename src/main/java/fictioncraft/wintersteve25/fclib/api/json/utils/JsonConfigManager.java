package fictioncraft.wintersteve25.fclib.api.json.utils;

import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import fictioncraft.wintersteve25.fclib.api.json.base.IJsonConfig;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JsonConfigManager {

    private static final Logger logger = LogManager.getLogger("FCLibJsonConfigManager");
    public static final Map<IJsonConfig, ResourceLocation> jsonConfigMap = new HashMap<>();

    public static Collection<IJsonConfig> getConfigsFromUID(ResourceLocation rlIn) {
        if (jsonConfigMap.containsValue(rlIn)) {
            return MiscHelper.getKeysWithValue(jsonConfigMap, rlIn);
        }
        logger.warn("Can not find valid config with the UID: {}", rlIn);
        return null;
    }

    public static ResourceLocation getUIDFromConfig(IJsonConfig config) {
        if (jsonConfigMap.get(config) != null) {
            return jsonConfigMap.get(config);
        }
        logger.warn("Can not find valid UID for config");
        return null;
    }

    public static Collection<ResourceLocation> getUIDs() {
        if (!jsonConfigMap.isEmpty()) {
            return jsonConfigMap.values();
        }
        logger.warn("Can not find valid configs");
        return null;
    }
}
