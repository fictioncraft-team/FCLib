package fictioncraft.wintersteve25.example;

import fictioncraft.wintersteve25.fclib.api.events.JsonConfigEvent;

public class EventTest {
    public static MainConfig CONFIG;

    public static void jsonRegister(JsonConfigEvent.Registration event) {
        CONFIG = new MainConfig();
        event.getManager().registerConfig(CONFIG);
    }
}
