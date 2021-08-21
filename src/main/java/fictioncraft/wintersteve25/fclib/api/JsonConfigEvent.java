package fictioncraft.wintersteve25.fclib.api;

import fictioncraft.wintersteve25.fclib.common.json.base.IJsonConfig;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * This event is fired right before json configs are loaded
 * There are 3 stages in total when loading json config: Write config, print example and lastly read config
 * This event can be canceled to prevent json being loaded/created
 */
@Cancelable
public class JsonConfigEvent extends Event {
    private final IJsonConfig config;
    private final boolean printExample;
    private final JsonConfigLoadStages stage;

    public JsonConfigEvent(IJsonConfig config, boolean printExample, JsonConfigLoadStages stage) {
        this.config = config;
        this.printExample = printExample;
        this.stage = stage;
    }

    public IJsonConfig getConfig() {
        return config;
    }

    public boolean printExample() {
        return printExample;
    }

    public JsonConfigLoadStages getStage() {
        return stage;
    }

    public enum JsonConfigLoadStages {
        WRITE,
        EXAMPLE,
        READ
    }
}
