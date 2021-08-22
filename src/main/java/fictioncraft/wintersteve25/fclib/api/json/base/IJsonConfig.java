package fictioncraft.wintersteve25.fclib.api.json.base;

import fictioncraft.wintersteve25.fclib.api.json.utils.JsonConfigManager;
import net.minecraft.util.ResourceLocation;

public interface IJsonConfig {

    void write();

    void example();

    void read();

    ResourceLocation UID();

    /**
     * this method should be called in your mod constructor
     */
    default void registerConfig() {
        JsonConfigManager.jsonConfigMap.putIfAbsent(this, UID());
    }
}
