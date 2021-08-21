package fictioncraft.wintersteve25.fclib.common.json.objects;

import fictioncraft.wintersteve25.fclib.common.json.objects.providers.SimpleObjProvider;

public class SimpleConfigObject {
    private final SimpleObjProvider target;

    public SimpleConfigObject(SimpleObjProvider target) {
        this.target = target;
    }

    public SimpleObjProvider getTarget() {
        return target;
    }
}
