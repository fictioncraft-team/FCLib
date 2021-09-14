package fictioncraft.wintersteve25.fclib.api.json.base;

import fictioncraft.wintersteve25.fclib.api.json.objects.SimpleObjectMap;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.io.File;

public interface IJsonConfig {

    void write();

    void example();

    void read();

    ResourceLocation UID();

    SimpleObjectMap finishedConfig();

    default File getDefaultFile() {
        return JsonUtils.getConfigFile(UID().getNamespace(), false);
    }

    default File getDefaultExample() {
        return JsonUtils.getConfigFile(UID().getNamespace(), true);
    }
}