package fictioncraft.wintersteve25.fclib.api.json.objects;

import java.util.List;
import java.util.Map;

public class SimpleObjectMap {
    private final Map<String, List<SimpleConfigObject>> configs;

    public SimpleObjectMap(Map<String, List<SimpleConfigObject>> in) {
        this.configs = in;
    }

    public Map<String, List<SimpleConfigObject>> getConfigs() {
        return configs;
    }

    public void addObject(String id, List<SimpleConfigObject> objToAdd) {
        configs.putIfAbsent(id, objToAdd);
    }
}
