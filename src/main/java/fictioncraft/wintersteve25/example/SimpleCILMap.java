package fictioncraft.wintersteve25.example;

import fictioncraft.wintersteve25.fclib.api.json.objects.SimpleConfigObject;
import fictioncraft.wintersteve25.fclib.api.json.objects.SimpleObjectMap;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleCILMap extends SimpleObjectMap {
    private final Map<String, List<SimpleCILConfigObject>> configurations;

    public SimpleCILMap(Map<String, List<SimpleCILConfigObject>> in) {
        super(null);
        this.configurations = in;
    }

    @Override
    public Map<String, List<SimpleConfigObject>> getConfigs() {
        Map<String, List<SimpleConfigObject>> map = new HashMap<>();

        for (List<SimpleCILConfigObject> list : configurations.values()) {
            List<SimpleConfigObject> list1 = new ArrayList<>();

            for (SimpleCILConfigObject configObject : list) {
                list1.add(new SimpleConfigObject(configObject.getTarget()));
            }

            map.putIfAbsent(MiscHelper.getKeysWithValue(configurations, list).get(0), list1);
        }

        return map;
    }

    public Map<String, List<SimpleCILConfigObject>> getConfigurations() {
        return configurations;
    }
}
