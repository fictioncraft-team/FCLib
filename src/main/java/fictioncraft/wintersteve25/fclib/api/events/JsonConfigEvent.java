package fictioncraft.wintersteve25.fclib.api.events;

import fictioncraft.wintersteve25.fclib.api.json.base.IJsonConfig;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonConfigManager;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * There are two types, Pre and Post. Pre is fired before jsons are loaded. Post is after it is loaded
 * There are 3 stages in total when loading json config: Write config, print example and lastly read config
 * This event can be canceled to prevent json being loaded/created
 */

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

    @Cancelable
    public static class Pre extends JsonConfigEvent {
        public Pre(IJsonConfig config, boolean printExample, JsonConfigLoadStages stage) {
            super(config, printExample, stage);
        }

        @Override
        public boolean isCancelable() {
            return true;
        }
    }

    public static class Post extends JsonConfigEvent {
        public Post(IJsonConfig config, boolean printExample, JsonConfigLoadStages stage) {
            super(config, printExample, stage);
        }

        @Override
        public boolean isCancelable() {
            return false;
        }
    }

    public static class Registration extends Event {
        private final JsonConfigManager manager;

        public Registration(JsonConfigManager manager) {
            this.manager = manager;
        }

        public JsonConfigManager getManager() {
            return manager;
        }
    }
}
