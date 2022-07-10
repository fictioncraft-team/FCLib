package fictioncraft.wintersteve25.fclib.common.helper;

import net.minecraftforge.fml.ModList;

public class ModListHelper {
    private static final ModList modList = ModList.get();

    public static boolean isModLoaded(String modId) {
        return modList.isLoaded(modId);
    }

    public static boolean isFCLibLoaded() {
        return isModLoaded("fclib");
    }

    public static boolean isPackModeLoaded() {
        return isModLoaded("packmode");
    }
}
