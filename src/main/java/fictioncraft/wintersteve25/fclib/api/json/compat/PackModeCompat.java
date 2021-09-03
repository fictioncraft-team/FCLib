package fictioncraft.wintersteve25.fclib.api.json.compat;

import com.teamacronymcoders.packmode.PackModeAPIImpl;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonUtils;

import java.io.File;

public class PackModeCompat {
    public static File getPackModeConfigFile(String modid, boolean example) {
        return JsonUtils.getConfigFile(modid, example, PackModeAPIImpl.getInstance().getPackMode());
    }
}
