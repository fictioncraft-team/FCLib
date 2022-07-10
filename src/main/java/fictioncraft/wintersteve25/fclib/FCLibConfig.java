package fictioncraft.wintersteve25.fclib;

import net.minecraftforge.common.ForgeConfigSpec;

public class FCLibConfig {
    public static final String CAT_GENERAL = "general";
    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec.BooleanValue GENERATE_EXAMPLE;
    public static ForgeConfigSpec.BooleanValue SHOW_ERRORS_IN_CHAT;

    static {
        ForgeConfigSpec.Builder SERVERBUILDER = new ForgeConfigSpec.Builder();
        GENERATE_EXAMPLE = SERVERBUILDER.comment("Should fclib mods generate json config example (if there is any)?").define("generateExample", true);
        SHOW_ERRORS_IN_CHAT = SERVERBUILDER.comment("Should fclib show script errors in chat?").define("showErrorsInChat", true);
        SERVER_CONFIG = SERVERBUILDER.build();
    }
}
