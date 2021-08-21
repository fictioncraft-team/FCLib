package fictioncraft.wintersteve25.fclib;

import net.minecraftforge.common.ForgeConfigSpec;

public class FCLibConfig {
    public static final String CAT_GENERAL = "general";
    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec.BooleanValue GENERATE_EXAMPLE;

    static {
        ForgeConfigSpec.Builder SERVERBUILDER = new ForgeConfigSpec.Builder();
        GENERATE_EXAMPLE = SERVERBUILDER.comment("Should fclib mods generate json config example (if there is any)?").define("generateExample", true);
        SERVER_CONFIG = SERVERBUILDER.build();
    }
}
